package bean;


import java.util.BitSet;
import java.util.HashMap;

public class pod{
	String namespace;//TODO how to get the info of NS
	String name;
	String IP;//TODO how to calculate IP
	int port;
	HashMap<String,String> labels;
//	BitSet SelectorNS;
//	BitSet SelectorPod;
//	BitSet AllowNS;
//	BitSet AllowPod;
	BitSet AllowPodIn;
	BitSet AllowPodE;
	
	public pod() {
		namespace = "default";
		name = "";
		IP = "0.0.0.0";
		labels = new HashMap<String,String>();
	}
	
	public pod(String namespace, String name, String IP, HashMap<String,String> labels) {
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

	public HashMap<String,String> getLabels() {
		return labels;
	}

	public void setLabels(HashMap<String,String> labels) {
		this.labels = labels;
	}
	
	public void setall(int length) {
		AllowPodIn = new BitSet(length);
		AllowPodIn.set(0, length);
		AllowPodE = new BitSet(length);
		AllowPodE.set(0, length);
	}
	
	public void clearall(int length) {
		AllowPodIn = new BitSet(length);
		AllowPodIn.clear(0, length);
		AllowPodE = new BitSet(length);
		AllowPodE.clear(0, length);
	}
	
	public BitSet getAllowPodIn() {
		return AllowPodIn;
	}

	public void setAllowPodIn(BitSet allowPodIn) {
		AllowPodIn = allowPodIn;
	}
	
	public void andAllowPodIn(BitSet allowIn) {
		AllowPodIn.and(allowIn);
	}
	
	public void orAllowPodIn(BitSet allowIn) {
		AllowPodIn.or(allowIn);
	}

	public BitSet getAllowPodE() {
		return AllowPodE;
	}

	public void setAllowPodE(BitSet allowPodE) {
		AllowPodE = allowPodE;
	}
	
	public void andAllowPodE(BitSet allowE) {
		AllowPodE.and(allowE);
	}
	
	public void orAllowPodE(BitSet allowE) {
		AllowPodE.or(allowE);
	}

	public void addLabel(String Key,String Value) {
		this.labels.put(Key, Value);
	}
	
	public String getLabel(String Key) {
		return this.labels.get(Key);
	}
	
	public boolean checkAllowIn(int dest) {
		return AllowPodIn.get(dest);
	}
	
	public boolean checkAllowE(int dest) {
		return AllowPodE.get(dest);
	}
}