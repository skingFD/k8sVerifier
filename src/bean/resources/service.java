package bean.resources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import bean.yaml.serviceYaml;

public class service{
	String name;
	String namespace;
	String type;
	boolean clusterIP;
	HashMap<String,String> labels;
	ArrayList<portCast> ports;
	HashMap<String,String> selector;
	
	public service() {
		name = "";
		namespace = "default";
		type = "ClusterIP";
		clusterIP = false;
		labels = new HashMap<String, String>();
		ports = new ArrayList<portCast>();
		selector = new HashMap<String, String>();
	}
	
	public service(String name, String namespace) {
		this.name = name;
		this.namespace = namespace;
		type = "ClusterIP";
		clusterIP = false;
		labels = new HashMap<String, String>();
		ports = new ArrayList<portCast>();
		selector = new HashMap<String, String>();
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isClusterIP() {
		return clusterIP;
	}

	public void setClusterIP(boolean clusterIP) {
		this.clusterIP = clusterIP;
	}

	public HashMap<String, String> getLabels() {
		return labels;
	}

	public void setLabels(HashMap<String, String> labels) {
		this.labels = labels;
	}

	public ArrayList<portCast> getPorts() {
		return ports;
	}

	public void setPorts(ArrayList<portCast> ports) {
		this.ports = ports;
	}

	public HashMap<String, String> getSelector() {
		return selector;
	}

	public void setSelector(HashMap<String, String> selector) {
		this.selector = selector;
	}
	
	public void putToLabels(String key, String value) {
		this.labels.put(key, value);
	}
	
	public String getFromLabels(String key) {
		return this.labels.get(key);
	}
	
	public void putToSelector(String key, String value) {
		this.selector.put(key, value);
	}
	
	public String getFromSelector(String key) {
		return this.selector.get(key);
	}
	
	public void addToPorts(portCast portcast) {
		this.ports.add(portcast);
	}
	
	public portCast getFromPorts(int i) {
		return this.ports.get(i);
	}
	
	public serviceYaml generateYaml() {
		LinkedHashMap result = new LinkedHashMap();
		result.put("apiVersion", "v1");
		result.put("kind", "Service");
		LinkedHashMap metadata = new LinkedHashMap();
		metadata.put("name", this.name);
		metadata.put("namespace", this.namespace);
		LinkedHashMap labels = new LinkedHashMap();
		for(String key:this.labels.keySet()) {
			labels.put(key, this.labels.get(key));
		}
		metadata.put("labels", labels);
		result.put("metadata", metadata);
		LinkedHashMap spec = new LinkedHashMap();
		spec.put("type", this.type);
		ArrayList ports = new ArrayList();
		for(portCast temp: this.ports) {
			LinkedHashMap port = new LinkedHashMap();
			if(temp.servicePort != -1) {
				port.put("port", temp.servicePort);
			}
			if(temp.podPort != -1) {
				port.put("targetPort", temp.podPort);
			}
			if(temp.nodePort != -1) {
				port.put("nodePort", temp.nodePort);
			}
			ports.add(port);
		}
		spec.put("ports", ports);
		LinkedHashMap selector = new LinkedHashMap();
		for(String key:this.selector.keySet()) {
			selector.put(key, this.selector.get(key));
		}
		spec.put("selector", selector);
		result.put("spec", spec);
		return new serviceYaml(result);
	}
}