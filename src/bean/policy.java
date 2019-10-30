package bean;

import java.util.ArrayList;
import java.util.BitSet;

public class policy{
	ArrayList<filter> filters;
	ArrayList<port> ports;
	BitSet allow; 
	
	public policy() {
		filters = new ArrayList<filter>();
		ports = new ArrayList<port>();
		allow = new BitSet();
	}
	
	public policy(ArrayList<filter> filters, ArrayList<port> ports) {
		this.filters = filters;
		this.ports = ports;
		allow = new BitSet();
	}

	public ArrayList<filter> getFilters() {
		return filters;
	}

	public void setFilters(ArrayList<filter> filters) {
		this.filters = filters;
	}

	public ArrayList<port> getPorts() {
		return ports;
	}

	public void setPorts(ArrayList<port> ports) {
		this.ports = ports;
	}
	
	public BitSet getAllow() {
		return allow;
	}

	public void setAllow(BitSet allow) {
		this.allow = allow;
	}

	public void addToFilters(filter Filter) {
		filters.add(Filter);
	}
	
	public filter getFromFilters(int i) {
		return filters.get(i);
	}
	
	public void addToPorts(port Port) {
		ports.add(Port);
	}
	
	public port getFromPorts(int i) {
		return ports.get(i);
	}
	
	public void calculateAllow(int i, pod Pod, namespace NS) {
		for(filter Filter: filters) {
			boolean NSMatch = true;
			boolean PodMatch = true;
			for(String Key: Filter.getNsSelector().keySet()) {
				if(!Filter.getNsSelector().get(Key).equals(NS.getLabels().get(Key))) {
					NSMatch = false;
					break;
				}
			}
			if(!NSMatch) {
				continue;
			}
			for(String Key: Filter.getPodSelector().keySet()) {
				if(!Filter.getPodSelector().get(Key).equals(Pod.getLabels().get(Key))) {
					PodMatch = false;
					break;
				}
			}
			if(!PodMatch) {
				continue;
			}
			allow.set(i);
			//TODO IP,IP,IP
		}
	}
}