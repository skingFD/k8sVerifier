package bean.resources;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

import bean.KVPair;
import bean.yaml.policyYaml;
import utils.bitsetUtil;

/**
 * @author skingFD
 * policies for BV generator, contains inPolicies, ePolicies
 */
public class policies{
	boolean haveIn;
	boolean haveE;
	boolean allPods;
	String name;
	String namespace;
	HashMap<String,String> pods; //Selector
	ArrayList<policy> inPolicies;
	ArrayList<policy> ePolicies;
	BitSet SelectedPods;
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
		this.SelectedPods = new BitSet();
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
		this.SelectedPods = new BitSet();
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

	public ArrayList<policy> getEPolicies() {
		return ePolicies;
	}

	public void setEPolicies(ArrayList<policy> ePolicies) {
		this.ePolicies = ePolicies;
	}

	public BitSet getInAllow() {
		return inAllow;
	}

	public void setInAllow(BitSet inAllow) {
		this.inAllow = inAllow;
	}

	public BitSet getEAllow() {
		return eAllow;
	}

	public void setEAllow(BitSet eAllow) {
		this.eAllow = eAllow;
	}

	public BitSet getSelectedPods() {
		return SelectedPods;
	}

	public void setSelectedPods(BitSet selectedPods) {
		SelectedPods = selectedPods;
	}
	
	public void setSelectedPods(int index) {
		SelectedPods.set(index);
	}
	
	public void clearSelectedPods(int index) {
		SelectedPods.clear(index);
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
	
	public void removePod(int index) {
		bitsetUtil.removeBit(this.SelectedPods, index);
		bitsetUtil.removeBit(this.eAllow, index);
		bitsetUtil.removeBit(this.inAllow, index);
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
	
	/**
	 * generate Yaml from policies
	 * @return generated Yaml
	 */
	public policyYaml generateYaml() {
		LinkedHashMap result = new LinkedHashMap();
		//apiVersion and kind
		result.put("apiVersion", "networking.k8s.io/v1");
		result.put("kind", "NetworkPolicy");
		//metadata
		LinkedHashMap metadata = new LinkedHashMap();
		metadata.put("name", name);
		metadata.put("namespace", namespace);
		
		
		//spec
		LinkedHashMap spec = new LinkedHashMap();
		//spec.podSelector
		LinkedHashMap podSelector = new LinkedHashMap();
		//spec.podSelector.matchLabels
		LinkedHashMap selectorLabels = new LinkedHashMap();
		for(String key: pods.keySet()) {
			selectorLabels.put(key, pods.get(key));
		}
		podSelector.put("matchLabels", selectorLabels);
		//spec.policyTypes
		ArrayList policyTypes = new ArrayList();
		if(haveIn) {
			policyTypes.add("Ingress");
		}
		if(haveE) {
			policyTypes.add("Egress");
		}
		//spec.ingress
		ArrayList ingress = new ArrayList();
		for(int i = 0; i< inPolicies.size(); i++) {
			LinkedHashMap ingressItem = new LinkedHashMap();
			ArrayList from = new ArrayList();
			ArrayList ports = new ArrayList();
			for(int j = 0; j< inPolicies.get(i).getFilters().size(); j++) {
				LinkedHashMap fromItem = new LinkedHashMap();
				if(inPolicies.get(i).getFilters().get(j).isHaveNsSelector()) { // If have namespaceSelector
					LinkedHashMap NSAllow = new LinkedHashMap(); 
					LinkedHashMap Labels = new LinkedHashMap();
					for(String key: inPolicies.get(i).getFilters().get(j).getNsSelector().keySet()) {
						Labels.put(key, inPolicies.get(i).getFilters().get(j).getNsSelector().get(key));
					}
					NSAllow.put("matchLabels", Labels);
					fromItem.put("namespaceSelector", NSAllow);
				}
				if(inPolicies.get(i).getFilters().get(j).isHavePodSelector()) { // If have podSelector
					LinkedHashMap PodAllow = new LinkedHashMap();
					LinkedHashMap Labels = new LinkedHashMap();
					for(String key: inPolicies.get(i).getFilters().get(j).getPodSelector().keySet()) {
						Labels.put(key, inPolicies.get(i).getFilters().get(j).getPodSelector().get(key));
					}
					PodAllow.put("matchLabels", Labels);
					fromItem.put("podSelector", PodAllow);
				}
				if(inPolicies.get(i).getFilters().get(j).isHaveCidr()) { // If have cidr
					LinkedHashMap ipBlock = new LinkedHashMap();
					ipBlock.put("cidr", inPolicies.get(i).getFilters().get(j).getCidr());
					ArrayList excepts = new ArrayList();
					for(String except: inPolicies.get(i).getFilters().get(j).getExcept()) {
						excepts.add(except);
					}
					ipBlock.put("except", excepts);
					fromItem.put("ipBlock", ipBlock);
				}
				from.add(fromItem);
			}
			for(int j = 0; j<inPolicies.get(i).getPorts().size(); j++) {
				LinkedHashMap portItem = new LinkedHashMap();
				portItem.put("protocol", inPolicies.get(i).getPorts().get(j).getProtocol());
				portItem.put("port", inPolicies.get(i).getPorts().get(j).getPort());
				ports.add(portItem);
			}
			ingressItem.put("from", from);
			ingressItem.put("ports", ports);
			ingress.add(ingressItem);
		}
		
		//spec.egress
		ArrayList egress = new ArrayList();
		for(int i = 0; i < ePolicies.size(); i++) {
			LinkedHashMap egressItem = new LinkedHashMap();
			ArrayList to = new ArrayList();
			ArrayList ports = new ArrayList();
			for(int j = 0; j< ePolicies.get(i).getFilters().size(); j++) {
				LinkedHashMap toItem = new LinkedHashMap();
				if(ePolicies.get(i).getFilters().get(j).isHaveNsSelector()) { // If have namespaceSelector
					LinkedHashMap NSAllow = new LinkedHashMap(); 
					LinkedHashMap Labels = new LinkedHashMap();
					for(String key: ePolicies.get(i).getFilters().get(j).getNsSelector().keySet()) {
						Labels.put(key, ePolicies.get(i).getFilters().get(j).getNsSelector().get(key));
					}
					NSAllow.put("matchLabels", Labels);
					toItem.put("namespaceSelector", NSAllow);
				}
				if(ePolicies.get(i).getFilters().get(j).isHavePodSelector()) { // If have podSelector
					LinkedHashMap PodAllow = new LinkedHashMap();
					LinkedHashMap Labels = new LinkedHashMap();
					for(String key: ePolicies.get(i).getFilters().get(j).getPodSelector().keySet()) {
						Labels.put(key, ePolicies.get(i).getFilters().get(j).getPodSelector().get(key));
					}
					PodAllow.put("matchLabels", Labels);
					toItem.put("podSelector", PodAllow);
				}
				if(ePolicies.get(i).getFilters().get(j).isHaveCidr()) { // If have cidr
					LinkedHashMap ipBlock = new LinkedHashMap();
					ipBlock.put("cidr", ePolicies.get(i).getFilters().get(j).getCidr());
					ArrayList excepts = new ArrayList();
					for(String except: ePolicies.get(i).getFilters().get(j).getExcept()) {
						excepts.add(except);
					}
					ipBlock.put("except", excepts);
					toItem.put("ipBlock", ipBlock);
				}
				to.add(toItem);
			}
			for(int j = 0; j<ePolicies.get(i).getPorts().size(); j++) {
				LinkedHashMap portItem = new LinkedHashMap();
				portItem.put("protocol", ePolicies.get(i).getPorts().get(j).getProtocol());
				portItem.put("port", ePolicies.get(i).getPorts().get(j).getPort());
				ports.add(portItem);
			}
			egressItem.put("to", to);
			egressItem.put("ports", ports);
			egress.add(egressItem);
		}
		
		// package spec
		spec.put("podSelector", podSelector);
		spec.put("policyTypes", policyTypes);
		spec.put("ingress", ingress);
		spec.put("egress", egress);
		
		// package result
		result.put("metadata", metadata);
		result.put("spec", spec);
		
		return new policyYaml(result);
	}

	public static void main(String args[]) {
		//for test
		policyYaml inputYaml = new policyYaml("test.yaml");
		policies test = inputYaml.getPolicies();
		policyYaml outputYaml = test.generateYaml();
		String yamlString = outputYaml.getYamlDump();
		System.out.println(yamlString);
	}
}