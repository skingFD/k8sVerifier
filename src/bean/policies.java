package bean;

import java.util.ArrayList;
import java.util.HashMap;

public class policies{
	boolean haveIn;
	boolean haveE;
	String name;
	String namespace;
	HashMap<String,String> pods;
	ArrayList<policy> Policies;
	
	public policies() {

		Policies = new ArrayList<policy>();
	}
	
	public policies(boolean haveIn, boolean haveE, String name, String namespace, HashMap<String,String> pods, ArrayList<policy> Policies) {
		this.haveIn = haveIn;
		this.haveE = haveE;
		this.name = name;
		this.namespace = namespace;
		this.pods = pods;
		this.Policies = Policies;
	}
	
	public boolean isHaveIn() {
		return haveIn;
	}

	public void setHaveIn(boolean haveIn) {
		this.haveIn = haveIn;
	}

	public boolean isHaveE() {
		return haveE;
	}

	public void setHaveE(boolean haveE) {
		this.haveE = haveE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public HashMap<String, String> getPods() {
		return pods;
	}

	public void setPods(HashMap<String, String> pods) {
		this.pods = pods;
	}

	public ArrayList<policy> getPolicies() {
		return Policies;
	}

	public void setPolicies(ArrayList<policy> policies) {
		Policies = policies;
	}
	
	public void add(policy Policy) {
		Policies.add(Policy);
	}
	
	public policy get(int i) {
		return Policies.get(i);
	}

	public ArrayList<String> getAllowList(){
		ArrayList<String> result = new ArrayList<String>();
		
		return result;
	}
	
	public ArrayList<String> getSelectorList(){	
		ArrayList<String> result = new ArrayList<String>();
		return result;
	}
}