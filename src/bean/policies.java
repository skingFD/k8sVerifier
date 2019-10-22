package bean;

import java.util.ArrayList;
import java.util.HashMap;

public class policies{
	boolean haveIn;
	boolean haveE;
	boolean allPods;
	String name;
	String namespace;
	HashMap<String,String> pods;
	ArrayList<policy> inPolicies;
	ArrayList<policy> ePolicies;
	
	public policies() {
		this.haveIn = false;
		this.haveE = false;
		this.allPods = false;
		this.name = "";
		this.namespace = "";
		this.pods = new HashMap<String,String>();
		this.inPolicies = new ArrayList<policy>();
		this.ePolicies = new ArrayList<policy>();
	}
	
	public policies(boolean haveIn, boolean haveE, boolean allPods, String name, String namespace, HashMap<String,String> pods, ArrayList<policy> inPolicies, ArrayList<policy> ePolicies) {
		this.haveIn = haveIn;
		this.haveE = haveE;
		this.allPods = allPods;
		this.name = name;
		this.namespace = namespace;
		this.pods = pods;
		this.inPolicies = inPolicies;
		this.ePolicies = ePolicies;
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
	
	public boolean isAllPods() {
		return allPods;
	}

	public void setAllPods(boolean allPods) {
		this.allPods = allPods;
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
	
	public ArrayList<policy> getInPolicies() {
		return inPolicies;
	}

	public void setInPolicies(ArrayList<policy> inPolicies) {
		this.inPolicies = inPolicies;
	}

	public ArrayList<policy> getePolicies() {
		return ePolicies;
	}

	public void setePolicies(ArrayList<policy> ePolicies) {
		this.ePolicies = ePolicies;
	}

	public void addToIn(policy Policy) {
		inPolicies.add(Policy);
	}
	
	public policy getFromIn(int i) {
		return inPolicies.get(i);
	}
	
	public void addToE(policy Policy) {
		ePolicies.add(Policy);
	}
	
	public policy getFromE(int i) {
		return ePolicies.get(i);
	}
	
	public void putToPods(String key, String value) {
		pods.put(key, value);
	}
	
	public String getFromPods(String key) {
		return pods.get(key);
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