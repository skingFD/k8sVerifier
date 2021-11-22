package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

import bean.KVPair;
import bean.allowLink;
import bean.bitMatrix;
import bean.serviceChain;
import bean.resources.filter;
import bean.resources.namespace;
import bean.resources.pod;
import bean.resources.policies;
import bean.resources.policy;
import bean.yaml.nsYaml;
import bean.yaml.podYaml;
import bean.yaml.policyYaml;

import org.yaml.snakeyaml.Yaml;

import utils.bitsetUtil;

//KV: Key-Value pair
//BV: BitVector
public class BVgenerator{
	ArrayList<policyYaml> PolicyYamlList; 
	ArrayList<policies> Policies;
	ArrayList<podYaml> PodYamlList;
	ArrayList<pod> Pods;
	ArrayList<nsYaml> NSYamlList;
	HashMap<String,namespace> Namespaces;
	ArrayList<KVPair> SelectorNSList;//store the corresponding relationship between KV pair and BitVector
	ArrayList<KVPair> SelectorPodList;
	ArrayList<KVPair> AllowNSList;
	ArrayList<KVPair> AllowPodList;
	ArrayList<String> AllowIPList;
	int SelectorNSLength;
	int SelectorPodLength;
	int AllowNSLength;
	int AllowPodLength;
	int AllowIPLength;
	bitMatrix InMatrix;
	bitMatrix EMatrix;
	bitMatrix ReachabilityMatrix;
	bitMatrix IntentMatrix;
	
	// For incremental operation
	HashMap<String,BitSet> podLabelHash;
	HashMap<String,BitSet> nsNameHash;
	HashMap<String,BitSet> nsLabelHash;
	ArrayList<String> nsNameList;
	
	public BVgenerator() {
		PolicyYamlList = new ArrayList<policyYaml>();
		Policies = new ArrayList<policies>();
		PodYamlList = new ArrayList<podYaml>();
		Pods = new ArrayList<pod>();
		NSYamlList  =new ArrayList<nsYaml>();
		Namespaces = new HashMap<String,namespace>();
		SelectorNSList = new ArrayList<KVPair>();
		SelectorPodList = new ArrayList<KVPair>();
		AllowNSList = new ArrayList<KVPair>();
		AllowPodList = new ArrayList<KVPair>();
		AllowIPList = new ArrayList<String>();
		SelectorNSLength = 0;
		SelectorPodLength = 0;
		AllowNSLength = 0;
		AllowPodLength = 0;
		AllowIPLength = 0;
		podLabelHash = new HashMap<String,BitSet>();
		nsNameHash = new HashMap<String,BitSet>();
		nsLabelHash = new HashMap<String,BitSet>();
		nsNameList = new ArrayList<String>();
	}

	public ArrayList<policyYaml> getPolicyYamlList() {
		return PolicyYamlList;
	}

	public void setPolicyYamlList(ArrayList<policyYaml> policyYamlList) {
		PolicyYamlList = policyYamlList;
	}

	public ArrayList<podYaml> getPodYamlList() {
		return PodYamlList;
	}

	public void setPodYamlList(ArrayList<podYaml> podYamlList) {
		PodYamlList = podYamlList;
	}

	public ArrayList<nsYaml> getNSYamlList() {
		return NSYamlList;
	}

	public void setNSYamlList(ArrayList<nsYaml> nSYamlList) {
		NSYamlList = nSYamlList;
	}

	public ArrayList<policies> getPolicies() {
		return Policies;
	}

	public void setPolicies(ArrayList<policies> policies) {
		Policies = policies;
	}

	public ArrayList<KVPair> getSelectorNSList() {
		return SelectorNSList;
	}

	public void setSelectorNSList(ArrayList<KVPair> selectorNSList) {
		SelectorNSList = selectorNSList;
	}

	public ArrayList<KVPair> getSelectorPodList() {
		return SelectorPodList;
	}

	public void setSelectorPodList(ArrayList<KVPair> selectorPodList) {
		SelectorPodList = selectorPodList;
	}

	public ArrayList<KVPair> getAllowNSList() {
		return AllowNSList;
	}

	public void setAllowNSList(ArrayList<KVPair> allowNSList) {
		AllowNSList = allowNSList;
	}

	public ArrayList<KVPair> getAllowPodList() {
		return AllowPodList;
	}

	public void setAllowPodList(ArrayList<KVPair> allowPodList) {
		AllowPodList = allowPodList;
	}

	public ArrayList<String> getAllowIPList() {
		return AllowIPList;
	}

	public void setAllowIPList(ArrayList<String> allowIPList) {
		AllowIPList = allowIPList;
	}

	public ArrayList<pod> getPods() {
		return Pods;
	}

	public void setPods(ArrayList<pod> pods) {
		Pods = pods;
	}

	public HashMap<String,namespace> getNamespaces() {
		return Namespaces;
	}

	public void setNamespaces(HashMap<String,namespace> namespaces) {
		Namespaces = namespaces;
	}
	
	
	public namespace getNamespace(String name) {
		return Namespaces.get(name);
	}

	public int getSelectorNSLength() {
		return SelectorNSLength;
	}

	public void setSelectorNSLength(int selectorNSLength) {
		SelectorNSLength = selectorNSLength;
	}

	public int getSelectorPodLength() {
		return SelectorPodLength;
	}

	public void setSelectorPodLength(int selectorPodLength) {
		SelectorPodLength = selectorPodLength;
	}

	public int getAllowNSLength() {
		return AllowNSLength;
	}

	public void setAllowNSLength(int allowNSLength) {
		AllowNSLength = allowNSLength;
	}

	public int getAllowPodLength() {
		return AllowPodLength;
	}

	public void setAllowPodLength(int allowPodLength) {
		AllowPodLength = allowPodLength;
	}

	public int getAllowIPLength() {
		return AllowIPLength;
	}

	public void setAllowIPLength(int allowIPLength) {
		AllowIPLength = allowIPLength;
	}
	
	public HashMap<String,BitSet> getPodLabelHash(){
		return this.podLabelHash;
	}

	public void yaml2Policies(){
		for(int i = 0; i < PolicyYamlList.size(); i++) {
			policyYaml temp = PolicyYamlList.get(i);
			Policies.add(temp.getPolicies());
		}
	}
	
	public void yaml2Pods() {
		for(int i = 0; i < PodYamlList.size(); i++) {
			podYaml temp = PodYamlList.get(i);
			Pods.add(temp.getPod());
		}
	}
	
	public void yaml2NS() {
		for(int i = 0; i < NSYamlList.size(); i++) {
			nsYaml temp = NSYamlList.get(i);
			Namespaces.put(temp.getNS().getName(),temp.getNS());
		}
	}
	
	/**
	 * verification with prefiltering
	 */
	public void prefilterVerify() {
		// Init all pods' allowE and allowIn bitset
		for(int i = 0; i < this.getPods().size(); i++) {
			this.getPods().get(i).setall(this.getPods().size());
		}
		
		// Init needed hashmaps
		//HashMap<String,BitSet> podLabelHash = new HashMap<String,BitSet>();
		//HashMap<String,BitSet> nsNameHash = new HashMap<String,BitSet>();
		//HashMap<String,BitSet> nsLabelHash = new HashMap<String,BitSet>();
		//ArrayList<String> nsNameList = new ArrayList<String>();
		
		// Hash pods by namespace, namespace labels and pod labels
		for(int i = 0; i < this.getPods().size();i++) {
			String nsName = this.getPods().get(i).getNamespace();
			if(nsNameHash.get(nsName)==null) {
				nsNameHash.put(nsName,new BitSet());
				nsNameHash.get(nsName).set(i);
			}else {
				nsNameHash.get(nsName).set(i);
			}
			for(String Key:this.getPods().get(i).getLabels().keySet()) {
				if(podLabelHash.get(Key)==null) {
					podLabelHash.put(Key,new BitSet());
					podLabelHash.get(Key).set(i);
				}else {
					podLabelHash.get(Key).set(i);
				}
			}
		}
		// Hash namespaces by namespace labels
		for(String Key: this.getNamespaces().keySet()) {
			nsNameList.add(Key);
		}
		
		for(int i = 0; i < nsNameList.size(); i++) {
			for(String LabelKey: this.getNamespaces().get(nsNameList.get(i)).getLabels().keySet()) {
				if(nsLabelHash.get(LabelKey)==null) {
					nsLabelHash.put(LabelKey, new BitSet());
					nsLabelHash.get(LabelKey).set(i);
				}else {
					nsLabelHash.get(LabelKey).set(i);
				}
			}
		}
		
		//Hash policies by namespace, namespace labels and pod labels
		//Hash result: select pods, allow pods
		for(int i = 0; i < this.getPolicies().size();i++) {
			BitSet selectedPodsBit = new BitSet(this.getPods().size());
			BitSet allowPodsInBit = new BitSet(this.getPods().size());
			BitSet allowPodsEBit = new BitSet(this.getPods().size());
			BitSet tempBit;
			BitSet tempPolicyBit;
			BitSet tempNsBit;
			//Select selected pods by namespace and podlabel
			if(nsNameHash.get(this.getPolicies().get(i).getNamespace())!=null) {
				selectedPodsBit.or(nsNameHash.get(this.getPolicies().get(i).getNamespace()));
			}
			for(String Key: this.getPolicies().get(i).getPods().keySet()) {
				if(podLabelHash.get(Key)!=null) {
					selectedPodsBit.and(podLabelHash.get(Key));
				}else {
					selectedPodsBit.clear();
				}
			}
			for(int j = 0;;) {
				j = selectedPodsBit.nextSetBit(j);
				if(j==-1) {
					break;
				}
				//get selected pods
				for(String Key: this.getPolicies().get(i).getPods().keySet()) {
					if(this.getPods().get(j).getLabel(Key).equals(this.getPolicies().get(i).getFromPods(Key))) {
						//If label matched
					}else {
						//If label not match
						selectedPodsBit.clear(j);
						break;
					}
				}
				j++;
			}
			//Set DDP2P map
			this.getPolicies().get(i).getSelectedPods().or(selectedPodsBit);
			for(int j = 0;;) {
				j = selectedPodsBit.nextSetBit(j);
				if(j==-1) {
					break;
				}
				this.getPods().get(j).getSelectedIndex().add(i);
				j++;
			}
			//Select allow pods by nsLabel and podLabel
			//In policy
			for(policy tempPolicy: this.getPolicies().get(i).getInPolicies()) {
				tempNsBit = new BitSet(nsNameList.size());
				tempNsBit.flip(0, nsNameList.size());
				tempPolicyBit = new BitSet(this.getPods().size());
				//tempPolicyBit.set(0, this.getPods().size());
				for(filter tempFilter: tempPolicy.getFilters()) {
					if(tempFilter.isHaveNsSelector()) {
						//Get all possible selected NS
						for(String Key: tempFilter.getNsSelector().keySet()) {
							if(nsLabelHash.get(Key)!=null) {
								tempNsBit.and(nsLabelHash.get(Key));
							}else {
								tempNsBit.clear();
							}
						}
						//Get all selected NS
						for(int j = 0;;) {
							j = tempNsBit.nextSetBit(j);
							if(j==-1) {
								break;
							}
							//get allowed pods
							//use nslabels
							for(String Key: tempFilter.getNsSelector().keySet()) {
								if(this.getNamespace(nsNameList.get(j)).getLabel(Key).equals(tempFilter.getNsSelector(Key))) {
									//If label matched
								}else {
									//If label not match
									tempNsBit.clear(j);
									break;
								}
							}
							j++;
						}
						//Cast NS to pods
						for(int j = 0;;) {
							j = tempNsBit.nextSetBit(j);
							if(j == -1) {
								break;
							}
							tempPolicyBit.or(nsNameHash.get(nsNameList.get(j)));
							j++;
						}
					}else {
						//If no nsSelector, use name to filter
						tempPolicyBit.or(nsNameHash.get(this.getPolicies().get(i).getNamespace()));
					}
					if(tempFilter.isHavePodSelector()) {
						for(String Key: tempFilter.getPodSelector().keySet()) {
							if(podLabelHash.get(Key)!=null) {
								tempPolicyBit.and(podLabelHash.get(Key));
							}else {
								tempPolicyBit.clear();
							}
						}
					}
					for(int j = 0;;) {
						j = tempPolicyBit.nextSetBit(j);
						if(j==-1) {
							break;
						}
						//get allowed pods
						//use labels
						if(tempFilter.isHavePodSelector()) {
							for(String Key: tempFilter.getPodSelector().keySet()) {
								if(this.getPods().get(j).getLabel(Key).equals(tempFilter.getPodSelector(Key))) {
									//If label matched
								}else {
									//If label not match
									tempPolicyBit.clear(j);
									break;
								}
							}
						}
						j++;
					}
				}
				
				allowPodsInBit.or(tempPolicyBit);
			}
			
			//Set DDP2P Map
			this.getPolicies().get(i).getInAllow().or(allowPodsInBit);
			for(int j = 0;;) {
				j = allowPodsInBit.nextSetBit(j);
				if(j==-1) {
					break;
				}
				this.getPods().get(j).getAllowInIndex().add(i);
				j++;
			}
			
			//E policy
			for(policy tempPolicy: this.getPolicies().get(i).getEPolicies()) {
				tempNsBit = new BitSet(nsNameList.size());
				tempNsBit.flip(0, nsNameList.size());
				tempPolicyBit = new BitSet(this.getPods().size());
				//tempPolicyBit.set(0, this.getPods().size());
				for(filter tempFilter: tempPolicy.getFilters()) {
					if(tempFilter.isHaveNsSelector()) {
						//Get all possible selected NS
						for(String Key: tempFilter.getNsSelector().keySet()) {
							if(nsLabelHash.get(Key)!=null) {
								tempNsBit.and(nsLabelHash.get(Key));
							}else {
								tempNsBit.clear();
							}
						}
						//Get all selected NS
						for(int j = 0;;) {
							j = tempNsBit.nextSetBit(j);
							if(j==-1) {
								break;
							}
							//get allowed pods
							//use nslabels
							for(String Key: tempFilter.getNsSelector().keySet()) {
								if(this.getNamespace(nsNameList.get(j)).getLabel(Key).equals(tempFilter.getNsSelector(Key))) {
									//If label matched
								}else {
									//If label not match
									tempNsBit.clear(j);
									break;
								}
							}
							j++;
						}
						//Cast NS to pods
						for(int j = 0;;) {
							j = tempNsBit.nextSetBit(j);
							if(j == -1) {
								break;
							}
							tempPolicyBit.or(nsNameHash.get(nsNameList.get(j)));
							j++;
						}
					}else {
						//If no nsSelector, use name to filter
						tempPolicyBit.or(nsNameHash.get(this.getPolicies().get(i).getNamespace()));
					}
					if(tempFilter.isHavePodSelector()) {
						for(String Key: tempFilter.getPodSelector().keySet()) {
							if(podLabelHash.get(Key)!=null) {
								tempPolicyBit.and(podLabelHash.get(Key));
							}else {
								tempPolicyBit.clear();
							}
						}
					}
					for(int j = 0;;) {
						j = tempPolicyBit.nextSetBit(j);
						if(j==-1) {
							break;
						}
						//get allowed pods
						//use labels
						if(tempFilter.isHavePodSelector()) {
							for(String Key: tempFilter.getPodSelector().keySet()) {
								if(this.getPods().get(j).getLabel(Key).equals(tempFilter.getPodSelector(Key))) {
									//If label matched
								}else {
									//If label not match
									tempPolicyBit.clear(j);
									break;
								}
							}
						}
						j++;
					}
				}
				allowPodsEBit.or(tempPolicyBit);
			}
			//Set DDP2P Map
			this.getPolicies().get(i).getEAllow().or(allowPodsEBit);
			for(int j = 0;;) {
				j = allowPodsEBit.nextSetBit(j);
				if(j==-1) {
					break;
				}
				this.getPods().get(j).getAllowEIndex().add(i);
				j++;
			}
			
			//Selected: selectedPodsBit
			//AllowedIn: allowPodsInBit
			//AllowedE: allowPodsEBit
			for(int j = 0;;) {
				j = selectedPodsBit.nextSetBit(j);
				if(j == -1) {
					break;
				}
				if(this.getPolicies().get(i).isHaveIn()) {
					this.Pods.get(j).setAllowPodIn(allowPodsInBit);
				}
				if(this.getPolicies().get(i).isHaveE()) {
					this.Pods.get(j).setAllowPodE(allowPodsEBit);
				}
				j++;
			}
			//calculateAllowMatrixs();
		}
	}
	
	/**
	 * naive verification
	 */
	public void naiveVerify() {
		// naive verification
		for (int i = 0; i < this.getPods().size(); i++) {
			this.getPods().get(i).setall(this.getPods().size());
		}
		for (int i = 0; i < this.getPolicies().size(); i++) {
			for (int j = 0; j < this.getPolicies().get(i).getInPolicies().size(); j++) {
				policy inPolicy = this.getPolicies().get(i).getInPolicies().get(j);
				for (int k = 0; k < this.getPods().size(); k++) {
					//this.getPolicies().get(i).getInPolicies().get(j).setAllow(k);
					namespace NS = this.getNamespace(this.getPods().get(k).getNamespace());
					inPolicy.calculateAllow(k, this.getPods().get(k), NS);
				}
			}
			for (int j = 0; j < this.getPolicies().get(i).getEPolicies().size(); j++) {
				policy ePolicy = this.getPolicies().get(i).getEPolicies().get(j);
				for (int k = 0; k < this.getPods().size(); k++) {
					//this.getPolicies().get(i).getEPolicies().get(j).setAllow(k);
					namespace NS = this.getNamespace(this.getPods().get(k).getNamespace());
					ePolicy.calculateAllow(k, this.getPods().get(k), NS);
				}
			}
			this.getPolicies().get(i).calculateBitVector(this.getPods().size());
		}

		for (int i = 0; i < this.getPods().size(); i++) {
			for (int j = 0; j < this.getPolicies().size(); j++) {
				if (this.getPolicies().get(j).selectPod(this.getPods().get(i))) {
					this.getPolicies().get(j).setSelectedPods(j);
					if(this.getPolicies().get(j).isHaveIn()) {
						this.getPods().get(i).setAllowPodIn(this.getPolicies().get(j).getInAllow());
					}
					if(this.getPolicies().get(j).isHaveE()) {
						this.getPods().get(i).setAllowPodE(this.getPolicies().get(j).getEAllow());
					}
				}
			}
		}

		/*for (int i = 0; i < this.Pods.size(); i++) {
			for (int j = 0; j < this.Pods.size(); j++) {
				pod podfrom = this.Pods.get(i);
				pod podto = this.Pods.get(j);
				System.out.println("from: " + podfrom.getNamespace() + "." + podfrom.getName());
				System.out.println("to: " + podto.getNamespace() + "." + podto.getName());
				System.out.println(podfrom.checkAllowE(j) && podto.checkAllowIn(i));
			}
		}*/
	}
	
	public void addPod(String fileName) {
		podYaml podyaml = new podYaml(fileName);
		this.getPodYamlList().add(podyaml);
		this.addPod(podyaml.getPod());
	}
	
	public void addPod(pod p) {
		this.Pods.add(p);
		int index = this.getPods().size()-1;
		// Init the new pods' allowE and allowIn bitset
		this.getPods().get(index).setall(this.getPods().size());
		
		// set pod Label hash
		for(String label : p.getLabels().keySet()) {
			if(this.podLabelHash.keySet().contains(label)) {
				this.podLabelHash.get(label).set(index);
			}else {
				BitSet newBitSet = new BitSet();
				newBitSet.set(index);
				this.podLabelHash.put(label, newBitSet);
			}
		}
		// calculate if selected by policies
		for(int i = 0; i < this.getPolicies().size(); i++) {
			if(!this.getPolicies().get(i).getNamespace().equals(p.getNamespace())) {
				continue;
			}
			this.getPolicies().get(i).setSelectedPods(index);
			for(String Key: this.getPolicies().get(i).getPods().keySet()) {
				if(!p.getLabels().containsKey(Key)) {
					this.getPolicies().get(i).clearSelectedPods(index);
				}
				if(p.getLabel(Key).equals(this.getPolicies().get(i).getFromPods(Key))) {
					//If label matched
				}else {
					//If label not match
					this.getPolicies().get(i).clearSelectedPods(index);
					break;
				}
			}
			if(this.getPolicies().get(i).getSelectedPods().get(index)) {
				this.getPods().get(index).getSelectedIndex().add(i);
				p.getAllowPodE().or(this.getPolicies().get(i).getEAllow());
				p.getAllowPodIn().or(this.getPolicies().get(i).getInAllow());
			}
		}
		// calculate if allowed by policies
		for(int i = 0; i < this.getPolicies().size(); i++) {
			// In
			for(int j = 0; j < this.getPolicies().get(i).getInPolicies().size(); j++ ) {
				policy inPolicy = this.getPolicies().get(i).getInPolicies().get(j);
				namespace NS = this.getNamespace(p.getNamespace());
				inPolicy.calculateAllow(index, p, NS);
			}
			// E
			for(int j = 0; j < this.getPolicies().get(i).getEPolicies().size(); j++ ) {
				policy ePolicy = this.getPolicies().get(i).getEPolicies().get(j);
				namespace NS = this.getNamespace(p.getNamespace());
				ePolicy.calculateAllow(index, p, NS);
			}
			this.getPolicies().get(i).calculateBitVector(this.getPods().size());
			if(this.getPolicies().get(i).getInAllow().get(index)) {
				this.getPods().get(index).getAllowInIndex().add(i);
			}
			if(this.getPolicies().get(i).getEAllow().get(index)) {
				this.getPods().get(index).getAllowEIndex().add(i);
			}
		}
		// set the corresponding bit in existing pods
		for(int i = 0; i < this.getPods().size()-1; i++) {
			if(!this.getPods().get(i).isSelectedE()) {
				this.getPods().get(i).getAllowPodE().set(index);
			}
			if(!this.getPods().get(i).isSelectedIn()) {
				this.getPods().get(i).getAllowPodIn().set(index);
			}
			ArrayList<Integer> selectedIndex = this.getPods().get(i).getSelectedIndex();
			for(int policyIndex : selectedIndex) {
				if(this.getPolicies().get(policyIndex).getEAllow().get(index)) {
					this.getPods().get(i).getAllowPodE().set(index);
				}
				if(this.getPolicies().get(policyIndex).getInAllow().get(index)) {
					this.getPods().get(i).getAllowPodIn().set(index);
				}
			}
		}

		
		// set allowIn and allowE of the added pod
		ArrayList<Integer> selectedIndex = this.getPods().get(index).getSelectedIndex();
		for(int policyIndex : selectedIndex) {
			policies tmpPolicy = this.getPolicies().get(policyIndex);
			if(!this.getPods().get(index).isSelectedIn() && tmpPolicy.isHaveIn()) {
				this.getPods().get(index).setSelectedIn(true);
				this.getPods().get(index).getAllowPodIn().clear();
				this.getPods().get(index).getAllowPodIn().or(tmpPolicy.getInAllow());
			}else if(tmpPolicy.isHaveIn()) {
				this.getPods().get(index).getAllowPodIn().or(tmpPolicy.getInAllow());
			}
			if(!this.getPods().get(index).isSelectedE() && tmpPolicy.isHaveE()) {
				this.getPods().get(index).setSelectedE(true);
				this.getPods().get(index).getAllowPodE().clear();
				this.getPods().get(index).getAllowPodE().or(tmpPolicy.getEAllow());
			}else if(tmpPolicy.isHaveIn()) {
				this.getPods().get(index).getAllowPodE().or(tmpPolicy.getEAllow());
			}
		}
			
		//set bit in podlabelHS
		for(String key : podLabelHash.keySet()) {
			if(p.getLabels().keySet().contains(key)) {
				podLabelHash.get(key).set(index);
			}
		}
	}
	
	public void addPolicy(String fileName) {
		policyYaml policyyaml = new policyYaml(fileName);
		this.getPolicyYamlList().add(policyyaml);
		this.addPolicy(policyyaml.getPolicies());
	}
	
	public void addPolicy(policies p) {
		int i = this.Policies.size();
		this.Policies.add(p);		
		BitSet selectedPodsBit = new BitSet(this.getPods().size());
		BitSet allowPodsInBit = new BitSet(this.getPods().size());
		BitSet allowPodsEBit = new BitSet(this.getPods().size());
		BitSet tempBit;
		BitSet tempPolicyBit;
		BitSet tempNsBit;
		//Select selected pods by namespace and podlabel
		if(nsNameHash.get(p.getNamespace())!=null) {
			selectedPodsBit.or(nsNameHash.get(p.getNamespace()));
		}
		for(String Key: p.getPods().keySet()) {
			if(podLabelHash.get(Key)!=null) {
				selectedPodsBit.and(podLabelHash.get(Key));
			}else {
				selectedPodsBit.clear();
			}
		}
		for(int j = 0;;) {
			j = selectedPodsBit.nextSetBit(j);
			if(j==-1) {
				break;
			}
			//get selected pods
			for(String Key: p.getPods().keySet()) {
				if(this.getPods().get(j).getLabel(Key).equals(p.getFromPods(Key))) {
					//If label matched
				}else {
					//If label not match
					selectedPodsBit.clear(j);
					break;
				}
			}
			j++;
		}
		//Set DDP2P map
		this.getPolicies().get(i).getSelectedPods().or(selectedPodsBit);
		for(int j = 0;;) {
			j = selectedPodsBit.nextSetBit(j);
			if(j==-1) {
				break;
			}
			this.getPods().get(j).getSelectedIndex().add(i);
			j++;
		}
		//Select allow pods by nsLabel and podLabel
		//In policy
		for(policy tempPolicy: p.getInPolicies()) {
			tempNsBit = new BitSet(nsNameList.size());
			tempNsBit.flip(0,nsNameList.size());
			tempPolicyBit = new BitSet(this.getPods().size());
			//tempPolicyBit.set(0, this.getPods().size());
			for(filter tempFilter: tempPolicy.getFilters()) {
				if(tempFilter.isHaveNsSelector()) {
					//Get all possible selected NS
					for(String Key: tempFilter.getNsSelector().keySet()) {
						if(nsLabelHash.get(Key)!=null) {
							tempNsBit.and(nsLabelHash.get(Key));
						}else {
							tempNsBit.clear();
						}
					}
					//Get all selected NS
					for(int j = 0;;) {
						j = tempNsBit.nextSetBit(j);
						if(j==-1) {
							break;
						}
						//get allowed pods
						//use nslabels
						for(String Key: tempFilter.getNsSelector().keySet()) {
							if(this.getNamespace(nsNameList.get(j)).getLabel(Key).equals(tempFilter.getNsSelector(Key))) {
								//If label matched
							}else {
								//If label not match
								tempNsBit.clear(j);
								break;
							}
						}
						j++;
					}
					//Cast NS to pods
					for(int j = 0;;) {
						j = tempNsBit.nextSetBit(j);
						if(j == -1) {
							break;
						}
						tempPolicyBit.or(nsNameHash.get(nsNameList.get(j)));
						j++;
					}
				}else {
					//If no nsSelector, use name to filter
					tempPolicyBit.or(nsNameHash.get(p.getNamespace()));
				}
				if(tempFilter.isHavePodSelector()) {
					for(String Key: tempFilter.getPodSelector().keySet()) {
						if(podLabelHash.get(Key)!=null) {
							tempPolicyBit.and(podLabelHash.get(Key));
						}else {
							tempPolicyBit.clear();
						}
					}
				}
				for(int j = 0;;) {
					j = tempPolicyBit.nextSetBit(j);
					if(j==-1) {
						break;
					}
					//get allowed pods
					//use labels
					if(tempFilter.isHavePodSelector()) {
						for(String Key: tempFilter.getPodSelector().keySet()) {
							if(this.getPods().get(j).getLabel(Key).equals(tempFilter.getPodSelector(Key))) {
								//If label matched
							}else {
								//If label not match
								tempPolicyBit.clear(j);
								break;
							}
						}
					}
					j++;
				}
			}
			
			allowPodsInBit.or(tempPolicyBit);
		}
			
		//Set DDP2P Map
		this.getPolicies().get(i).getInAllow().or(allowPodsInBit);
		for(int j = 0;;) {
			j = allowPodsInBit.nextSetBit(j);
			if(j==-1) {
				break;
			}
			this.getPods().get(j).getAllowInIndex().add(i);
			j++;
		}
			
		//E policy
		for(policy tempPolicy: p.getEPolicies()) {
			tempNsBit = new BitSet(nsNameList.size());
			tempNsBit.flip(0,nsNameList.size());
			tempPolicyBit = new BitSet(this.getPods().size());
			//tempPolicyBit.set(0, this.getPods().size());
			for(filter tempFilter: tempPolicy.getFilters()) {
				if(tempFilter.isHaveNsSelector()) {
					//Get all possible selected NS
					for(String Key: tempFilter.getNsSelector().keySet()) {
						if(nsLabelHash.get(Key)!=null) {
							tempNsBit.and(nsLabelHash.get(Key));
						}else {
							tempNsBit.clear();
						}
					}
					//Get all selected NS
					for(int j = 0;;) {
						j = tempNsBit.nextSetBit(j);
						if(j==-1) {
							break;
						}
						//get allowed pods
						//use nslabels
						for(String Key: tempFilter.getNsSelector().keySet()) {
							if(this.getNamespace(nsNameList.get(j)).getLabel(Key).equals(tempFilter.getNsSelector(Key))) {
								//If label matched
							}else {
								//If label not match
								tempNsBit.clear(j);
								break;
							}
						}
						j++;
					}
					//Cast NS to pods
					for(int j = 0;;) {
						j = tempNsBit.nextSetBit(j);
						if(j == -1) {
							break;
						}
						tempPolicyBit.or(nsNameHash.get(nsNameList.get(j)));
						j++;
					}
				}else {
					//If no nsSelector, use name to filter
					tempPolicyBit.or(nsNameHash.get(this.getPolicies().get(i).getNamespace()));
				}
				if(tempFilter.isHavePodSelector()) {
					for(String Key: tempFilter.getPodSelector().keySet()) {
						if(podLabelHash.get(Key)!=null) {
							tempPolicyBit.and(podLabelHash.get(Key));
						}else {
							tempPolicyBit.clear();
						}
					}
				}
				for(int j = 0;;) {
					j = tempPolicyBit.nextSetBit(j);
					if(j==-1) {
						break;
					}
					//get allowed pods
					//use labels
					if(tempFilter.isHavePodSelector()) {
						for(String Key: tempFilter.getPodSelector().keySet()) {
							if(this.getPods().get(j).getLabel(Key).equals(tempFilter.getPodSelector(Key))) {
								//If label matched
							}else {
								//If label not match
								tempPolicyBit.clear(j);
								break;
							}
						}
					}
					j++;
				}
			}
			allowPodsEBit.or(tempPolicyBit);
		}
		//Set DDP2P Map
		this.getPolicies().get(i).getEAllow().or(allowPodsEBit);
		for(int j = 0;;) {
			j = allowPodsEBit.nextSetBit(j);
			if(j==-1) {
				break;
			}
			this.getPods().get(j).getAllowEIndex().add(i);
			j++;
		}
			
		//Selected: selectedPodsBit
		//AllowedIn: allowPodsInBit
		//AllowedE: allowPodsEBit
		for(int j = 0;;) {
			j = selectedPodsBit.nextSetBit(j);
			if(j == -1) {
				break;
			}
			if(this.getPolicies().get(i).isHaveIn()) {
				this.Pods.get(j).setAllowPodIn(allowPodsInBit);
			}
			if(this.getPolicies().get(i).isHaveE()) {
				this.Pods.get(j).setAllowPodE(allowPodsEBit);
			}
			j++;
		}
	}
	
	public void removePod(int index) {
		this.Pods.remove(index);
		for(int i = 0; i < this.getPods().size(); i++) {
			this.getPods().get(i).removePod(index);
		}
		for(int i = 0; i < this.getPolicies().size(); i++) {
			this.getPolicies().get(i).removePod(index);
		}
		for(String key : podLabelHash.keySet()) {
			bitsetUtil.removeBit(podLabelHash.get(key), index);
		}
	}
	
	public void removePolicy(int index) {
		this.removePolicy(index, false);
	}
	
	public ArrayList<allowLink> removePolicy(int index, boolean needsReturn) {
		// Return remove allowLink for fix. If not fix, it can be ignored
		ArrayList<allowLink> removedLinks = new ArrayList<allowLink>();
		
		// when remove policy, find the pods affected by this policy
		// and use BCP map to find other policies affecting these pods
		policies rmPolicy = this.getPolicies().get(index);
		BitSet rmSelected = rmPolicy.getSelectedPods();
		BitSet rmInAllow = rmPolicy.getInAllow();
		BitSet rmEAllow = rmPolicy.getEAllow();
		
		for(int i = 0;;) {
			i = rmSelected.nextSetBit(i);
			if(i==-1) {
				break;
			}
			pod selectedPod = this.getPods().get(i);
			// recalculate in allow bitset
			BitSet InAllow = new BitSet(this.getPods().size());
			BitSet EAllow = new BitSet(this.getPods().size());
			boolean inSelected = false;
			boolean eSelected = false;
			for(int selectIndex : selectedPod.getSelectedIndex()){
				if(selectIndex == index) {
					continue;
				}
				if(this.getPolicies().get(selectIndex).isHaveIn()) {
					inSelected = true;
					InAllow.or(this.getPolicies().get(selectIndex).getInAllow());
				}
				if(this.getPolicies().get(selectIndex).isHaveE()) {
					eSelected = true;
					EAllow.or(this.getPolicies().get(selectIndex).getEAllow());
				}
			}
			if(inSelected == true) {
				if(needsReturn) {
					BitSet tmpBitSet = new BitSet(InAllow.length());
					tmpBitSet.or(InAllow);
					tmpBitSet.flip(0, InAllow.length());
					tmpBitSet.and(this.getPods().get(i).getAllowPodIn());
					for(int tmp_i = 0;;) {
						tmp_i = tmpBitSet.nextSetBit(i);
						if(tmp_i == -1) {
							break;
						}
						removedLinks.add(new allowLink(tmp_i, i));
						tmp_i++;
					}
				}
				this.getPods().get(i).andAllowPodIn(InAllow);
			}else {
				if(needsReturn) {
					assert false;
				}
				InAllow.set(0, this.getPods().size());
				this.getPods().get(i).setAllowPodIn(InAllow);
				this.getPods().get(i).setSelectedIn(false);
			}
			if(eSelected == true) {
				if(needsReturn) {
					BitSet tmpBitSet = new BitSet(EAllow.length());
					tmpBitSet.or(EAllow);
					tmpBitSet.flip(0, EAllow.length());
					tmpBitSet.and(this.getPods().get(i).getAllowPodE());
					for(int tmp_i = 0;;) {
						tmp_i = tmpBitSet.nextSetBit(i);
						if(tmp_i == -1) {
							break;
						}
						removedLinks.add(new allowLink(i, tmp_i));
						tmp_i++;
					}
				}
				this.getPods().get(i).andAllowPodE(EAllow);
			}else {
				if(needsReturn) {
					assert false;
				}
				EAllow.set(0, this.getPods().size());
				this.getPods().get(i).setAllowPodE(EAllow);
				this.getPods().get(i).setSelectedE(false);
			}
			i++;
		}
		// remove the corresponding bit in pod selector and allow
		for(int i = 0; i < this.getPods().size(); i++) {
			int rmIndex = -1;
			for(int j = 0; j < this.getPods().get(i).getSelectedIndex().size(); j++) {
				if(this.getPods().get(i).getSelectedIndex().get(j) < index) {
					continue;
				}else if(this.getPods().get(i).getSelectedIndex().get(j) == index){
					rmIndex = j;
				}else {
					this.getPods().get(i).getSelectedIndex().set(j, this.getPods().get(i).getSelectedIndex().get(j)-1);
				}
			}
			if(rmIndex != -1) {
				this.getPods().get(i).getSelectedIndex().remove(rmIndex);
				rmIndex = -1;
			}
			
			for(int j = 0; j < this.getPods().get(i).getAllowInIndex().size(); j++) {
				if(this.getPods().get(i).getAllowInIndex().get(j) < index) {
					continue;
				}else if(this.getPods().get(i).getAllowInIndex().get(j) == index){
					rmIndex = j;
				}else {
					this.getPods().get(i).getAllowInIndex().set(j, this.getPods().get(i).getAllowInIndex().get(j)-1);
				}
			}
			if(rmIndex != -1) {
				this.getPods().get(i).getAllowInIndex().remove(rmIndex);
				rmIndex = -1;
			}
			
			for(int j = 0; j < this.getPods().get(i).getAllowEIndex().size(); j++) {
				if(this.getPods().get(i).getAllowEIndex().get(j) < index) {
					continue;
				}else if(this.getPods().get(i).getAllowEIndex().get(j) == index){
					rmIndex = j;
				}else {
					this.getPods().get(i).getAllowEIndex().set(j, this.getPods().get(i).getAllowEIndex().get(j)-1);
				}
			}
			if(rmIndex != -1) {
				this.getPods().get(i).getAllowEIndex().remove(rmIndex);
			}
		}
		// TODO refreash AllowPodE and AllowPodIn, selectedE and selectedIn
		// remove policies in global variables
		this.PolicyYamlList.remove(index);
		this.Policies.remove(index);
		return removedLinks;
	}
	
	public void setConsistency() {
		//set the connectivity now as intent
		for(int i = 0; i < this.Pods.size(); i++) {
			this.Pods.get(i).setIntentE(this.Pods.get(i).getAllowPodE());
			this.Pods.get(i).setIntentIn(this.Pods.get(i).getAllowPodIn());
			this.Pods.get(i).setAllowPodE(new BitSet());
			this.Pods.get(i).setAllowPodIn(new BitSet());
		}
	}
	
	public void setIntent(serviceChain intent) {
		for(int i = 0; i < intent.getLinks().size(); i++) {
			allowLink templink = intent.getLink(i);
			this.Pods.get(templink.getSrcIndex()).getIntentE().set(templink.getDstIndex());
			this.Pods.get(templink.getDstIndex()).getIntentE().set(templink.getSrcIndex());
		}
	}
	
	public bitMatrix getInMatrix() {
		return InMatrix;
	}

	public void setInMatrix(bitMatrix inMatrix) {
		InMatrix = inMatrix;
	}

	public bitMatrix getEMatrix() {
		return EMatrix;
	}

	public void setEMatrix(bitMatrix eMatrix) {
		EMatrix = eMatrix;
	}

	public bitMatrix getReachabilityMatrix() {
		return ReachabilityMatrix;
	}

	public void setReachabilityMatrix(bitMatrix reachabilityMatrix) {
		ReachabilityMatrix = reachabilityMatrix;
	}

	public bitMatrix getIntentMatrix() {
		return IntentMatrix;
	}

	public void setIntentMatrix(bitMatrix intentMatrix) {
		IntentMatrix = intentMatrix;
	}

	public void calculateAllowMatrixs() {
		InMatrix = new bitMatrix(this.Pods.size());
		EMatrix = new bitMatrix(this.Pods.size());
		ReachabilityMatrix = new bitMatrix(this.Pods.size());
		for(int i = 0; i < this.Pods.size(); i++) {
			InMatrix.setRow(i, this.Pods.get(i).getAllowPodIn());
			EMatrix.setColumn(i, this.Pods.get(i).getAllowPodE());
		}
		ReachabilityMatrix.or(InMatrix);
		ReachabilityMatrix.and(EMatrix);
	}
	
	public void calculateIntentMatrixs() {
		bitMatrix InMatrix = new bitMatrix(this.Pods.size());
		bitMatrix EMatrix = new bitMatrix(this.Pods.size());
		IntentMatrix = new bitMatrix(this.Pods.size());
		for(int i = 0; i < this.Pods.size(); i++) {
			InMatrix.setRow(i, this.Pods.get(i).getAllowPodIn());
			EMatrix.setColumn(i, this.Pods.get(i).getAllowPodE());
		}
		IntentMatrix.or(InMatrix);
		IntentMatrix.and(EMatrix);
	}
	
	//Verifiers
	public void allReachableVerifier() {
		System.out.println("--------\nverfifying allReachable...");
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			BitSet temp = new BitSet();
			temp.set(0, this.getPods().size());
			temp.xor(this.getPods().get(i).getAllowPodIn());
			if(temp.isEmpty()) {
				System.out.println(this.getPods().get(i).getName() + " all reachable");
			}
		}
		long stoptime = System.nanoTime();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void allIsolatedVerifier() {
		System.out.println("--------\nverfifying allIsolated...");
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			if(this.getPods().get(i).getAllowPodIn().isEmpty()) {
				System.out.println(this.getPods().get(i).getName() + " all isolated");
			}
		}
		long stoptime = System.nanoTime();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void certainIsolatedVerifier() {
		System.out.println("--------\nverfifying certainIsolated...");
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			if(!this.getPods().get(i).getAllowPodIn().get(0)) {
				System.out.println(this.getPods().get(i).getName() + " isolated from system");
			}
		}
		long stoptime = System.nanoTime();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void userReachableVerifier() {
		System.out.println("--------\nverfifying userReachable...");
		// Hash pods by namespace, namespace labels and pod labels
		//HashMap<String, BitSet> nsNameHash = new HashMap<String, BitSet>();
		//for (int i = 0; i < this.getPods().size(); i++) {
		//	String nsName = this.getPods().get(i).getNamespace();
		//	if (nsNameHash.get(nsName) == null) {
		//		nsNameHash.put(nsName, new BitSet());
		//		nsNameHash.get(nsName).set(i);
		//	} else {
		//		nsNameHash.get(nsName).set(i);
		//	}
		//}
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			BitSet temp = new BitSet();
			temp.or(nsNameHash.get(this.getPods().get(i).getNamespace()));
			temp.flip(0, this.getPods().size());
			temp.and(this.getPods().get(i).getAllowPodIn());
			if(!temp.isEmpty()) {
				System.out.println(this.getPods().get(i).getName() + " can be reached by other user");
			}
		}
		long stoptime = System.nanoTime();
		//TODO System.out.println("tomcat can be reached by other user");		
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void policyCoverageVerifier() {
		System.out.println("--------\nverfifying policyCoverage...");
		// Hash pods by namespace, namespace labels and pod labels
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			for(int j = 0; j < this.getPods().get(i).getSelectedIndex().size();j++) {
				for(int k = 0; k < this.getPods().get(i).getSelectedIndex().size();k++) {
					if(k == j) {
						continue;
					}
					BitSet temp0 = new BitSet();
					BitSet temp1 = new BitSet();
					temp0.or(this.getPolicies().get(this.getPods().get(i).getSelectedIndex().get(j)).getInAllow());
					temp1.or(this.getPolicies().get(this.getPods().get(i).getSelectedIndex().get(k)).getInAllow());
					temp0.and(temp1);
					temp0.xor(temp1);
					if(temp0.isEmpty()) {
						System.out.print("Policy" + j + " covers Policy" + k + ", delete Policy" + k + "?");
					}
				}
			}
		}
		long stoptime = System.nanoTime();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void policyConflictVerifier() {
		System.out.println("--------\nverfifying policyConflict...");
		// Hash pods by namespace, namespace labels and pod labels
		long starttime = System.nanoTime();
		for(int i = 0; i< this.getPods().size();i++) {
			for(int j = 0; j < this.getPods().get(i).getSelectedIndex().size();j++) {
				for(int k = 0; k < this.getPods().get(i).getSelectedIndex().size();k++) {
					if(k == j) {
						continue;
					}
					BitSet temp0 = new BitSet();
					BitSet temp1 = new BitSet();
					temp0.or(this.getPolicies().get(this.getPods().get(i).getSelectedIndex().get(j)).getInAllow());
					temp1.or(this.getPolicies().get(this.getPods().get(i).getSelectedIndex().get(k)).getInAllow());
					temp1.flip(0, this.getPods().size());
					temp0.and(temp1);
					temp0.xor(temp1);
					if(temp0.isEmpty()) {
						System.out.print("Policy" + j + " conflicts with Policy" + k);
					}
				}
			}
		}
		long stoptime = System.nanoTime();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.print((stoptime - starttime)+"\t");
	}
	
	public void init(String filePath) {
		File file = new File(filePath);
		File[] fileList = file.listFiles();
		for(int i = 0; i < fileList.length; i++) {
			if(fileList[i].getName().endsWith("yaml")) {
				Yaml yaml = new Yaml();
				File f = new File(filePath + fileList[i].getName());
				LinkedHashMap content = null;
				try {
					content = (LinkedHashMap)yaml.load(new FileInputStream(f));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				String kind = (String)content.get("kind");
				if(kind.equals("NetworkPolicy")) {
					policyYaml policyyaml = new policyYaml(filePath + fileList[i].getName());
					this.getPolicyYamlList().add(policyyaml);
				}else if(kind.equals("Deployment")) {
					podYaml podyaml = new podYaml(filePath + fileList[i].getName());
					this.getPodYamlList().add(podyaml);
				}else if(kind.equals("Namespace")) {
					nsYaml nsyaml = new nsYaml(filePath + fileList[i].getName());
					this.getNSYamlList().add(nsyaml);
				}else {
					assert(false);
				}
			}
		}
		this.yaml2Policies();
		this.yaml2Pods();
		this.yaml2NS();
		System.out.print("");
	}
	
	public void tempInit() {
		// initiate policy
		for (int i = 0; i < 50; i++) {
			policyYaml policyyaml = new policyYaml("examples/test100_5_50/testpolicy" + i + ".yaml");
			this.getPolicyYamlList().add(policyyaml);
		}

		// initiate pod
		for (int i = 0; i < 100; i++) {
			podYaml podyaml = new podYaml("examples/test100_5_50/testpod" + i + ".yaml");
			this.getPodYamlList().add(podyaml);
		}

		// initiate NS
		for (int i = 0; i < 5; i++) {
			nsYaml nsyaml = new nsYaml("examples/test100_5_50/testns" + i + ".yaml");
			this.getNSYamlList().add(nsyaml);
		}

		this.yaml2Policies();
		this.yaml2Pods();
		this.yaml2NS();
		long starttime = System.nanoTime();
		this.naiveVerify();
		// bvg.prefilterVerify();
		long stoptime = System.nanoTime();
		// bvg.calculateAllowMatrixs();

		System.out.println(stoptime - starttime);
	}
	
	public void tempInit2(int podNum, int nsNum, int policyNum) {
		// initiate policy
		for (int i = 0; i < policyNum; i++) {
			policyYaml policyyaml = new policyYaml("examples/test"+podNum+"_"+nsNum+"_"+policyNum+"/testpolicy" + i + ".yaml");
			this.getPolicyYamlList().add(policyyaml);
		}

		// initiate pod
		for (int i = 0; i < policyNum; i++) {
			podYaml podyaml = new podYaml("examples/test"+podNum+"_"+nsNum+"_"+policyNum+"/testpod" + i + ".yaml");
			this.getPodYamlList().add(podyaml);
		}

		// initiate NS
		for (int i = 0; i < nsNum; i++) {
			nsYaml nsyaml = new nsYaml("examples/test"+podNum+"_"+nsNum+"_"+policyNum+"/testns" + i + ".yaml");
			this.getNSYamlList().add(nsyaml);
		}

		this.yaml2Policies();
		this.yaml2Pods();
		this.yaml2NS();
		//System.out.println("Generating reachability matrix...");
		//long starttime = System.nanoTime();
		//bvg.naiveVerify();
		//this.prefilterVerify();
		this.naiveVerify();
		//long stoptime = System.nanoTime();
		// bvg.calculateAllowMatrixs();
		//System.out.println("Cost time: " + (stoptime - starttime));
		//System.out.println((stoptime-starttime));
		for(int i = 0; i< 110000; i++) {
			this.allReachableVerifier();
			this.allIsolatedVerifier();
			this.certainIsolatedVerifier();
			this.userReachableVerifier();
			this.policyCoverageVerifier();
			this.policyConflictVerifier();
			Runtime run = Runtime.getRuntime();
			System.out.println(run.totalMemory()-run.freeMemory());
		}
	}
	
	public static void main(String args[]) {
		String podInput = "";
		String nsInput = "";
		String policyInput = "";
		boolean allVerify = false;
		if(args.length == 0) {
			args = new String[]{"-h"};
		}
		for(int i = 0; i < args.length; i++) {
			if(args[i] == "-h") {
				System.out.println("Help info");
				System.out.println("==============================================");
				System.out.println("Usage:");
				System.out.println("-h                : print this info");
				System.out.println("-po/--pod         : set pod config input");
				System.out.println("-ns/--namespace   : set namespace config input");
				System.out.println("-pl/--policy      : set policy config input");
				System.out.println("-all              : all verify");
				System.out.println("Example:");
				System.out.println("java Kano.jar -po po.yaml -ns ns.yaml -pl pl.yaml -all");
			}else if(args[i] == "--pod" || args[i] == "-po") {
				//read pod file
				if(i == args.length - 1) {
					assert false;
				}
				podInput = args[i+1];
				i++;
			}else if(args[i] == "--namespace" || args[i] == "-ns") {
				//read ns file
				if(i == args.length - 1) {
					assert false;
				}
				nsInput = args[i+1];
				i++;
			}else if(args[i] == "--policy" || args[i] == "-pl") {
				//read policy file
				if(i == args.length - 1) {
					assert false;
				}
				policyInput = args[i+1];
				i++;
			}else if(args[i] == "-all") {
				allVerify = true;
			}
		}
		/*
		if(args.length!=3){
			return;
		}else{	
			int podNum = Integer.parseInt(args[0]);
			int nsNum = Integer.parseInt(args[1]);
			int policyNum = Integer.parseInt(args[2]);
			// test main function
			//BVgenerator bv1 = new BVgenerator();
		
			BVgenerator bv2 = new BVgenerator();
			//bv1.tempInit();
			//bv1.calculateAllowMatrixs();
			bv2.tempInit2(podNum,nsNum,policyNum);
			bv2.calculateAllowMatrixs();
			//bv1.ReachabilityMatrix.getData().xor(bv2.ReachabilityMatrix.getData());
			//System.out.print(bv1.ReachabilityMatrix.getData());
			bv2 = null;
			System.gc();
		}
		*/
	}
}