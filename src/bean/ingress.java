package bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import bean.yaml.ingressYaml;
import bean.yaml.nsYaml;

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
	
	public ingressYaml generateYaml() {
		LinkedHashMap result = new LinkedHashMap();
		//apiVersion and kind
		result.put("apiVersion", "extensions/v1beta1");
		result.put("kind", "Ingress");
		//metadata
		LinkedHashMap metadata = new LinkedHashMap(); // metadata 
		metadata.put("name", name);
		metadata.put("namespace", namespace);
		
		LinkedHashMap spec = new LinkedHashMap(); // spec
		ArrayList rules = new ArrayList(); // spec.rules
		for(ingressRule ingressrule: ingressRules) {
			LinkedHashMap rule = new LinkedHashMap(); // spec.rules[i]
			rule.put("host", ingressrule.host);
			if(ingressrule.getHttpPaths().size()!=0) {
				LinkedHashMap http = new LinkedHashMap(); // spec.rules.http
				ArrayList paths = new ArrayList(); // spec.rules.http.paths
				for(ingressPath ingresspath: ingressrule.getHttpPaths()) {
					LinkedHashMap path = new LinkedHashMap(); // spec.rules.http.paths.path
					path.put("path", ingresspath.getPath());
					LinkedHashMap backend = new LinkedHashMap();
					backend.put("serviceName",ingresspath.getServiceName());
					backend.put("servicePort",ingresspath.getServicePort());
					path.put("backend", backend);
					paths.add(path);
				}
				http.put("paths", paths);
				rule.put("http", http);
			}
			if(ingressrule.getHttpsPaths().size()!=0) {
				LinkedHashMap https = new LinkedHashMap(); // spec.rules.http
				ArrayList paths = new ArrayList(); // spec.rules.http.paths
				for(ingressPath ingresspath: ingressrule.getHttpsPaths()) {
					LinkedHashMap path = new LinkedHashMap(); // spec.rules.http.paths.path
					path.put("path", ingresspath.getPath());
					LinkedHashMap backend = new LinkedHashMap();
					backend.put("serviceName",ingresspath.getServiceName());
					backend.put("servicePort",ingresspath.getServicePort());
					path.put("backend", backend);
					paths.add(path);
				}
				https.put("paths", paths);
				rule.put("https", https);
			}
			rules.add(rule);
		}
		
		spec.put("rules", rules);
		result.put("spec", spec);
		result.put("metadata", metadata);
		
		return new ingressYaml(result);
	}
}