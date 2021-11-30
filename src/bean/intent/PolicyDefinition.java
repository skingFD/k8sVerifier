package bean.intent;

import java.util.HashMap;

//Policy(a:a1,b:b1,c:c1;a:a2,b:b2,c:c2) policy_a
public class PolicyDefinition {
	String name;
	HashMap<String, String> selectLabels;
	HashMap<String, String> allowLabels;
	public PolicyDefinition(String name, HashMap<String, String> selectLabels, HashMap<String, String> allowLabels){
		this.name = name;
		this.selectLabels = selectLabels;
		this.allowLabels = allowLabels;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, String> getSelectLabels() {
		return selectLabels;
	}
	public void setSelectLabels(HashMap<String, String> selectLabels) {
		this.selectLabels = selectLabels;
	}
	public HashMap<String, String> getAllowLabels() {
		return allowLabels;
	}
	public void setAllowLabels(HashMap<String, String> allowLabels) {
		this.allowLabels = allowLabels;
	}
	
}