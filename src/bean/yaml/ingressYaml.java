package bean.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import bean.resources.ingress;
import bean.resources.ingressPath;
import bean.resources.ingressRule;
import bean.resources.pod;

public class ingressYaml{
	public Yaml yaml;
	public LinkedHashMap content;
	public String yamlString;
	
	public ingressYaml() {
		this.yaml = new Yaml();
		this.content = new LinkedHashMap();
		this.yamlString = yaml.dump(content);
	}
	
	public ingressYaml(String FilePath) {
		this.yaml = new Yaml();
		File f = new File(FilePath);
		try {
			this.content = (LinkedHashMap)yaml.load(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.yamlString = yaml.dump(content);
	}
	
	public ingressYaml(LinkedHashMap content) {
		this.yaml = new Yaml();
		this.content = content;
		this.yamlString = yaml.dump(content);
	}
	
	public String getYamlDump() {
		this.yamlString = yaml.dump(content);
		return this.yamlString;
	}
	
	public ingress getIngress(){
		ingress result= new ingress();
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		if (metadata.get("name") != null) { // metadata.name
			result.setName((String) metadata.get("name"));
		}
		if (metadata.get("namespace") != null) { // metadata.namespace
			result.setNamespace((String) metadata.get("namespace"));
		} else {
			result.setNamespace("default");
		}
		LinkedHashMap spec = (LinkedHashMap) content.get("spec"); //spec
		if(spec.get("rules")!=null) { // spec.rules
			ArrayList rules = (ArrayList) spec.get("rules");
			for(Object rule: rules) {
				ingressRule ingressrule = new ingressRule();
				LinkedHashMap ruleMap = (LinkedHashMap)rule;
				String host = (String) ruleMap.get("host");
				ingressrule.setHost(host);
				if(ruleMap.get("http") != null) {
					LinkedHashMap http = (LinkedHashMap) ruleMap.get("http");
					ArrayList paths = (ArrayList) http.get("paths");
					for(Object path: paths) {
						LinkedHashMap pathMap = (LinkedHashMap)path;
						ingressPath ingresspath = new ingressPath();
						ingresspath.setPath((String)pathMap.get("path"));
						LinkedHashMap backend = (LinkedHashMap)pathMap.get("backend");
						ingresspath.setServiceName((String)backend.get("serviceName"));
						ingresspath.setServicePort((int)backend.get("servicePort"));
						ingressrule.addHttpPath(ingresspath);
					}
				}
				if(ruleMap.get("https") != null) {
					LinkedHashMap https = (LinkedHashMap) ruleMap.get("https");
					ArrayList paths = (ArrayList) https.get("paths");
					for(Object path: paths) {
						LinkedHashMap pathMap = (LinkedHashMap)path;
						ingressPath ingresspath = new ingressPath();
						ingresspath.setPath((String)pathMap.get("path"));
						LinkedHashMap backend = (LinkedHashMap)pathMap.get("backend");
						ingresspath.setServiceName((String)backend.get("serviceName"));
						ingresspath.setServicePort((int)backend.get("servicePort"));
						ingressrule.addHttpsPath(ingresspath);
					}
				}
				result.addToRules(ingressrule);
			}
		}
		return result;
	}
	
	public static void main(String args[]) {
		ingressYaml test = new ingressYaml("testingress.yaml");
		ingress testIngress = test.getIngress();
		ingressYaml testout = testIngress.generateYaml();
		System.out.println(testout);
	}
}