package bean;

import java.util.HashMap;
import java.util.Map;

public class pod{
	String namespace;
	String name;
	String IP;
	Map labels;
	
	public pod() {
		namespace = "default";
		name = "";
		IP = "0.0.0.0";
		labels = new HashMap<String,String>();
	}
	
	public pod(String namespace, String name, String IP, Map labels) {
		this.namespace = namespace;
		this.name = name;
		this.IP = IP;
		this.labels = labels;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIP() {
		return IP;
	}

	public void setIP(String iP) {
		IP = iP;
	}

	public Map getLabels() {
		return labels;
	}

	public void setLabels(Map labels) {
		this.labels = labels;
	}
	
}