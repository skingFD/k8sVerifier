package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class myYaml{
	public Yaml yaml;
	public LinkedHashMap content;
	public String yamlString;
	
	public myYaml() {
		this.yaml = new Yaml();
		this.content = new LinkedHashMap();
		this.yamlString = yaml.dump(content);
	}
	
	public myYaml(String FilePath) {
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
	
	public LinkedHashMap getMap(String property) {
		return (LinkedHashMap) content.get(property);
	}
	
	public ArrayList getList(String property) {
		return (ArrayList) content.get(property);
	}
	
	public String getString(String property) {
		return (String) content.get(property);
	}
	
	public ArrayList<String> getSelectorList() {
		ArrayList<String> result = new ArrayList<String>();
		//get namespace
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		String namespace = (String) metadata.get("namespace");
		result.add("ns="+namespace);
		
		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
		LinkedHashMap podSelector = (LinkedHashMap) spec.get("podSelector");
		LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
		
		for (Object key: matchLabels.keySet()) {
			result.add((String)key + "=" + (String)matchLabels.get((String)key));
		}
		
		return result;
	}
	
	// TODO finish this policy generate function
	public policies getPolicy(boolean inout) {//0:ingress, 1:egress
		policies result = new policies();
		policy inPolicy = new policy();
		policy ePolicy = new policy();
		
		//spec
		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
		if(spec.get("policyTypes") != null) { // spec.policyType
			ArrayList policyTypes = (ArrayList) spec.get("policyTypes");
			for(int i = 0; i < policyTypes.size(); i++) {
				String policyType = (String)policyTypes.get(i);
				if(policyType.equals("Ingress")) {
					result.setHaveIn(true);
				}else if(policyType.equals("Egress")) {
					result.setHaveE(true);
				}
			}
		}else {
			// TODO error, no policyType
		}
		if(spec.get("ingress") != null) { // spec.ingress
			ArrayList ingress = (ArrayList) spec.get("ingress");
			if(ingress.size() != 0) {
				result.setHaveIn(true);
			}
			for(int i = 0; i < ingress.size(); i++) {
				
			}
			// TODO analysis matchlabels
		}
		if(spec.get("egress") != null) { // spec.egress
			ArrayList egress = (ArrayList) spec.get("egress");
			if(egress.size() != 0) {
				result.setHaveE(true);
			}
		}
		if(spec.get("podSelector") != null) { // spec.podSelector
			LinkedHashMap podSelector = (LinkedHashMap) spec.get("podSelector");
			if(podSelector.isEmpty()) {
				if(result.isHaveIn()) {
					inPolicy.setAllPods(true);
				}
				if(result.isHaveE()) {
					ePolicy.setAllPods(true);
				}
			}else {
				if(podSelector.get("matchLabels") != null) { // spec.podSelector.matchLabels
					LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
					for(Object key: matchLabels.keySet()) {
						if(result.isHaveIn()) {
							inPolicy.getPods().put((String)key, (String)matchLabels.get((String)key));
						}
						if(result.isHaveE()) {
							inPolicy.getPods().put((String)key, (String)matchLabels.get((String)key));
						}
					}
				}else {
					// TODO error, no matchLabels
				}
			}
		}else {
			// TODO error, no podSelector
		}
		
		// metadata
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		if (metadata.get("name") != null) { // metadata.name
			if (result.isHaveIn()) {
				inPolicy.setName((String) metadata.get("name"));
			}
			if (result.isHaveE()) {
				ePolicy.setName((String) metadata.get("name"));
			}
		}
		if (metadata.get("namespace") != null) { // metadata.namespace
			if (result.isHaveIn()) {
				inPolicy.setNamespace((String) metadata.get("namespace"));
			}
			if (result.isHaveE()) {
				ePolicy.setNamespace((String) metadata.get("namespace"));
			}
		} else {
			if (result.isHaveIn()) {
				inPolicy.setNamespace("default");
			}
			if (result.isHaveE()) {
				ePolicy.setNamespace("default");
			}
		}

		result.setInPolicy(inPolicy);
		result.setePolicy(ePolicy);
		return result;
	}
	
	public ArrayList<String> getAllowList() {
		ArrayList<String> result = new ArrayList<String>();
		//get ingress
		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
		ArrayList ingress = (ArrayList) spec.get("ingress");
		LinkedHashMap inMap = (LinkedHashMap) ingress.get(0);
		if(inMap.get("from")!=null) {
			ArrayList from = (ArrayList) inMap.get("from");
			for(int i = 0; i < from.size(); i++) {
				LinkedHashMap fromItem = (LinkedHashMap) from.get(i);
				for (Object key: fromItem.keySet()) {
					String keyString = (String) key;
					if(keyString.equals("ipBlock")) { //Ingress ipBlock
						LinkedHashMap ipBlock = (LinkedHashMap) fromItem.get(keyString);
						if(ipBlock.get("cidr")!=null) { //Ingress ipBlock cidr
							String cidr = (String)ipBlock.get("cidr");
							result.add("cidr=" + cidr);
						}
						if(ipBlock.get("except")!=null) {
							String except = ipBlock.get("except").toString();
							result.add("except" + except);
						}
					}else if(keyString.equals("namespaceSelector")){ //Ingress nsSelector
						LinkedHashMap namespaceSelector = (LinkedHashMap) fromItem.get(keyString);
						if(namespaceSelector.get("matchLabels")!=null) {
							LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
							for (Object nsLabel: matchLabels.keySet()) {//ingress ns_key value 
								result.add("ns_"+nsLabel.toString() + "=" + matchLabels.get(nsLabel.toString()).toString());
							}
						}
					}else if(keyString.equals("podSelector")){ //Ingress podSelector
						LinkedHashMap podSelector = (LinkedHashMap) fromItem.get(keyString);
						if(podSelector.get("matchLabels")!=null) {
							LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
							for (Object Label: matchLabels.keySet()) {//ingress key value 
								result.add(Label.toString() + "=" + matchLabels.get(Label.toString()).toString());
							}
						}
					}
				}
			}
		}
		// TODO select ports
		// TODO How to deal with IP block
		// get egress
		ArrayList egress = (ArrayList) spec.get("egress");
		LinkedHashMap eMap = (LinkedHashMap) egress.get(0);
		if(eMap.get("to")!=null) {
			ArrayList to = (ArrayList) eMap.get("to");
			for(int i = 0; i < to.size(); i++) {
				LinkedHashMap toItem = (LinkedHashMap) to.get(i);
				for (Object key: toItem.keySet()) {
					String keyString = (String) key;
					if(keyString.equals("ipBlock")) { //Ingress ipBlock
						LinkedHashMap ipBlock = (LinkedHashMap) toItem.get(keyString);
						if(ipBlock.get("cidr")!=null) { //Ingress ipBlock cidr
							String cidr = (String)ipBlock.get("cidr");
							result.add("cidr=" + cidr);
						}
						if(ipBlock.get("except")!=null) {
							String except = ipBlock.get("except").toString();
							result.add("except=" + except);
						}
					}else if(keyString.equals("namespaceSelector")){ //Ingress nsSelector
						LinkedHashMap namespaceSelector = (LinkedHashMap) toItem.get(keyString);
						if(namespaceSelector.get("matchLabels")!=null) {
							LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
							for (Object nsLabel: matchLabels.keySet()) {//ingress ns_key value 
								result.add("ns_"+nsLabel.toString() + "=" + matchLabels.get(nsLabel.toString()).toString());
							}
						}
					}else if(keyString.equals("podSelector")){ //Ingress podSelector
						LinkedHashMap podSelector = (LinkedHashMap) toItem.get(keyString);
						if(podSelector.get("matchLabels")!=null) {
							LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
							for (Object Label: matchLabels.keySet()) {//ingress key value 
								result.add(Label.toString() + matchLabels.get(Label.toString()).toString());
							}
						}
					}
				}
			}
		}
		
		return result;
	}
	
	public static void main(String args[]) {
		myYaml myyaml = new myYaml("test.yaml");
		ArrayList<String> Selector = myyaml.getSelectorList();
		ArrayList<String> Allow = myyaml.getAllowList();
		System.out.println(Selector);
		System.out.println(Allow);
	}
}