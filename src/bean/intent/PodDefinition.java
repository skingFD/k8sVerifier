package bean.intent;

import java.util.HashMap;

//Pod(a:a,b:b,c:c) pod_a;
public class PodDefinition {
	String name;
	HashMap<String, String> arg;
	public PodDefinition(String name, HashMap<String, String> arg){
		this.name = name;
		this.arg = arg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String, String> getArg() {
		return arg;
	}
	public void setArg(HashMap<String, String> arg) {
		this.arg = arg;
	}
	
}