package bean.resources;


import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

public class pod{
	String type; //Deployment, DaemonSet, StatefulSet
	String namespace;//TODO how to get the info of NS
	String name;
	String IP;//TODO how to calculate IP
	int port;
	HashMap<String,String> labels;
//	BitSet SelectorNS;
//	BitSet SelectorPod;
//	BitSet AllowNS;
//	BitSet AllowPod;
	//choose data structure, BitSet is stable, ArrayList performs better when sparse
	BitSet IntentIn;
	BitSet IntentE;
	BitSet AllowPodIn;
	BitSet AllowPodE;
	ArrayList<probe> Probes;//Used to add probe parameters, unused now
	boolean selectedIn;
	boolean selectedE;
	//record the policies select or allow this pod to find contradiction
	ArrayList<Integer> selectedIndex;
	ArrayList<Integer> AllowInIndex;
	ArrayList<Integer> AllowEIndex;
	
	
	public pod() {
		type = "Deployment";
		namespace = "default";
		name = "";
		IP = "0.0.0.0";
		labels = new HashMap<String,String>();
		IntentIn = new BitSet();
		IntentE = new BitSet();
		AllowPodIn = new BitSet();
		AllowPodE = new BitSet();
		Probes = new ArrayList<probe>();
		selectedIn = false;
		selectedE = false;
		selectedIndex = new ArrayList<Integer>();
		AllowInIndex = new ArrayList<Integer>();
		AllowEIndex = new ArrayList<Integer>();
	}
	
	public pod(String namespace, String name, String IP, HashMap<String,String> labels) {
		type = "Deployment";
		this.namespace = namespace;
		this.name = name;
		this.IP = IP;
		this.labels = labels;
		IntentIn = new BitSet();
		IntentE = new BitSet();
		AllowPodIn = new BitSet();
		AllowPodE = new BitSet();
		Probes = new ArrayList<probe>();
		selectedIn = false;
		selectedE = false;
		selectedIndex = new ArrayList<Integer>();
		AllowInIndex = new ArrayList<Integer>();
		AllowEIndex = new ArrayList<Integer>();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	
	public void setIntent(int length) {
		IntentIn = new BitSet(length);
		IntentIn.set(0, length);
		IntentE = new BitSet(length);
		IntentE.set(0, length);
	}
	
	public void clearIntent(int length) {
		IntentIn = new BitSet(length);
		IntentIn.clear(0, length);
		IntentE = new BitSet(length);
		IntentE.clear(0, length);
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
	
	public BitSet getIntentIn() {
		return IntentIn;
	}

	public void setIntentIn(BitSet intentIn) {
		IntentIn = intentIn;
	}

	public BitSet getIntentE() {
		return IntentE;
	}

	public void setIntentE(BitSet intentE) {
		IntentE = intentE;
	}

	public BitSet getAllowPodIn() {
		return AllowPodIn;
	}

	public void setAllowPodIn(BitSet allowPodIn) {
		if(this.isSelectedIn()) {
			this.AllowPodIn.or(allowPodIn);
		}else{
			this.setSelectedIn(true);
			this.AllowPodIn.clear();
			this.AllowPodIn.or(allowPodIn);
		}
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
		if(this.isSelectedE()) {
			this.AllowPodE.or(allowPodE);
		}else{
			this.setSelectedE(true);
			this.AllowPodE.clear();
			this.AllowPodE.or(allowPodE);
		}
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
	
	public ArrayList<probe> getProbes(){
		return this.Probes;
	}
	
	public void addToProbes(probe Probe) {
		this.Probes.add(Probe);
	}
	
	public probe getFromProbes(int i) {
		return this.Probes.get(i);
	}

	public boolean isSelectedE() {
		return selectedE;
	}

	public void setSelectedE(boolean selectedE) {
		this.selectedE = selectedE;
	}
	
	public boolean isSelectedIn() {
		return selectedIn;
	}

	public void setSelectedIn(boolean selectedIn) {
		this.selectedIn = selectedIn;
	}

	public void setProbes(ArrayList<probe> probes) {
		Probes = probes;
	}

	public ArrayList<Integer> getSelectedIndex() {
		return selectedIndex;
	}

	public void setSelectedIndex(ArrayList<Integer> selectedIndex) {
		this.selectedIndex = selectedIndex;
	}

	public ArrayList<Integer> getAllowInIndex() {
		return AllowInIndex;
	}

	public void setAllowInIndex(ArrayList<Integer> allowInIndex) {
		AllowInIndex = allowInIndex;
	}

	public ArrayList<Integer> getAllowEIndex() {
		return AllowEIndex;
	}

	public void setAllowEIndex(ArrayList<Integer> allowEIndex) {
		AllowEIndex = allowEIndex;
	}
}