package bean;

import java.util.ArrayList;
import java.util.BitSet;

public class namespace{
	String name;
	ArrayList<KVPair> labels;
	BitSet allowNS;
	
	public namespace() {
		name = "";
		labels = new ArrayList<KVPair>();
		allowNS = new BitSet();
	}
	
	public namespace(String name) {
		this.name = name;
		labels = new ArrayList<KVPair>();
		allowNS = new BitSet();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<KVPair> getLabels() {
		return labels;
	}
	public void setLabels(ArrayList<KVPair> labels) {
		this.labels = labels;
	}
	public BitSet getAllowNS() {
		return allowNS;
	}
	public void setAllowNS(BitSet allowNS) {
		this.allowNS = allowNS;
	}
	public void addLabel(KVPair label) {
		labels.add(label);
	}
	public KVPair getLabel(int i) {
		return labels.get(i);
	}
}