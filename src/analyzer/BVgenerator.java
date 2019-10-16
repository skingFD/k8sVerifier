package analyzer;

import java.util.ArrayList;

import bean.myYaml;

public class BVgenerator{
	ArrayList<myYaml> YamlList; 
	ArrayList<String> SelectorKV;//store the corresponding relationship between KV pair and BitVector
	ArrayList<String> AllowKV;
	int SelectorBVlength;
	int AllowBVlength;
	
	public BVgenerator() {
		YamlList = new ArrayList<myYaml>();
		SelectorKV = new ArrayList<String>();
		AllowKV = new ArrayList<String>();
		SelectorBVlength = 0;
		AllowBVlength = 0;
	}
	
	public BVgenerator(ArrayList<myYaml> YamlList) {
		this.YamlList = YamlList;		
		SelectorKV = new ArrayList<String>();
		AllowKV = new ArrayList<String>();
		SelectorBVlength = 0;
		AllowBVlength = 0;
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

	public int getSelectorBVlength() {
		return SelectorBVlength;
	}

	public void setSelectorBVlength(int selectorBVlength) {
		SelectorBVlength = selectorBVlength;
	}

	public int getAllowBVlength() {
		return AllowBVlength;
	}

	public void setAllowBVlength(int allowBVlength) {
		AllowBVlength = allowBVlength;
	}

	public void CalculateSelectorBVlength() {
		for(int i = 0; i < YamlList.size(); i++) {
			// TODO Yaml get KV pair
		}
	}
	
	public void CalculateAllowBVlength() {
		for(int i = 0; i < YamlList.size(); i++) {
			
		}
	}
}