package analyzer;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;

import analyzer.BVgenerator;
import bean.allowLink;
import bean.bitMatrix;
import bean.serviceChain;
import bean.resources.filter;
import bean.resources.namespace;
import bean.resources.pod;
import bean.resources.policies;
import bean.resources.policy;
import bean.resources.probe;
import bean.yaml.nsYaml;
import bean.yaml.podYaml;
import bean.yaml.policyYaml;
import utils.randomUtil;

public class Policygenerator{
	//input yamls
	BVgenerator bG;
	// For fix only including adding policy
	ArrayList<HashMap<String, String>> policySelectedList;
	ArrayList<HashMap<String, String>> policyAllowedList;
	ArrayList<Boolean> policyMerged;
	// For fix including deleting policy
	ArrayList<Integer> isolatePods;
	ArrayList<Integer> delPolicies;
	//ArrayList<pod> podList;
	//HashMap<String, namespace> nsList;
	//input intent
	ArrayList<allowLink> addLinks;
	ArrayList<allowLink> delLinks;
	//HashMap<String, BitSet> podLabelHash;
	HashSet<String> fixLabels;
	
	public Policygenerator() {
		this.bG = new BVgenerator();
		this.addLinks = new ArrayList<allowLink>();
		this.delLinks = new ArrayList<allowLink>();
		this.fixLabels = new HashSet<String>();
		this.policySelectedList = new ArrayList<HashMap<String, String>>();
		this.policyAllowedList = new ArrayList<HashMap<String, String>>();
		this.policyMerged = new ArrayList<Boolean>();
		this.isolatePods = new ArrayList<Integer>();
		this.delPolicies = new ArrayList<Integer>();
	}
	
	public Policygenerator(BVgenerator bG) {
		this.bG = bG;
		this.addLinks = new ArrayList<allowLink>();
		this.delLinks = new ArrayList<allowLink>();
		this.fixLabels = new HashSet<String>();
		this.policySelectedList = new ArrayList<HashMap<String, String>>();
		this.policyAllowedList = new ArrayList<HashMap<String, String>>();
		this.policyMerged = new ArrayList<Boolean>();
		this.isolatePods = new ArrayList<Integer>();
		this.delPolicies = new ArrayList<Integer>();
	}

	public ArrayList<allowLink> getAddLinks() {
		return addLinks;
	}

	public void setAddLinks(ArrayList<allowLink> addLinks) {
		this.addLinks = addLinks;
	}

	public ArrayList<allowLink> getDelLinks() {
		return delLinks;
	}

	public void setDelLinks(ArrayList<allowLink> delLinks) {
		this.delLinks = delLinks;
	}

	public void generateFix() {
		for(allowLink delLink : this.getDelLinks()) {
			this.generateDelSingle(delLink);
		}
		this.generateIsolate();
		for(allowLink addLink : this.getAddLinks()) {
			this.generateFixSingle(addLink);
		}
	}
	
	public void generateDelSingle(allowLink link) {
		if(!this.bG.getPods().get(link.getDstIndex()).getAllowPodIn().get(link.getSrcIndex())) {
			return;
		}
		if(!this.bG.getPods().get(link.getDstIndex()).isSelectedIn()) {
			// Need add isolating policy, and then add other links
			this.isolatePods.add(link.getDstIndex());
		}
		// Remove the policy creating this link
		ArrayList<Integer> rmPolicyList = new ArrayList<Integer>();
		for(int policyIndex : this.bG.getPods().get(link.getDstIndex()).getSelectedIndex()) {
			if(this.bG.getPolicies().get(link.getDstIndex()).getInAllow().get(link.getSrcIndex())) {
				rmPolicyList.add(policyIndex);
			}
		}
		if(rmPolicyList.size() == this.bG.getPods().get(link.getDstIndex()).getSelectedIndex().size()) {
			//Need add isolating policy, and then add other links
			this.isolatePods.add(link.getDstIndex());
		}else {
			for(int i = 0; i < rmPolicyList.size(); i++) {
				ArrayList<allowLink> affectedLinks = this.bG.removePolicy(rmPolicyList.get(i), true);
				// Adjust rmPolicy indexes
				for(int j = i; i < rmPolicyList.size(); i++) {
					if(rmPolicyList.get(j) > rmPolicyList.get(i)) {
						rmPolicyList.set(j, rmPolicyList.get(j) - 1);
					}
				}
				// If affected link in del links or already in add links, break
				for(allowLink affectedLink : affectedLinks) {
					for(allowLink tmpLink: this.addLinks) {
						if(affectedLink.getSrcIndex() == tmpLink.getSrcIndex() && affectedLink.getDstIndex() == tmpLink.getDstIndex()) {
							break;
						}
					}
					for(allowLink tmpLink: this.delLinks) {
						if(affectedLink.getSrcIndex() == tmpLink.getSrcIndex() && affectedLink.getDstIndex() == tmpLink.getDstIndex()) {
							break;
						}
					}
					this.addLinks.add(affectedLink);
				}
			}
		}
	}
	
	public void generateIsolate() {
		HashSet<Integer> isolated = new HashSet<Integer>();
		for(int podIndex : this.isolatePods) {
			if(!isolated.contains(podIndex)) {
				pod p = this.bG.getPods().get(podIndex);
				BitSet selectedPods = new BitSet(this.bG.getPods().size());
				selectedPods.flip(0, selectedPods.length());
				// Find other pods that may be selected
				for(String label: p.getLabels().keySet()) {
					selectedPods.and(this.bG.getPodLabelHash().get(label));
				}
				boolean affectOthersFlag = false;
				for(int i = 0;;) {
					i = selectedPods.nextSetBit(i);
					if(i == -1) {
						break;
					}
					boolean selectedFlag = true;
					for(String label: p.getLabels().keySet()) {
						if(p.getLabel(label) != this.bG.getPods().get(i).getLabel(label)) {
							selectedFlag = false;
						}
					}
					if(!selectedFlag) {
						selectedPods.clear(i);
					}else if((!this.isolatePods.contains(i)) && (!this.bG.getPods().get(i).isSelectedIn())){
						affectOthersFlag = true;
						break;
					}
					i++;
				}
				// Affects other no need to isolated pods
				if(affectOthersFlag) {
					//Add fix label
					String fixLabel = this.generateFixLabel();
					this.bG.getPods().get(podIndex).addLabel(fixLabel, fixLabel);
				}else {
					for(int i = 0;;) {
						i = selectedPods.nextSetBit(i);
						if(i == -1) {
							break;
						}
						isolated.add(i);
						i++;
					}
				}
				//Generate an isolate policy and add to bG
				policies testPolicies = new policies();
				policy inPolicy = new policy();
				testPolicies.setHaveIn(true); // Ingress policy
				testPolicies.setName(this.generateFixLabel());
				for(String selectKey : this.bG.getPods().get(podIndex).getLabels().keySet()) {
					testPolicies.putToPods(selectKey, this.bG.getPods().get(podIndex).getLabel(selectKey));
				}
				testPolicies.addToIn(inPolicy);
				this.bG.getPolicyYamlList().add(testPolicies.generateYaml());
				this.bG.addPolicy(testPolicies);
			}
			for(int i = 0; i < this.bG.getPods().size(); i++) {
				boolean delFlag = false;
				for(allowLink delLink : this.delLinks) {
					if(delLink.getSrcIndex() == i && delLink.getDstIndex() == podIndex) {
						delFlag = true;
						break;
					}
				}
				if(!delFlag) {
					this.addLinks.add(new allowLink(i, podIndex));
				}
			}
		}
	}
	
	public void generateFixSingle(allowLink link) {
		// add a new policy to add a link between Src and Dst
		// 1. the labels of Src and Dst are unique. Directly add.
		// 2. if not unique, check whether other pods are already/intently allowed
		// 3. there is no usable label, add new label and note it is a fix label
		//    and then ask whether needs to add this label to other pods
		// 4. there are many links need to be added. Aggregate
		int policyIndex = policySelectedList.size();
		policySelectedList.add(new HashMap<String, String>());
		policyAllowedList.add(new HashMap<String, String>());
		policyMerged.add(false);
		
		int srcIndex = link.getSrcIndex();
		int dstIndex = link.getDstIndex();
		// Pod which does not have in or have e should be added to both selected pods
		BitSet haveIn = new BitSet(this.bG.getPods().size());
		BitSet haveE = new BitSet(this.bG.getPods().size());
		BitSet selectedPods = new BitSet(this.bG.getPods().size());
		BitSet allowedPods = new BitSet(this.bG.getPods().size());
		selectedPods.flip(0, selectedPods.size());
		allowedPods.flip(0, allowedPods.size());
		//// Record pods that all allow and all reject
		for(int i = 0; i < this.bG.getPods().size(); i++) {
			if(!this.bG.getPods().get(i).isSelectedIn()) {
				haveIn.set(i);
			}
			if(!this.bG.getPods().get(i).isSelectedE()) {
				haveE.set(i);
			}
		}
		// Find possible pods that may cause not unique in policy adding
		//// src/selected pod
		pod srcPod = this.bG.getPods().get(srcIndex);
		for(String key : srcPod.getLabels().keySet()) {
			selectedPods.and(this.bG.getPodLabelHash().get(key));
		}
		for(int i = 0;;) {
			i = selectedPods.nextSetBit(i);
			if(i == -1) {
				break;
			}
			for(String key : srcPod.getLabels().keySet()) {
				if(this.bG.getPods().get(i).getLabel(key) != this.bG.getPods().get(srcIndex).getLabel(key)) {
					selectedPods.clear(i);
				}
			}
			i++;
		}
		//// dst/allowed pod
		pod dstPod = this.bG.getPods().get(dstIndex);
		for(String label : dstPod.getLabels().keySet()) {
			allowedPods.and(this.bG.getPodLabelHash().get(label));
		}
		for(int i = 0;;) {
			i = allowedPods.nextSetBit(i);
			if(i == -1) {
				break;
			}
			for(String key : dstPod.getLabels().keySet()) {
				if(this.bG.getPods().get(i).getLabel(key) != this.bG.getPods().get(dstIndex).getLabel(key)) {
					allowedPods.clear(i);
				}
			}
			i++;
		}
		// If not intersects unneeded links, directly add a policy
		boolean selectedIntersectFlag = false;
		boolean allowedIntersectFlag = false;
		boolean intersectFlag = false;
		//// If unneeded pods are selected 
		for(int i = 0;;) {
			i = selectedPods.nextSetBit(i);
			if(i == -1 || selectedIntersectFlag) {
				break;
			}
			if(!this.bG.getPods().get(i).getAllowPodIn().get(dstIndex)) {
				selectedIntersectFlag = true;
			}
			i++;
		}
		//// If unneeded pods are allowed
		for(int i = 0;;) {
			i = allowedPods.nextSetBit(i);
			if(i == -1 || allowedIntersectFlag) {
				break;
			}
			if(!this.bG.getPods().get(srcIndex).getAllowPodIn().get(i)) {
				allowedIntersectFlag = true;
			}
			i++;
		}
		//// If unneeded links between unnecessary src and dst pods
		intersectFlag = selectedIntersectFlag|allowedIntersectFlag;
		for(int i = 0;;) {
			i = selectedPods.nextSetBit(i);
			if(i == srcIndex) {
				i++;
				continue;
			}
			if(i == -1 || intersectFlag) {
				break;
			}
			for(int j = 0;;) {
				j = allowedPods.nextSetBit(j);
				if(j == dstIndex) {
					j++;
					continue;
				}
				if(j == -1 || intersectFlag) {
					break;
				}
				if(!this.bG.getPods().get(i).getAllowPodIn().get(j)) {
					intersectFlag = true;
				}
				j++;
			}
			i++;
		}

		// add policy
		for(String key : this.bG.getPods().get(dstIndex).getLabels().keySet()) {
			this.policySelectedList.get(policyIndex).put(key, this.bG.getPods().get(dstIndex).getLabel(key));
		}
		for(String key : this.bG.getPods().get(srcIndex).getLabels().keySet()) {
			this.policyAllowedList.get(policyIndex).put(key, this.bG.getPods().get(srcIndex).getLabel(key));
		}
		if(intersectFlag) {
			// generate unique fix label
			if(!(allowedIntersectFlag & (!selectedIntersectFlag))) {
				String fixSelectLabel = this.generateFixLabel();
				// add label to select pods and policies
				this.bG.getPods().get(dstIndex).addLabel(fixSelectLabel, fixSelectLabel);
				this.policySelectedList.get(policyIndex).put(fixSelectLabel, fixSelectLabel);
			}
			if(allowedIntersectFlag) {
				String fixAllowLabel = this.generateFixLabel();
				// add label to allow pods and policies
				this.bG.getPods().get(srcIndex).addLabel(fixAllowLabel, fixAllowLabel);
				this.policyAllowedList.get(policyIndex).put(fixAllowLabel, fixAllowLabel);
			}
		}
	}
	
	public void mergePolicy() {
		// Tmply record the links which will be created by allowed links
		ArrayList<Boolean> tmpLinks = new ArrayList<Boolean>();
		for(allowLink link : this.addLinks) {
			tmpLinks.add(this.bG.getPods().get(link.getDstIndex()).getAllowPodIn().get(link.getSrcIndex()));
			this.bG.getPods().get(link.getDstIndex()).getAllowPodIn().set(link.getSrcIndex());
		}
		
		ArrayList<Integer> mergedIndexs = new ArrayList<Integer>();
		for(int i = 0; i < this.policySelectedList.size(); i++) {
			HashMap<String, String> tmpSelected = this.policySelectedList.get(i);
			HashMap<String, String> tmpAllowed = this.policyAllowedList.get(i);
			for(int j = i + 1; j < this.policySelectedList.size(); j++) {
				if(mergedIndexs.contains(j)) {
					continue;
				}
				HashMap<String, String> otherSelected = this.policySelectedList.get(j);
				HashMap<String, String> selected = new HashMap<String, String>();
				for(String key : tmpSelected.keySet()) {
					if(otherSelected.keySet().contains(key)) {
						if(tmpSelected.get(key).equals(otherSelected.get(key))) {
							selected.put(key, tmpSelected.get(key));
						}
					}
				}
				if(selected.size() == 0) {
					continue;
				}
				HashMap<String, String> otherAllowed = this.policyAllowedList.get(j);
				HashMap<String, String> allowed = new HashMap<String, String>();
				for(String key : tmpAllowed.keySet()) {
					if(otherAllowed.keySet().contains(key)) {
						if(tmpAllowed.get(key).equals(otherAllowed.get(key))) {
							allowed.put(key, tmpAllowed.get(key));
						}
					}
				}
				if(allowed.size() == 0) {
					continue;
				}
				if(allowed.size() == tmpAllowed.size() && selected.size() == tmpSelected.size()) {
					mergedIndexs.add(j);
				}else if(allowed.size() == otherAllowed.size() && selected.size() == otherSelected.size()) {
					tmpSelected = selected;
					tmpAllowed = allowed;
					mergedIndexs.add(j);
				}else {
					// Find possible pods that may cause not unique in policy adding
					//// selected pod
					BitSet selectedPods = new BitSet(this.bG.getPods().size());
					BitSet allowedPods = new BitSet(this.bG.getPods().size());
					selectedPods.flip(0, selectedPods.size());
					allowedPods.flip(0, allowedPods.size());
					for(String key : selected.keySet()) {
						selectedPods.and(this.bG.getPodLabelHash().get(key));
					}
					for(int k = 0;;) {
						k = selectedPods.nextSetBit(k);
						if(k == -1) {
							break;
						}
						for(String key : selected.keySet()) {
							if(this.bG.getPods().get(k).getLabel(key) != selected.get(key)) {
								selectedPods.clear(k);
							}
						}
						k++;
					}
					//// dst/allowed pod
					for(String key : allowed.keySet()) {
						allowedPods.and(this.bG.getPodLabelHash().get(key));
					}
					for(int k = 0;;) {
						k = allowedPods.nextSetBit(k);
						if(k == -1) {
							break;
						}
						for(String key : allowed.keySet()) {
							if(this.bG.getPods().get(k).getLabel(key) != allowed.get(key)) {
								allowedPods.clear(k);
							}
						}
						k++;
					}
					// If not intersects unneeded links, directly add a policy
					boolean intersect_flag = false;
					for(int k = 0;;) {
						k = selectedPods.nextSetBit(k);
						if(k == -1 || intersect_flag) {
							break;
						}
						for(int l = 0;;) {
							l = allowedPods.nextSetBit(l);
							if(l == -1 || intersect_flag) {
								break;
							}
							if(!this.bG.getPods().get(k).getAllowPodIn().get(l)) {
								intersect_flag = true;
							}
							l++;
						}
						k++;
					}
					if(!intersect_flag) {
						tmpSelected = selected;
						tmpAllowed = allowed;
						mergedIndexs.add(j);
						this.policyMerged.set(j, true);
					}
				}
			}
			this.policyAllowedList.set(i, tmpAllowed);
			this.policySelectedList.set(i, tmpSelected);
		}
		// Reset tmply links
		for(int i = 0; i < this.addLinks.size(); i++) {
			allowLink link = this.addLinks.get(i);
			this.bG.getPods().get(link.getDstIndex()).getAllowPodIn().set(link.getSrcIndex(), tmpLinks.get(i));
		}
	}

	public String generateFixLabel() {
		String randomStr = "";
		while(true) {
			randomStr = randomUtil.getRandomStr(10);
			if(!this.fixLabels.contains(randomStr)) {
				this.fixLabels.add(randomStr);
				break;
			}
		}
		return "fix_" + randomStr;
	}
	
	/**
	 * Generate policies according to policyAllowedList and policySelectedList
	 */
	public void generatePolicies() {
		//design an algorithm which uses existing label to generate policy
		for(int i = 0; i < this.policyAllowedList.size(); i++) {
			if(this.policyMerged.get(i)) {
				continue;
			}
			policies testPolicies = new policies();
			policy inPolicy = new policy();
			filter inFilter = new filter(false);
			testPolicies.setHaveIn(true); // Ingress policy
			testPolicies.setName(this.generateFixLabel());
			for(String selectKey : this.policySelectedList.get(i).keySet()) {
				testPolicies.putToPods(selectKey, this.policySelectedList.get(i).get(selectKey));
			}
			inFilter.setHavePodSelector(true);
			for(String allowKey : this.policyAllowedList.get(i).keySet()) {
				inFilter.putPodSelector(allowKey, this.policyAllowedList.get(i).get(allowKey));
			}
			inPolicy.addToFilters(inFilter);
			
			//testPolicies.setNamespace(podList.get(i).getNamespace()); // Set policy namespace
			//testPolicies.putToPods(selectKey, selectValue); // Pod Selector 
			//inFilter.setHaveNsSelector(true);
			//inFilter.putNsSelector(NSKey, NSValue);
			//sysFilter.setHaveNsSelector(true);
			//sysFilter.putNsSelector("role", "kube-system"); //XXX: need to attach label to kube-system
			//inPolicy.addToFilters(sysFilter);
			testPolicies.addToIn(inPolicy);
			this.bG.getPolicyYamlList().add(testPolicies.generateYaml());
			this.bG.addPolicy(testPolicies);
		}
	}

	public static void main(String args[]) {
		
	}
}