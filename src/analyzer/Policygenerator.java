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
	ArrayList<policyYaml> policyYamlList;
	ArrayList<podYaml> podYamlList;
	ArrayList<nsYaml> nsYamlList;
	ArrayList<policies> policyList;
	ArrayList<HashMap<String, String>> policySelectedList;
	ArrayList<HashMap<String, String>> policyAllowedList;
	ArrayList<Boolean> policyMerged;
	ArrayList<pod> podList;
	HashMap<String, namespace> nsList;
	//input intent
	ArrayList<allowLink> links;
	HashMap<String, BitSet> podLabelHash;
	HashSet<String> fixLabels;
	
	public Policygenerator() {
		policyYamlList = new ArrayList<policyYaml>();
		podYamlList = new ArrayList<podYaml>();
		nsYamlList = new ArrayList<nsYaml>();
		policyList = new ArrayList<policies>();
		podList = new ArrayList<pod>();
		nsList = new HashMap<String, namespace>();
		links = new ArrayList<allowLink>();
		podLabelHash = new HashMap<String, BitSet>();
		fixLabels = new HashSet<String>();
		this.policySelectedList = new ArrayList<HashMap<String, String>>();
		this.policyAllowedList = new ArrayList<HashMap<String, String>>();
		this.policyMerged = new ArrayList<Boolean>();
	}
	
	public Policygenerator(BVgenerator Bg) {
		this.policyYamlList = Bg.getPolicyYamlList();
		this.podYamlList = Bg.getPodYamlList();
		this.nsYamlList = Bg.getNSYamlList();
		this.policyList = Bg.getPolicies();
		this.podList = Bg.getPods();
		this.nsList = Bg.getNamespaces();
		this.links = new ArrayList<allowLink>();
		this.podLabelHash = Bg.podLabelHash;
		this.fixLabels = new HashSet<String>();
		this.policySelectedList = new ArrayList<HashMap<String, String>>();
		this.policyAllowedList = new ArrayList<HashMap<String, String>>();
		this.policyMerged = new ArrayList<Boolean>();
	}
	
	public Policygenerator(Intentanalyzer IntentAnalyzer) {
		this(IntentAnalyzer.getIntent().getPodYamls(), IntentAnalyzer.getIntent().getLinks());
	}
	
	public Policygenerator(ArrayList<String> podinput, ArrayList<allowLink> linkinput) {
		policyYamlList = new ArrayList<policyYaml>();
		podYamlList = new ArrayList<podYaml>();
		nsYamlList = new ArrayList<nsYaml>();
		policyList = new ArrayList<policies>();
		podList = new ArrayList<pod>();
		nsList = new HashMap<String, namespace>();
		links = new ArrayList<allowLink>();
		podLabelHash = new HashMap<String, BitSet>();
		fixLabels = new HashSet<String>();
		this.policySelectedList = new ArrayList<HashMap<String, String>>();
		this.policyAllowedList = new ArrayList<HashMap<String, String>>();
		this.policyMerged = new ArrayList<Boolean>();

		// initiate pod
		for(String podyaml: podinput) {
			podYamlList.add(new podYaml(podyaml));
		}
		for(podYaml podyaml: podYamlList) {
			podList.add(podyaml.getPod());
		}
		
		// initiate NS
		
		// initiate allowLink
		for(allowLink link: linkinput) {
			links.add(link);
		}
		generateFix();
	}
	
	public ArrayList<policyYaml> getPolicyYamlList() {
		return policyYamlList;
	}

	public void setPolicyYamlList(ArrayList<policyYaml> policyYamlList) {
		this.policyYamlList = policyYamlList;
	}

	public ArrayList<podYaml> getPodYamlList() {
		return podYamlList;
	}

	public void setPodYamlList(ArrayList<podYaml> podYamlList) {
		this.podYamlList = podYamlList;
	}

	public ArrayList<nsYaml> getNsYamlList() {
		return nsYamlList;
	}

	public void setNsYamlList(ArrayList<nsYaml> nsYamlList) {
		this.nsYamlList = nsYamlList;
	}

	public ArrayList<policies> getPolicyList() {
		return policyList;
	}

	public void setPolicyList(ArrayList<policies> policyList) {
		this.policyList = policyList;
	}

	public ArrayList<pod> getPodList() {
		return podList;
	}

	public void setPodList(ArrayList<pod> podList) {
		this.podList = podList;
	}

	public HashMap<String, namespace> getNsList() {
		return nsList;
	}

	public void setNsList(HashMap<String, namespace> nsList) {
		this.nsList = nsList;
	}

	public ArrayList<allowLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<allowLink> links) {
		this.links = links;
	}

	public int getNS(String name) {
		for(int i = 0; i< nsList.size(); i++) {
			if(nsList.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	public void generateFix() {
		for(allowLink link : this.getLinks()) {
			this.generateFixSingle(link);
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
		BitSet haveIn = new BitSet(this.podList.size());
		BitSet haveE = new BitSet(this.podList.size());
		BitSet selectedPods = new BitSet(this.podList.size());
		BitSet allowedPods = new BitSet(this.podList.size());
		selectedPods.flip(0, selectedPods.size());
		allowedPods.flip(0, allowedPods.size());
		//// Record pods that all allow and all reject
		for(int i = 0; i < this.podList.size(); i++) {
			if(!this.podList.get(i).isSelectedIn()) {
				haveIn.set(i);
			}
			if(!this.podList.get(i).isSelectedE()) {
				haveE.set(i);
			}
		}
		// Find possible pods that may cause not unique in policy adding
		//// src/selected pod
		pod srcPod = this.podList.get(srcIndex);
		for(String key : srcPod.getLabels().keySet()) {
			selectedPods.and(podLabelHash.get(key));
		}
		for(int i = 0;;) {
			i = selectedPods.nextSetBit(i);
			if(i == -1) {
				break;
			}
			for(String key : srcPod.getLabels().keySet()) {
				if(this.podList.get(i).getLabel(key) != this.podList.get(srcIndex).getLabel(key)) {
					selectedPods.clear(i);
				}
			}
			i++;
		}
		//// dst/allowed pod
		pod dstPod = this.podList.get(dstIndex);
		for(String label : dstPod.getLabels().keySet()) {
			allowedPods.and(podLabelHash.get(label));
		}
		for(int i = 0;;) {
			i = allowedPods.nextSetBit(i);
			if(i == -1) {
				break;
			}
			for(String key : dstPod.getLabels().keySet()) {
				if(this.podList.get(i).getLabel(key) != this.podList.get(dstIndex).getLabel(key)) {
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
			if(!this.getPodList().get(i).getAllowPodIn().get(dstIndex)) {
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
			if(!this.getPodList().get(srcIndex).getAllowPodIn().get(i)) {
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
				if(!this.getPodList().get(i).getAllowPodIn().get(j)) {
					intersectFlag = true;
				}
				j++;
			}
			i++;
		}

		// add policy
		for(String key : this.getPodList().get(dstIndex).getLabels().keySet()) {
			this.policySelectedList.get(policyIndex).put(key, this.getPodList().get(dstIndex).getLabel(key));
		}
		for(String key : this.getPodList().get(srcIndex).getLabels().keySet()) {
			this.policyAllowedList.get(policyIndex).put(key, this.getPodList().get(srcIndex).getLabel(key));
		}
		if(intersectFlag) {
			// generate unique fix label
			if(!(allowedIntersectFlag & (!selectedIntersectFlag))) {
				String fixSelectLabel = this.generateFixLabel();
				// add label to select pods and policies
				this.getPodList().get(dstIndex).addLabel(fixSelectLabel, fixSelectLabel);
				this.policySelectedList.get(policyIndex).put(fixSelectLabel, fixSelectLabel);
			}
			if(allowedIntersectFlag) {
				String fixAllowLabel = this.generateFixLabel();
				// add label to allow pods and policies
				this.getPodList().get(srcIndex).addLabel(fixAllowLabel, fixAllowLabel);
				this.policyAllowedList.get(policyIndex).put(fixAllowLabel, fixAllowLabel);
			}
		}
	}
	
	public void mergePolicy() {
		// Tmply record the links which will be created by allowed links
		ArrayList<Boolean> tmpLinks = new ArrayList<Boolean>();
		for(allowLink link : this.links) {
			tmpLinks.add(this.getPodList().get(link.getDstIndex()).getAllowPodIn().get(link.getSrcIndex()));
			this.getPodList().get(link.getDstIndex()).getAllowPodIn().set(link.getSrcIndex());
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
					BitSet selectedPods = new BitSet(this.podList.size());
					BitSet allowedPods = new BitSet(this.podList.size());
					selectedPods.flip(0, selectedPods.size());
					allowedPods.flip(0, allowedPods.size());
					for(String key : selected.keySet()) {
						selectedPods.and(podLabelHash.get(key));
					}
					for(int k = 0;;) {
						k = selectedPods.nextSetBit(k);
						if(k == -1) {
							break;
						}
						for(String key : selected.keySet()) {
							if(this.podList.get(k).getLabel(key) != selected.get(key)) {
								selectedPods.clear(k);
							}
						}
						k++;
					}
					//// dst/allowed pod
					for(String key : allowed.keySet()) {
						allowedPods.and(podLabelHash.get(key));
					}
					for(int k = 0;;) {
						k = allowedPods.nextSetBit(k);
						if(k == -1) {
							break;
						}
						for(String key : allowed.keySet()) {
							if(this.podList.get(k).getLabel(key) != allowed.get(key)) {
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
							if(!this.getPodList().get(k).getAllowPodIn().get(l)) {
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
		for(int i = 0; i < this.links.size(); i++) {
			allowLink link = this.links.get(i);
			this.getPodList().get(link.getDstIndex()).getAllowPodIn().set(link.getSrcIndex(), tmpLinks.get(i));
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
			for(String allowKey : this.policyAllowedList.get(i).keySet()) {
				inFilter.setHavePodSelector(true);
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
			policyList.add(testPolicies);
			policyYamlList.add(testPolicies.generateYaml());
		}
	}

	public static void main(String args[]) {
		
		Policygenerator pg = new Policygenerator();
		// test main function

		// initiate policy
		for(int i = 0; i < 4; i++) {
			policyYaml policyyaml = new policyYaml("examples/test/testpolicy" + i + ".yaml");
			pg.getPolicyYamlList().add(policyyaml);
			pg.getPolicyList().add(policyyaml.getPolicies());
		}

		// initiate pod
		for(int i = 0; i < 6; i++) {
			podYaml podyaml = new podYaml("examples/test/testpod" + i + ".yaml");
			pg.getPodYamlList().add(podyaml);
			pg.getPodList().add(podyaml.getPod());
		}
		
		// initiate NS
		for(int i = 0; i < 4; i++) {
			nsYaml nsyaml = new nsYaml("examples/test/testns" + i + ".yaml");
			pg.getNsYamlList().add(nsyaml);
			pg.getNsList().put(nsyaml.getNS().getName(), nsyaml.getNS());
		}
		
		// initiate allowLink
		allowLink alink = new allowLink(0,1,80,false);
		pg.getLinks().add(alink);
		
		pg.generateFix();
		System.out.println(pg);
	}
}