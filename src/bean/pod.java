package bean;

import java.util.ArrayList;
import java.util.BitSet;

public class pod{
	String namespace;//TODO how to get the info of NS
	String name;
	String IP;//TODO how to calculate IP
	int port;
	ArrayList<KVPair> labels;
	BitSet SelectorNS;
	BitSet SelectorPod;
	BitSet AllowNS;
	BitSet AllowPod;
	
	public pod() {
		namespace = "default";
		name = "";
		IP = "0.0.0.0";
		labels = new ArrayList<KVPair>();
	}
	
	public pod(String namespace, String name, String IP, ArrayList<KVPair> labels) {
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
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}

	public ArrayList<KVPair> getLabels() {
		return labels;
	}

	public void setLabels(ArrayList<KVPair> labels) {
		this.labels = labels;
	}
	
	public BitSet getSelectorNS() {
		return SelectorNS;
	}

	public void setSelectorNS(BitSet selectorNS) {
		SelectorNS = selectorNS;
	}

	public BitSet getSelectorPod() {
		return SelectorPod;
	}

	public void setSelectorPod(BitSet selectorPod) {
		SelectorPod = selectorPod;
	}

	public BitSet getAllowNS() {
		return AllowNS;
	}

	public void setAllowNS(BitSet allowNS) {
		AllowNS = allowNS;
	}

	public BitSet getAllowPod() {
		return AllowPod;
	}

	public void setAllowPod(BitSet allowPod) {
		AllowPod = allowPod;
	}

	public void addLabel(KVPair label) {
		this.labels.add(label);
	}
	
	public KVPair getLabel(int i) {
		return this.labels.get(i);
	}
}