package bean;

import java.util.ArrayList;

public class namespace{
	String name;
	ArrayList<KVPair> labels = new ArrayList<KVPair>();
	
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
	
	public void addLabel(KVPair label) {
		labels.add(label);
	}
	
	public KVPair getLabel(int i) {
		return labels.get(i);
	}
}