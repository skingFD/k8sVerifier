package bean.template;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import bean.allowLink;

public class template{
	String name;
	LinkedHashMap roles;
	
	public template() {
		name = "";
	}
	
	public template(LinkedHashMap config) {
		name = (String)config.get("name");
		roles = (LinkedHashMap)config.get("roles");
	}
	
	public ArrayList<allowLink> analyzeTemplate(){
		ArrayList<allowLink> result = new ArrayList<allowLink>();
		switch(name){
		case "shop":
			int LB = (int)roles.get("LB");
			int Web = (int)roles.get("Web");
			int DB = (int)roles.get("DB");
			result.add(new allowLink(LB,Web));
			result.add(new allowLink(Web,DB));
			break;
		}
		return result;
	}
}