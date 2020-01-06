package analyzer;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

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
		HashMap<String,BitSet> podLabelHash = new HashMap<String,BitSet>();
		HashMap<String,BitSet> nsNameHash = new HashMap<String,BitSet>();
		HashMap<String,BitSet> nsLabelHash = new HashMap<String,BitSet>();
		ArrayList<String> nsNameList = new ArrayList<String>();
		
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
			//Select allow pods by nsLabel and podLabel
			//In policy
			for(policy tempPolicy: this.getPolicies().get(i).getInPolicies()) {
				tempNsBit = new BitSet(nsNameList.size());
				tempPolicyBit = new BitSet(this.getPods().size());
				tempPolicyBit.set(0, this.getPods().size());
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
			
			//E policy
			for(policy tempPolicy: this.getPolicies().get(i).getEPolicies()) {
				tempNsBit = new BitSet(nsNameList.size());
				tempPolicyBit = new BitSet(this.getPods().size());
				tempPolicyBit.set(0, this.getPods().size());
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
			
			//Selected: selectedPodsBit
			//AllowedIn: allowPodsBit
			//AllowedE: allowPodsBit
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
	
	public void tempInit() {
		// initiate policy
		for (int i = 0; i < 20000; i++) {
			policyYaml policyyaml = new policyYaml("examples/test/testpolicy" + i + ".yaml");
			this.getPolicyYamlList().add(policyyaml);
		}

		// initiate pod
		for (int i = 0; i < 50000; i++) {
			podYaml podyaml = new podYaml("examples/test/testpod" + i + ".yaml");
			this.getPodYamlList().add(podyaml);
		}

		// initiate NS
		for (int i = 0; i < 500; i++) {
			nsYaml nsyaml = new nsYaml("examples/test/testns" + i + ".yaml");
			this.getNSYamlList().add(nsyaml);
		}

		this.yaml2Policies();
		this.yaml2Pods();
		this.yaml2NS();
		long starttime = System.currentTimeMillis();
		this.naiveVerify();
		// bvg.prefilterVerify();
		long stoptime = System.currentTimeMillis();
		// bvg.calculateAllowMatrixs();

		System.out.println(stoptime - starttime);
	}
	
	public void tempInit2() {
		// initiate policy
		for (int i = 0; i < 20000; i++) {
			policyYaml policyyaml = new policyYaml("examples/test/testpolicy" + i + ".yaml");
			this.getPolicyYamlList().add(policyyaml);
		}

		// initiate pod
		for (int i = 0; i < 50000; i++) {
			podYaml podyaml = new podYaml("examples/test/testpod" + i + ".yaml");
			this.getPodYamlList().add(podyaml);
		}

		// initiate NS
		for (int i = 0; i < 500; i++) {
			nsYaml nsyaml = new nsYaml("examples/test/testns" + i + ".yaml");
			this.getNSYamlList().add(nsyaml);
		}

		this.yaml2Policies();
		this.yaml2Pods();
		this.yaml2NS();
		long starttime = System.currentTimeMillis();
		//bvg.naiveVerify();
		this.prefilterVerify();
		long stoptime = System.currentTimeMillis();
		// bvg.calculateAllowMatrixs();

		System.out.println(stoptime - starttime);
	}
	
	public static void main(String args[]) {	
		// test main function
		//BVgenerator bv1 = new BVgenerator();
		BVgenerator bv2 = new BVgenerator();
		//bv1.tempInit();
		//bv1.calculateAllowMatrixs();
		bv2.tempInit2();
		bv2.calculateAllowMatrixs();
		//bv1.ReachabilityMatrix.getData().xor(bv2.ReachabilityMatrix.getData());
		//System.out.print(bv1.ReachabilityMatrix.getData());
	}
}