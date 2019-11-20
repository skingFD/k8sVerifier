package analyzer;

import java.util.ArrayList;
import java.util.BitSet;

import bean.KVPair;
import bean.namespace;
import bean.pod;
import bean.policies;
import bean.policy;
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
	ArrayList<namespace> Namespaces;
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
	
	public BVgenerator() {
		PolicyYamlList = new ArrayList<policyYaml>();
		Policies = new ArrayList<policies>();
		PodYamlList = new ArrayList<podYaml>();
		Pods = new ArrayList<pod>();
		NSYamlList  =new ArrayList<nsYaml>();
		Namespaces = new ArrayList<namespace>();
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

	public ArrayList<namespace> getNamespaces() {
		return Namespaces;
	}

	public void setNamespaces(ArrayList<namespace> namespaces) {
		Namespaces = namespaces;
	}
	
	public namespace getNamespace(String name) {
		for(int i = 0; i < Namespaces.size(); i++) {
			if(Namespaces.get(i).getName().equals(name)) {
				return Namespaces.get(i);
			}
		}
		return null;
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
			Namespaces.add(temp.getNS());
		}
	}

	public void CalculateSelectorBV() {
		for(int i = 0; i < Policies.size(); i++) {
			ArrayList<KVPair> NSList = Policies.get(i).getSelectorNSList();
			ArrayList<KVPair> PodList = Policies.get(i).getSelectorPodList();
			for(int j = 0; j < NSList.size(); j++) {
				if (!SelectorNSList.contains(NSList.get(j))){
					SelectorNSList.add(NSList.get(j));
				}
			}
			for(int j = 0; j < PodList.size(); j++) {
				if (!SelectorPodList.contains(PodList.get(j))) {
					SelectorPodList.add(PodList.get(j));
				}
			}
		}
		SelectorNSLength = SelectorNSList.size();
		SelectorPodLength = SelectorPodList.size();
	}
	
	public void CalculateAllowBV() {
		for(int i = 0; i < Policies.size(); i++) {
			ArrayList<KVPair> NSList = Policies.get(i).getAllowNSList();
			ArrayList<KVPair> PodList = Policies.get(i).getAllowPodList();
			for(int j = 0; j< NSList.size();j++) {
				if (!AllowNSList.contains(NSList.get(j))){
					AllowNSList.add(NSList.get(j));
				}
			}
			for(int j = 0; j < PodList.size(); j++) {
				if (!AllowPodList.contains(PodList.get(j))) {
					AllowPodList.add(PodList.get(j));
				}
			}
		}
		AllowNSLength = AllowNSList.size();
		AllowPodLength = AllowPodList.size();
	}
	
	public void CalculatePods0() {
//		for(int i = 0; i < Namespaces.size(); i++) {
//			namespace NS = Namespaces.get(i);
//			//calculate AllowNS of NS
//			BitSet AllowNS = new BitSet(AllowNSLength);
//			for(int j = 0; j < NS.getLabels().size(); j++) {
//				if(AllowNSList.contains(NS.getLabel(j))) {
//					AllowNS.set(AllowNSList.indexOf(NS.getLabel(j)));
//				}
//			}
//			Namespaces.get(i).setAllowNS(AllowNS);
//		}
//		for(int i = 0; i < Pods.size(); i++) {
//			pod Pod = Pods.get(i);
//			
//			//calculate SelectorNS
//			BitSet SelectorNS = new BitSet(SelectorNSLength);
//			if(SelectorNSList.contains(new KVPair("name",Pod.getNamespace()))) {
//				SelectorNS.set(SelectorNSList.indexOf(new KVPair("name",Pod.getNamespace())));
//			}
//			Pods.get(i).setSelectorNS(SelectorNS);
//			
//			//calculate SelectorPod
//			BitSet SelectorPod = new BitSet(SelectorPodLength);
//			for(int j = 0; j < Pod.getLabels().size(); j++) {
//				if(SelectorPodList.contains(Pod.getLabel(j))) {
//					SelectorPod.set(SelectorPodList.indexOf(Pod.getLabel(j)));
//				}
//			}
//			Pods.get(i).setSelectorPod(SelectorPod);
//			
//			//calculate AllowNS
//			for(int j = 0; j < Namespaces.size(); j++) {
//				if(Namespaces.get(j).getName().equals(Pod.getName())) {
//					Pods.get(i).setAllowNS(Namespaces.get(j).getAllowNS());
//					break;
//				}
//			}
//			
//			//calculate AllowPod
//			BitSet AllowPod = new BitSet(AllowPodLength);
//			for(int j = 0; j < Pod.getLabels().size(); j++) {
//				if(AllowPodList.contains(Pod.getLabel(j))) {
//					AllowPod.set(AllowPodList.indexOf(Pod.getLabel(j)));
//				}
//			}
//			Pods.get(i).setSelectorPod(AllowPod);
//			
//			//TODO calculate IP
//		}
	}
	
	public void CalculatePods() {
		for(policies tempPolicy: Policies) {
			
		}
	}
	
	public static void main(String args[]) {		
		// test main function
		// initiate BVgenerator
		BVgenerator bvg = new BVgenerator();
		
		// initiate policy
		policyYaml policyyaml = new policyYaml("testpolicy.yaml");
		bvg.getPolicyYamlList().add(policyyaml);
		
		// initiate pod
		podYaml podyaml1 = new podYaml("testdep1.yaml");
		podYaml podyaml2 = new podYaml("testdep2.yaml");
		bvg.getPodYamlList().add(podyaml1);
		bvg.getPodYamlList().add(podyaml2);
		
		// initiate NS
		nsYaml nsyaml1 = new nsYaml("testns1.yaml");
		nsYaml nsyaml2 = new nsYaml("testns2.yaml");
		bvg.getNSYamlList().add(nsyaml1);
		bvg.getNSYamlList().add(nsyaml2);
		
		bvg.yaml2Policies();
		bvg.yaml2Pods();
		bvg.yaml2NS();
		//bvg.CalculateAllowBV();
		//bvg.CalculateSelectorBV();
		
		//naive verification
		for(int i = 0; i < bvg.getPods().size(); i++) {
			bvg.getPods().get(i).setall(bvg.getPods().size());
		}
		for(int i = 0; i < bvg.getPolicies().size(); i++) {
			for(int j = 0; j < bvg.getPolicies().get(i).getInPolicies().size(); j++) {
				policy inPolicy = bvg.getPolicies().get(i).getInPolicies().get(j);
				for(int k = 0; k < bvg.getPods().size(); k++) {
					namespace NS = bvg.getNamespace(bvg.getPods().get(k).getName());
					inPolicy.calculateAllow(k, bvg.getPods().get(k), NS);
				}
			}
			for(int j = 0; j < bvg.getPolicies().get(i).getePolicies().size(); j++) {
				policy ePolicy = bvg.getPolicies().get(i).getePolicies().get(j);
				for(int k = 0; k < bvg.getPods().size(); k++) {
					namespace NS = bvg.getNamespace(bvg.getPods().get(k).getName());
					ePolicy.calculateAllow(k, bvg.getPods().get(k), NS);
				}
			}
			bvg.getPolicies().get(i).calculateBitVector(bvg.getPods().size());
		}
		
		for(int i = 0; i < bvg.getPods().size(); i++) {
			boolean first = true;
			for(int j = 0; j < bvg.getPolicies().size(); j++) {
				if(bvg.getPolicies().get(j).selectPod(bvg.getPods().get(i))) {
					if(first) {
						bvg.getPods().get(i).clearall(bvg.getPods().size());
						first = false;
					}
					bvg.getPods().get(i).orAllowPodIn(bvg.getPolicies().get(j).getInAllow());
					bvg.getPods().get(i).orAllowPodE(bvg.getPolicies().get(j).geteAllow());
				}
			}
		}
		
		for(int i = 0; i < bvg.Pods.size(); i++) {
			for(int j = 0; j < bvg.Pods.size(); j++) {
				pod podfrom = bvg.Pods.get(i);
				pod podto = bvg.Pods.get(j);
				System.out.println("from: "+ podfrom.getNamespace() + "." + podfrom.getName());
				System.out.println("to: "+ podto.getNamespace() + "." + podto.getName());
				System.out.println(podfrom.checkAllowE(j)&&podto.checkAllowIn(i));
			}
		}
		System.out.print(bvg);
	}
}