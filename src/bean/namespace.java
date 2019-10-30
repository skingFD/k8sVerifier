package bean;

import java.util.BitSet;
import java.util.HashMap;

public class namespace{
	String name;
	HashMap<String,String> labels;
	BitSet allowNS;
	
	public namespace() {
		name = "";
		labels = new HashMap<String,String>();
		allowNS = new BitSet();
	}
	
	public namespace(String name) {
		this.name = name;
		labels = new HashMap<String,String>();
		allowNS = new BitSet();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public HashMap<String,String> getLabels() {
		return labels;
	}
	public void setLabels(HashMap<String,String> labels) {
		this.labels = labels;
	}
	public BitSet getAllowNS() {
		return allowNS;
	}
	public void setAllowNS(BitSet allowNS) {
		this.allowNS = allowNS;
	}
	public void addLabel(String Key, String Value) {
		labels.put(Key, Value);
	}
	public String getLabel(String Key) {
		return labels.get(Key);
	}
}