package bean.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import bean.resources.portCast;
import bean.resources.service;

public class serviceYaml{
	public Yaml yaml;
	public LinkedHashMap content;
	public String yamlString;
	
	public serviceYaml() {
		this.yaml = new Yaml();
		this.content = new LinkedHashMap();
		this.yamlString = yaml.dump(content);
	}
	
	public serviceYaml(String FilePath) {
		this.yaml = new Yaml();
		File f = new File(FilePath);
		try {
			this.content = (LinkedHashMap)yaml.load(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.yamlString = yaml.dump(content);
	}
	
	public serviceYaml(LinkedHashMap content) {
		this.yaml = new Yaml();
		this.content = content;
		this.yamlString = yaml.dump(content);
	}
	
	public String getYamlDump() {
		this.yamlString = yaml.dump(content);
		return this.yamlString;
	}
	
	public service getService() {
		service result = new service();
		LinkedHashMap metadata = (LinkedHashMap)content.get("metadata");
		result.setName((String)metadata.get("name"));
		result.setNamespace((String)metadata.get("namespace"));
		if(metadata.get("labels")!=null) {
			LinkedHashMap labels = (LinkedHashMap)metadata.get("labels");
			for(Object label:labels.keySet()) {
				result.putToLabels((String)label, (String)labels.get(label));
			}
		}
		LinkedHashMap spec = (LinkedHashMap)content.get("spec");
		ArrayList ports = (ArrayList)spec.get("ports");
		for(Object port:ports) {
			LinkedHashMap portMap = (LinkedHashMap)port;
			portCast portcast = new portCast();
			if(portMap.get("port") != null) {//TODO which is podport and which is serviceport
				portcast.setServicePort((int)portMap.get("port"));
			}
			if(portMap.get("targetPort") != null) {
				portcast.setPodPort((int)portMap.get("targetPort"));
			}
			if(portMap.get("nodePort") != null) {
				portcast.setNodePort((int)portMap.get("nodePort"));
				portcast.setType(1);
			}
			if(portMap.get("name") != null) {
				portcast.setName((String)portMap.get("name"));
			}
			result.addToPorts(portcast);
		}
		LinkedHashMap selector = (LinkedHashMap)spec.get("selector");
		for(Object label:selector.keySet()) {
			result.putToSelector((String)label, (String)selector.get(label));
		}
		if(spec.get("type") != null) {
			result.setType((String)spec.get("type"));
		}else {
			result.setType("ClusterIP");
		}
		if(((String)spec.get("clusterIP")) == "None") {
			result.setClusterIP(false);
		}
		return result;
	}
	
	public static void main(String args[]) {
		serviceYaml testYaml = new serviceYaml("testsvc.yaml");
		service testsvc = testYaml.getService();
		serviceYaml testout = testsvc.generateYaml();
		System.out.println(testout);
	}
}