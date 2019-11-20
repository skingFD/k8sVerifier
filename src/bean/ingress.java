package bean;

import java.util.ArrayList;

public class ingress{
	String name;
	String namespace;
	ArrayList<ingressRule> ingressRules;
	
	public ingress() {
		name = "";
		namespace = "default";
		ingressRules = new ArrayList<ingressRule>();
	}
	
	public ingress(String name, String namespace) {
		this.name = name;
		this.namespace = namespace;
		ingressRules = new ArrayList<ingressRule>();
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

	public ArrayList<ingressRule> getIngressRules() {
		return ingressRules;
	}

	public void setIngressRules(ArrayList<ingressRule> ingressRules) {
		this.ingressRules = ingressRules;
	}
	
	public void addToRules(ingressRule ingressrule) {
		ingressRules.add(ingressrule);
	}
	
	public ingressRule getRule(int i) {
		return ingressRules.get(i);
	}
}