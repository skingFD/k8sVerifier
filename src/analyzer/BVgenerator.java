package analyzer;

import java.util.ArrayList;
import java.util.BitSet;

import bean.KVPair;
import bean.allowLink;
import bean.bitMatrix;
import bean.serviceChain;
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
	
	public void verifyReachability() {
		// naive verification
		for (int i = 0; i < this.getPods().size(); i++) {
			this.getPods().get(i).setall(this.getPods().size());
		}
		for (int i = 0; i < this.getPolicies().size(); i++) {
			for (int j = 0; j < this.getPolicies().get(i).getInPolicies().size(); j++) {
				policy inPolicy = this.getPolicies().get(i).getInPolicies().get(j);
				for (int k = 0; k < this.getPods().size(); k++) {
					namespace NS = this.getNamespace(this.getPods().get(k).getName());
					inPolicy.calculateAllow(k, this.getPods().get(k), NS);
				}
			}
			for (int j = 0; j < this.getPolicies().get(i).getePolicies().size(); j++) {
				policy ePolicy = this.getPolicies().get(i).getePolicies().get(j);
				for (int k = 0; k < this.getPods().size(); k++) {
					namespace NS = this.getNamespace(this.getPods().get(k).getName());
					ePolicy.calculateAllow(k, this.getPods().get(k), NS);
				}
			}
			this.getPolicies().get(i).calculateBitVector(this.getPods().size());
		}

		for (int i = 0; i < this.getPods().size(); i++) {
			boolean first = true;
			for (int j = 0; j < this.getPolicies().size(); j++) {
				if (this.getPolicies().get(j).selectPod(this.getPods().get(i))) {
					if (first) {
						this.getPods().get(i).clearall(this.getPods().size());
						first = false;
					}
					this.getPods().get(i).orAllowPodIn(this.getPolicies().get(j).getInAllow());
					this.getPods().get(i).orAllowPodE(this.getPolicies().get(j).geteAllow());
				}
			}
		}

		for (int i = 0; i < this.Pods.size(); i++) {
			for (int j = 0; j < this.Pods.size(); j++) {
				pod podfrom = this.Pods.get(i);
				pod podto = this.Pods.get(j);
				System.out.println("from: " + podfrom.getNamespace() + "." + podfrom.getName());
				System.out.println("to: " + podto.getNamespace() + "." + podto.getName());
				System.out.println(podfrom.checkAllowE(j) && podto.checkAllowIn(i));
			}
		}
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
		bvg.verifyReachability();
		bvg.calculateAllowMatrixs();
		
		System.out.print(bvg);
	}
}