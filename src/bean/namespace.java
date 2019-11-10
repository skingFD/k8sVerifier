package bean;

import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class namespace{
	String name;
	HashMap<String,String> labels;
	BitSet allowNS;
	
	public namespace() {
		name = "";
		labels = new HashMap<String,String>();
		allowNS = new BitSet();
	}
	
	public namespace(String name) {
		this.name = name;
		labels = new HashMap<String,String>();
		allowNS = new BitSet();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String,String> getLabels() {
		return labels;
	}
	public void setLabels(HashMap<String,String> labels) {
		this.labels = labels;
	}
	public BitSet getAllowNS() {
		return allowNS;
	}
	public void setAllowNS(BitSet allowNS) {
		this.allowNS = allowNS;
	}
	public void addLabel(String Key, String Value) {
		labels.put(Key, Value);
	}
	public String getLabel(String Key) {
		return labels.get(Key);
	}
	
	/**
	 * generate Yaml from namespace
	 * @return generated Yaml
	 */
	public nsYaml generateYaml() {
		LinkedHashMap result = new LinkedHashMap();
		//apiVersion and kind
		result.put("apiVersion", "networking.k8s.io/v1");
		result.put("kind", "NetworkPolicy");
		//metadata
		LinkedHashMap metadata = new LinkedHashMap();
		metadata.put("name", name);
		
		LinkedHashMap labelsMap = new LinkedHashMap();
		for(String key: labels.keySet()) {
			labelsMap.put(key, labels.get(key));
		}
		metadata.put("labels", labelsMap);
		result.put("metadata", metadata);
		
		return new nsYaml(result);
	}
	
	public static void main(String args[]) {
		nsYaml testYaml = new nsYaml("testns1.yaml");
		namespace testNs = testYaml.getNS();
		nsYaml testAfter = testNs.generateYaml();
		System.out.println(testAfter);
	}
}