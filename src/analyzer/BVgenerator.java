package analyzer;

import java.util.ArrayList;
import java.util.BitSet;

import bean.KVPair;
import bean.namespace;
import bean.pod;
import bean.policyYaml;
import bean.policies;

//KV: Key-Value pair
//BV: BitVector
public class BVgenerator{
	ArrayList<policyYaml> YamlList; 
	ArrayList<policies> Policies;
	ArrayList<KVPair> SelectorNSList;//store the corresponding relationship between KV pair and BitVector
	ArrayList<KVPair> SelectorPodList;
	ArrayList<KVPair> AllowNSList;
	ArrayList<KVPair> AllowPodList;
	ArrayList<String> AllowIPList;
	ArrayList<pod> Pods;
	ArrayList<namespace> Namespaces;
	int SelectorNSLength;
	int SelectorPodLength;
	int AllowNSLength;
	int AllowPodLength;
	int AllowIPLength;
	
	public BVgenerator() {
		YamlList = new ArrayList<policyYaml>();
		Policies = new ArrayList<policies>();
		SelectorNSList = new ArrayList<KVPair>();
		SelectorPodList = new ArrayList<KVPair>();
		AllowNSList = new ArrayList<KVPair>();
		AllowPodList = new ArrayList<KVPair>();
		AllowIPList = new ArrayList<String>();
		Pods = new ArrayList<pod>();
		Namespaces = new ArrayList<namespace>();
		SelectorNSLength = 0;
		SelectorPodLength = 0;
		AllowNSLength = 0;
		AllowPodLength = 0;
		AllowIPLength = 0;
	}
	
	public BVgenerator(ArrayList<policyYaml> YamlList) {
		this.YamlList = YamlList;
		Policies = new ArrayList<policies>();
		SelectorNSList = new ArrayList<KVPair>();
		SelectorPodList = new ArrayList<KVPair>();
		AllowNSList = new ArrayList<KVPair>();
		AllowPodList = new ArrayList<KVPair>();
		AllowIPList = new ArrayList<String>();
		Pods = new ArrayList<pod>();
		Namespaces = new ArrayList<namespace>();
		SelectorNSLength = 0;
		SelectorPodLength = 0;
		AllowNSLength = 0;
		AllowPodLength = 0;
		AllowIPLength = 0;
	}
	
	public ArrayList<policyYaml> getYamlList() {
		return YamlList;
	}

	public void setYamlList(ArrayList<policyYaml> yamlList) {
		YamlList = yamlList;
	}
	
	public void addYaml(policyYaml myyaml) {
		this.YamlList.add(myyaml);
	}
	
	public policyYaml getYaml(int i) {
		return this.YamlList.get(i);
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
		for(int i = 0; i < YamlList.size(); i++) {
			policyYaml test = YamlList.get(i);
			Policies.add(test.getPolicies());
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
	
	public void CalculatePods() {
		for(int i = 0; i < Pods.size(); i++) {
			pod Pod = Pods.get(i);
			
			//calculate SelectorNS
			BitSet SelectorNS = new BitSet(SelectorNSLength);
			if(SelectorNSList.contains(new KVPair("name",Pod.getNamespace()))) {
				SelectorNS.set(SelectorNSList.indexOf(new KVPair("name",Pod.getNamespace())));
			}
			Pods.get(i).setSelectorNS(SelectorNS);
			
			//calculate SelectorPod
			BitSet SelectorPod = new BitSet(SelectorPodLength);
			for(int j = 0; j < Pod.getLabels().size(); j++) {
				if(SelectorPodList.contains(Pod.getLabel(j))) {
					SelectorPod.set(SelectorPodList.indexOf(Pod.getLabel(j)));
				}
			}
			Pods.get(i).setSelectorPod(SelectorPod);
			
			//TODO calculate AllowNS
			//calculate AllowPod
			BitSet AllowPod = new BitSet(AllowPodLength);
			for(int j = 0; j < Pod.getLabels().size(); j++) {
				if(AllowPodList.contains(Pod.getLabel(j))) {
					AllowPod.set(AllowPodList.indexOf(Pod.getLabel(j)));
				}
			}
			Pods.get(i).setSelectorPod(AllowPod);
			
			//TODO calculate IP
		}
	}
	
	public static void main(String args[]) {
		BVgenerator bvg = new BVgenerator();
		policyYaml myyaml = new policyYaml("test1.yaml");
		bvg.addYaml(myyaml);
		bvg.yaml2Policies();
		bvg.CalculateAllowBV();
		bvg.CalculateSelectorBV();
		System.out.print(bvg);
	}
}