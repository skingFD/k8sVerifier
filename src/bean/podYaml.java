package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

public class podYaml{
	public Yaml yaml;
	public LinkedHashMap content;
	public String yamlString;
	
	public podYaml() {
		this.yaml = new Yaml();
		this.content = new LinkedHashMap();
		this.yamlString = yaml.dump(content);
	}
	
	public podYaml(String FilePath) {
		this.yaml = new Yaml();
		File f = new File(FilePath);
		try {
			this.content = (LinkedHashMap)yaml.load(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.yamlString = yaml.dump(content);
	}
	
	public String getYamlDump() {
		this.yamlString = yaml.dump(content);
		return this.yamlString;
	}
	
	public pod getPod() {
		pod result = new pod();
		// metadata
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		if (metadata.get("name") != null) { // metadata.name
			result.setName((String) metadata.get("name"));
		}
		if (metadata.get("namespace") != null) { // metadata.namespace
			result.setNamespace((String) metadata.get("namespace"));
		} else {
			result.setNamespace("default");
		}
		//spec
		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
		LinkedHashMap selector = null;
		LinkedHashMap matchLabels = null;
		LinkedHashMap template = null;
		LinkedHashMap tem_metadata = null;
		LinkedHashMap tem_labels = null;
		LinkedHashMap tem_spec = null;
		ArrayList<KVPair> pod_labels = new ArrayList<KVPair>();
		if(spec.get("selector")!=null) {
			selector = (LinkedHashMap) spec.get("selector");
			if(selector.get("matchLabels")!=null) {
				matchLabels = (LinkedHashMap) selector.get("matchLabels");
			}else {
				System.out.println("error: no matchLabels");
				return null;
				// TODO error: no matchLabels
			}
		}else {
			System.out.println("error: no selector");
			return null;
			//TODO error: no selector
		}
		if(spec.get("template")!=null) {
			template = (LinkedHashMap) spec.get("template");
			if(template.get("metadata")!=null) {
				tem_metadata = (LinkedHashMap) template.get("metadata");
				if(tem_metadata.get("labels")!=null) {
					tem_labels = (LinkedHashMap) tem_metadata.get("labels");
				}else {
					System.out.println("error: no labels");
					return null;
					// TODO error: no labels
				}
			}else {
				System.out.print("error: no metadata");
				return null;
				// TODO error: no metadata
			}
		}else {
			System.out.print("error: no template");
			return null;
			// TODO error: no template
		}
		if((matchLabels!=null)&&(tem_labels!=null)) {
			for(Object key: tem_labels.keySet()) {
				pod_labels.add(new KVPair((String)key,(String)tem_labels.get((String)key)));
			}
			for(Object key: matchLabels.keySet()) {
				KVPair tempPair = new KVPair((String)key,(String)tem_labels.get((String)key));
				if(!pod_labels.contains(tempPair)) {
					return null;
				}
			}
			for(KVPair kvpair: pod_labels) {
				result.addLabel(kvpair.getKey(),kvpair.getValue());
			}
		}
		//port
		if(template.get("spec")!=null){
			tem_spec = (LinkedHashMap) template.get("spec");
			if(tem_spec.get("containers")!=null) {
				ArrayList containers = (ArrayList) tem_spec.get("containers");
				for(int i = 0; i< containers.size(); i++) {
					LinkedHashMap container = (LinkedHashMap) containers.get(i);
					if(container.get("ports")!=null) {
						ArrayList ports = (ArrayList) container.get("ports");
						for(int j = 0; j<ports.size(); j++) {
							LinkedHashMap port = (LinkedHashMap) ports.get(j);
							for(Object key: port.keySet()) {
								String KeyString = (String) key;
								if(key.equals("containerPort")) {
									result.setPort((int)port.get("containerPort"));
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	
	public void addLabels(HashMap<String,String> labels) {
		LinkedHashMap result = new LinkedHashMap();
		// apiVersion and kind
		result.put("apiVersion", (String) content.get("apiVersion"));
		result.put("kind", (String) content.get("kind"));
		// metadata
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		result.put("metadata", metadata);
		//spec
		LinkedHashMap new_spec = new LinkedHashMap();
		LinkedHashMap new_selector = new LinkedHashMap();
		LinkedHashMap new_selector_matchLabels = new LinkedHashMap();
		LinkedHashMap new_template = new LinkedHashMap();
		LinkedHashMap new_template_metadata = new LinkedHashMap();
		LinkedHashMap new_template_metadata_labels = new LinkedHashMap();
		
		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
		LinkedHashMap selector = null;
		LinkedHashMap matchLabels = null;
		LinkedHashMap template = null;
		LinkedHashMap tem_metadata = null;
		LinkedHashMap tem_labels = null;
		LinkedHashMap tem_spec = null;
		ArrayList<KVPair> pod_labels = new ArrayList<KVPair>();
		if(spec.get("selector")!=null) {
			selector = (LinkedHashMap) spec.get("selector");
			if(selector.get("matchLabels")!=null) {
				matchLabels = (LinkedHashMap) selector.get("matchLabels");
				for(Object key: matchLabels.keySet()) {
					new_selector_matchLabels.put(key, matchLabels.get(key));
				}
			}else {
				System.out.println("error: no matchLabels");
			}
		}else {
			System.out.println("error: no selector");
		}
		if(spec.get("template")!=null) {
			template = (LinkedHashMap) spec.get("template");
			if(template.get("metadata")!=null) {
				tem_metadata = (LinkedHashMap) template.get("metadata");
				if(tem_metadata.get("labels")!=null) {
					tem_labels = (LinkedHashMap) tem_metadata.get("labels");
					for(Object key: tem_labels.keySet()) {
						new_template_metadata_labels.put(key, matchLabels.get(key));
					}
				}else {
					System.out.println("error: no labels");	
				}
			}else {
				System.out.print("error: no metadata");
			}
		}else {
			System.out.print("error: no template");
		}
		
		for(String key: labels.keySet()) {
			new_selector_matchLabels.put(key,labels.get(key));
			new_template_metadata_labels.put(key,labels.get(key));
		}
		
		new_selector.put("matchLabels", new_selector_matchLabels);
		new_template_metadata.put("labels", new_template_metadata_labels);
		new_template.put("metadata", new_template_metadata);
		new_spec.put("selector", new_selector);
		new_spec.put("replicas", spec.get("replicas"));
		
		//add template.spec by package
		if(template.get("spec")!=null){
			tem_spec = (LinkedHashMap) template.get("spec");
			new_template.put("spec", tem_spec);
		}
		new_spec.put("template", new_template);
		content = result;
	}
	
	public static void main(String args[]) {
		podYaml test = new podYaml("test2.yaml");
		pod testPod = test.getPod();
		System.out.println(testPod);
	}
}