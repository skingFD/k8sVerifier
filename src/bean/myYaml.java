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
	
	// policy generate function
	public policies getPolicies() {
		policies result = new policies();
		
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
			ArrayList ingressArray = (ArrayList) spec.get("ingress");			
			if(ingressArray.size() != 0) {
				result.setHaveIn(true);
			}
			for (int ingressIndex = 0; ingressIndex < ingressArray.size(); ingressIndex++) {
				policy inPolicy = new policy();
				LinkedHashMap ingress = (LinkedHashMap) ingressArray.get(ingressIndex);
				if (ingress.get("from") != null) { // spec.ingress.from
					ArrayList from = (ArrayList) ingress.get("from");
					for (int i = 0; i < from.size(); i++) {
						LinkedHashMap fromItem = (LinkedHashMap) from.get(i);
						filter inFilter = new filter(false);
						for (Object keyObject : fromItem.keySet()) {
							String key = (String) keyObject;
							if (key.equals("ipBlock")) { // spec.ingress.from.ipBlock
								LinkedHashMap ipBlock = (LinkedHashMap) fromItem.get(key);
								inFilter.setHaveCidr(true);
								if (ipBlock.get("cidr") != null) { // spec.ingress.from.ipBlock.cidr
									String cidr = (String) ipBlock.get("cidr");
									inFilter.getCidr().add(cidr);
								}
								if (ipBlock.get("except") != null) { // spec.ingress.from.ipBlock.except
									ArrayList except = (ArrayList) ipBlock.get("except");
									for (int j = 0; j < except.size(); j++) {
										String expectItem = (String) except.get(j);
										inFilter.getExcept().add(expectItem);
									}
								}
							} else if (key.equals("namespaceSelector")) { // spec.ingress.from.namespaceSelector
								LinkedHashMap namespaceSelector = (LinkedHashMap) fromItem.get("namespaceSelector");
								inFilter.setHaveNsSelector(true);
								if (namespaceSelector.get("matchLabels") != null) { // spec.ingress.from.namespaceSelector.matchLabels
									LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
									for (Object NSkeyObject : matchLabels.keySet()) {
										String NSkey = (String) NSkeyObject;
										String NSValue = (String) matchLabels.get(NSkey);
										inFilter.getNsSelector().put(NSkey, NSValue);
									}
								}
							} else if (key.equals("podSelector")) { // sepc.ingress.from.podSelector
								LinkedHashMap podSelector = (LinkedHashMap) fromItem.get("podSelector");
								inFilter.setHavePodSelector(true);
								if (podSelector.get("matchLabels") != null) { // spec.ingress.from.podSelector.matchLabels
									LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
									for (Object PodkeyObject : matchLabels.keySet()) {
										String Podkey = (String) PodkeyObject;
										String PodValue = (String) matchLabels.get(Podkey);
										inFilter.getPodSelector().put(Podkey, PodValue);
									}
								}
							}
						}
						inPolicy.addToFilters(inFilter);
					}
				}
				if (ingress.get("ports") != null) { // spec.ingress.ports
					ArrayList ports = (ArrayList) ingress.get("ports");
					for (int i = 0; i < ports.size(); i++) {
						port Port= new port();
						LinkedHashMap portYaml = (LinkedHashMap) ports.get(i);
						if(portYaml.get("port") != null) {
							Port.setPort((Integer) portYaml.get("port"));
						}else {
							Port.setPort(-1);
						}
						if(portYaml.get("protocol") != null) {
							Port.setProtocol((String) portYaml.get("protocol"));
						}else {
							Port.setProtocol("TCP");
						}
						inPolicy.addToPorts(Port);
					}
				}
				result.addToIn(inPolicy);
			}
		}
		if (spec.get("egress") != null) { // spec.egress
			ArrayList egressArray = (ArrayList) spec.get("egress");
			if (egressArray.size() != 0) {
				result.setHaveE(true);
			}
			for (int egressIndex = 0; egressIndex < egressArray.size(); egressIndex++) {
				policy ePolicy = new policy();
				LinkedHashMap egress = (LinkedHashMap) egressArray.get(egressIndex);
				if (egress.get("to") != null) { // spec.egress.from
					ArrayList to = (ArrayList) egress.get("to");
					for (int i = 0; i < to.size(); i++) {
						LinkedHashMap toItem = (LinkedHashMap) to.get(i);
						filter eFilter = new filter(false);
						for (Object keyObject : toItem.keySet()) {
							String key = (String) keyObject;
							if (key.equals("ipBlock")) { // spec.egress.to.ipBlock
								LinkedHashMap ipBlock = (LinkedHashMap) toItem.get(key);
								eFilter.setHaveCidr(true);
								if (ipBlock.get("cidr") != null) { // spec.egress.to.ipBlock.cidr
									String cidr = (String) ipBlock.get("cidr");
									eFilter.getCidr().add(cidr);
								}
								if (ipBlock.get("except") != null) { // spec.egress.to.ipBlock.except
									ArrayList except = (ArrayList) ipBlock.get("except");
									for (int j = 0; j < except.size(); j++) {
										String expectItem = (String) except.get(j);
										eFilter.getExcept().add(expectItem);
									}
								}
							} else if (key.equals("namespaceSelector")) { // spec.egress.to.namespaceSelector
								LinkedHashMap namespaceSelector = (LinkedHashMap) toItem.get("namespaceSelector");
								eFilter.setHaveNsSelector(true);
								if (namespaceSelector.get("matchLabels") != null) { // spec.egress.to.namespaceSelector.matchLabels
									LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
									for (Object NSkeyObject : matchLabels.keySet()) {
										String NSkey = (String) NSkeyObject;
										String NSValue = (String) matchLabels.get(NSkey);
										eFilter.getNsSelector().put(NSkey, NSValue);
									}
								}
							} else if (key.equals("podSelector")) { // sepc.egress.to.podSelector
								LinkedHashMap podSelector = (LinkedHashMap) toItem.get("podSelector");
								eFilter.setHavePodSelector(true);
								if (podSelector.get("matchLabels") != null) { // spec.egress.to.podSelector.matchLabels
									LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
									for (Object PodkeyObject : matchLabels.keySet()) {
										String Podkey = (String) PodkeyObject;
										String PodValue = (String) matchLabels.get(Podkey);
										eFilter.getPodSelector().put(Podkey, PodValue);
									}
								}
							}
						}
						ePolicy.addToFilters(eFilter);
					}
				}
				if (egress.get("ports") != null) { // spec.egress.ports
					ArrayList ports = (ArrayList) egress.get("ports");
					for (int i = 0; i < ports.size(); i++) {
						port Port= new port();
						LinkedHashMap portYaml = (LinkedHashMap) ports.get(i);
						if(portYaml.get("port") != null) {
							Port.setPort((Integer) portYaml.get("port"));
						}else {
							Port.setPort(-1);
						}
						if(portYaml.get("protocol") != null) {
							Port.setProtocol((String) portYaml.get("protocol"));
						}else {
							Port.setProtocol("TCP");
						}
						ePolicy.addToPorts(Port);
					}
				}
				result.addToE(ePolicy);
			}
		}
		if(spec.get("podSelector") != null) { // spec.podSelector
			LinkedHashMap podSelector = (LinkedHashMap) spec.get("podSelector");
			if(podSelector.isEmpty()) {
				result.setAllPods(true);
			}else {
				if(podSelector.get("matchLabels") != null) { // spec.podSelector.matchLabels
					LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
					for(Object key: matchLabels.keySet()) {
						result.putToPods((String)key, (String)matchLabels.get((String)key));
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
			result.setName((String) metadata.get("name"));
		}
		if (metadata.get("namespace") != null) { // metadata.namespace
			result.setNamespace((String) metadata.get("namespace"));
		} else {
			result.setNamespace((String) metadata.get("default"));
		}
		return result;
	}
	
//	public ArrayList<String> getAllowList() {
//		ArrayList<String> result = new ArrayList<String>();
//		//get ingress
//		LinkedHashMap spec = (LinkedHashMap) content.get("spec");
//		ArrayList ingress = (ArrayList) spec.get("ingress");
//		LinkedHashMap inMap = (LinkedHashMap) ingress.get(0);
//		if(inMap.get("from")!=null) {
//			ArrayList from = (ArrayList) inMap.get("from");
//			for(int i = 0; i < from.size(); i++) {
//				LinkedHashMap fromItem = (LinkedHashMap) from.get(i);
//				for (Object key: fromItem.keySet()) {
//					String keyString = (String) key;
//					if(keyString.equals("ipBlock")) { //Ingress ipBlock
//						LinkedHashMap ipBlock = (LinkedHashMap) fromItem.get(keyString);
//						if(ipBlock.get("cidr")!=null) { //Ingress ipBlock cidr
//							String cidr = (String)ipBlock.get("cidr");
//							result.add("cidr=" + cidr);
//						}
//						if(ipBlock.get("except")!=null) {
//							String except = ipBlock.get("except").toString();
//							result.add("except" + except);
//						}
//					}else if(keyString.equals("namespaceSelector")){ //Ingress nsSelector
//						LinkedHashMap namespaceSelector = (LinkedHashMap) fromItem.get(keyString);
//						if(namespaceSelector.get("matchLabels")!=null) {
//							LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
//							for (Object nsLabel: matchLabels.keySet()) {//ingress ns_key value 
//								result.add("ns_"+nsLabel.toString() + "=" + matchLabels.get(nsLabel.toString()).toString());
//							}
//						}
//					}else if(keyString.equals("podSelector")){ //Ingress podSelector
//						LinkedHashMap podSelector = (LinkedHashMap) fromItem.get(keyString);
//						if(podSelector.get("matchLabels")!=null) {
//							LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
//							for (Object Label: matchLabels.keySet()) {//ingress key value 
//								result.add(Label.toString() + "=" + matchLabels.get(Label.toString()).toString());
//							}
//						}
//					}
//				}
//			}
//		}
//		// TODO select ports
//		// TODO How to deal with IP block
//		// get egress
//		ArrayList egress = (ArrayList) spec.get("egress");
//		LinkedHashMap eMap = (LinkedHashMap) egress.get(0);
//		if(eMap.get("to")!=null) {
//			ArrayList to = (ArrayList) eMap.get("to");
//			for(int i = 0; i < to.size(); i++) {
//				LinkedHashMap toItem = (LinkedHashMap) to.get(i);
//				for (Object key: toItem.keySet()) {
//					String keyString = (String) key;
//					if(keyString.equals("ipBlock")) { //Ingress ipBlock
//						LinkedHashMap ipBlock = (LinkedHashMap) toItem.get(keyString);
//						if(ipBlock.get("cidr")!=null) { //Ingress ipBlock cidr
//							String cidr = (String)ipBlock.get("cidr");
//							result.add("cidr=" + cidr);
//						}
//						if(ipBlock.get("except")!=null) {
//							String except = ipBlock.get("except").toString();
//							result.add("except=" + except);
//						}
//					}else if(keyString.equals("namespaceSelector")){ //Ingress nsSelector
//						LinkedHashMap namespaceSelector = (LinkedHashMap) toItem.get(keyString);
//						if(namespaceSelector.get("matchLabels")!=null) {
//							LinkedHashMap matchLabels = (LinkedHashMap) namespaceSelector.get("matchLabels");
//							for (Object nsLabel: matchLabels.keySet()) {//ingress ns_key value 
//								result.add("ns_"+nsLabel.toString() + "=" + matchLabels.get(nsLabel.toString()).toString());
//							}
//						}
//					}else if(keyString.equals("podSelector")){ //Ingress podSelector
//						LinkedHashMap podSelector = (LinkedHashMap) toItem.get(keyString);
//						if(podSelector.get("matchLabels")!=null) {
//							LinkedHashMap matchLabels = (LinkedHashMap) podSelector.get("matchLabels");
//							for (Object Label: matchLabels.keySet()) {//ingress key value 
//								result.add(Label.toString() + matchLabels.get(Label.toString()).toString());
//							}
//						}
//					}
//				}
//			}
//		}
//		
//		return result;
//	}
	
	public static void main(String args[]) {
		myYaml myyaml = new myYaml("test1.yaml");
		policies Policies = myyaml.getPolicies();
		ArrayList<String> Selector = myyaml.getSelectorList();
		//ArrayList<String> Allow = myyaml.getAllowList();
		System.out.println(Policies);
		System.out.println(Selector);
		//System.out.println(Allow);
	}
}