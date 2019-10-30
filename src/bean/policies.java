package bean;

import java.util.ArrayList;
import java.util.BitSet;
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
	BitSet inAllow;
	BitSet eAllow;
	
	public policies() {
		this.haveIn = false;
		this.haveE = false;
		this.allPods = false;
		this.name = "";
		this.namespace = "";
		this.pods = new HashMap<String,String>();
		this.inPolicies = new ArrayList<policy>();
		this.ePolicies = new ArrayList<policy>();
		this.inAllow = new BitSet();
		this.eAllow = new BitSet();
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
		this.inAllow = new BitSet();
		this.eAllow = new BitSet();
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

	public BitSet getInAllow() {
		return inAllow;
	}

	public void setInAllow(BitSet inAllow) {
		this.inAllow = inAllow;
	}

	public BitSet geteAllow() {
		return eAllow;
	}

	public void seteAllow(BitSet eAllow) {
		this.eAllow = eAllow;
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

	public ArrayList<KVPair> getAllowNSList(){
		ArrayList<KVPair> result = new ArrayList<KVPair>();
		for(int i = 0; i < inPolicies.size(); i++) {
			for(int j = 0; j < inPolicies.get(i).getFilters().size(); j++) {
				for(String key:inPolicies.get(i).getFromFilters(j).getNsSelector().keySet()) {
					result.add(new KVPair(key,inPolicies.get(i).getFromFilters(j).getNsSelector().get(key)));
				}
			}
		}
		for(int i = 0; i < ePolicies.size(); i++) {
			for(int j = 0; j < ePolicies.get(i).getFilters().size(); j++) {
				for(String key:ePolicies.get(i).getFromFilters(j).getNsSelector().keySet()) {
					result.add(new KVPair(key,ePolicies.get(i).getFromFilters(j).getNsSelector().get(key)));
				}
			}
		}
		return result;
	}
	
	public ArrayList<KVPair> getAllowPodList(){
		ArrayList<KVPair> result = new ArrayList<KVPair>();
		for(int i = 0; i < inPolicies.size(); i++) {
			for(int j = 0; j < inPolicies.get(i).getFilters().size(); j++) {
				for(String key:inPolicies.get(i).getFromFilters(j).getPodSelector().keySet()) {
					result.add(new KVPair(key,inPolicies.get(i).getFromFilters(j).getPodSelector().get(key)));
				}
			}
		}
		for(int i = 0; i < ePolicies.size(); i++) {
			for(int j = 0; j < ePolicies.get(i).getFilters().size(); j++) {
				for(String key:ePolicies.get(i).getFromFilters(j).getPodSelector().keySet()) {
					result.add(new KVPair(key,ePolicies.get(i).getFromFilters(j).getPodSelector().get(key)));
				}
			}
		}
		return result;
	}
	
	public ArrayList<String> getAllowIPList(){
		ArrayList<String> result = new ArrayList<String>();
		//TODO design a representation of ipBlock
		return result;
	}
	
	public ArrayList<KVPair> getSelectorNSList(){
		ArrayList<KVPair> result = new ArrayList<KVPair>();
		result.add(new KVPair("name",namespace));
		return result;
	}
	
	public ArrayList<KVPair> getSelectorPodList(){
		ArrayList<KVPair> result = new ArrayList<KVPair>();
		for(String key: pods.keySet()) {
			result.add(new KVPair(key,pods.get(key)));
		}
		return result;
	}
	
	public boolean selectPod(pod Pod) {
		boolean result = false;
		if(Pod.getNamespace().equals(namespace)) {
			for(String Key: pods.keySet()) {
				if(!pods.get(Key).equals(Pod.getLabels().get(Key))) {
					return result;
				}
			}
			result = true;
		}
		return result;
	}
	
	public void calculateBitVector(int pods) {
		if(this.haveIn) {
			for(int i = 0; i < inPolicies.size(); i++) {
				inAllow.or(inPolicies.get(i).getAllow());
			}
		}else {
			inAllow.set(0, pods);
		}
		
		if(this.haveE) {
			for(int i = 0; i < ePolicies.size(); i++) {
				eAllow.or(ePolicies.get(i).getAllow());
			}
		}else {
			eAllow.set(0, pods);
		}
	}
}