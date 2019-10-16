package bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class policy{
	String name;
	String namespace;
	Map podSelector;
	ArrayList inSelector;
	ArrayList<port> inports;
	ArrayList eSelector;
	ArrayList<port> eports;
	
	public policy() {
		this.name = "";
		this.namespace = "";
		this.podSelector = new HashMap<String,String>();
		this.inSelector = new ArrayList();
		this.inports = new ArrayList<port>();
		this.eSelector = new ArrayList();
		this.eports = new ArrayList<port>();
	}
	
	
}