package analyzer;

import java.util.ArrayList;

import bean.KVPair;
import bean.myYaml;
import bean.policies;

//KV: Key-Value pair
//BV: BitVector
public class BVgenerator{
	ArrayList<myYaml> YamlList; 
	ArrayList<policies> Policies;
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
		YamlList = new ArrayList<myYaml>();
		Policies = new ArrayList<policies>();
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
	
	public BVgenerator(ArrayList<myYaml> YamlList) {
		this.YamlList = YamlList;
		Policies = new ArrayList<policies>();
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
	
	public ArrayList<myYaml> getYamlList() {
		return YamlList;
	}

	public void setYamlList(ArrayList<myYaml> yamlList) {
		YamlList = yamlList;
	}
	
	public void addYaml(myYaml myyaml) {
		this.YamlList.add(myyaml);
	}
	
	public myYaml getYaml(int i) {
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
			myYaml test = YamlList.get(i);
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
	
	public static void main(String args[]) {
		BVgenerator bvg = new BVgenerator();
		myYaml myyaml = new myYaml("test1.yaml");
		bvg.addYaml(myyaml);
		bvg.yaml2Policies();
		bvg.CalculateAllowBV();
		bvg.CalculateSelectorBV();
		System.out.print(bvg);
	}
}