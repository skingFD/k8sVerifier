package analyzer;

import java.util.ArrayList;

import bean.myYaml;
import bean.policies;

//KV: Key-Value pair
//BV: BitVector
public class BVgenerator{
	ArrayList<myYaml> YamlList; 
	ArrayList<String> SelectorKV;//store the corresponding relationship between KV pair and BitVector
	ArrayList<String> AllowKV;
	int SelectorBVLength;
	int AllowBVLength;
	
	public BVgenerator() {
		YamlList = new ArrayList<myYaml>();
		SelectorKV = new ArrayList<String>();
		AllowKV = new ArrayList<String>();
		SelectorBVLength = 0;
		AllowBVLength = 0;
	}
	
	public BVgenerator(ArrayList<myYaml> YamlList) {
		this.YamlList = YamlList;		
		SelectorKV = new ArrayList<String>();
		AllowKV = new ArrayList<String>();
		SelectorBVLength = 0;
		AllowBVLength = 0;
	}
	
	public ArrayList<myYaml> getYamlList() {
		return YamlList;
	}

	public void setYamlList(ArrayList<myYaml> yamlList) {
		YamlList = yamlList;
	}

	public ArrayList<String> getSelectorKV() {
		return SelectorKV;
	}

	public void setSelectorKV(ArrayList<String> selectorKV) {
		SelectorKV = selectorKV;
	}

	public ArrayList<String> getAllowKV() {
		return AllowKV;
	}

	public void setAllowKV(ArrayList<String> allowKV) {
		AllowKV = allowKV;
	}

	public int getSelectorBVLength() {
		return SelectorBVLength;
	}

	public void setSelectorBVLength(int selectorBVLength) {
		SelectorBVLength = selectorBVLength;
	}

	public int getAllowBVLength() {
		return AllowBVLength;
	}

	public void setAllowBVLength(int allowBVLength) {
		AllowBVLength = allowBVLength;
	}

	public void CalculateSelectorBVlength() {
		for(int i = 0; i < YamlList.size(); i++) {
			ArrayList<String> SelectorList = YamlList.get(i).getSelectorList();
			for(int j = 0; j< SelectorList.size();j++) {
				if (!SelectorKV.contains(SelectorList.get(j))){
					SelectorKV.add(SelectorList.get(j));
				}
			}
		}
		SelectorBVLength = SelectorKV.size();
	}
	
	public void CalculateAllowBVlength() {
		for(int i = 0; i < YamlList.size(); i++) {
			policies Policies = YamlList.get(i).getPolicies();
			ArrayList<String> AllowList = Policies.getAllowList();
			for(int j = 0; j< AllowList.size();j++) {
				if (!AllowKV.contains(AllowList.get(j))){
					AllowKV.add(AllowList.get(j));
				}
			}
		}
		AllowBVLength = AllowKV.size();
	}
}