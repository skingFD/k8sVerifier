package bean;

import java.util.ArrayList;

public class policy{
	ArrayList<filter> filters;
	ArrayList<port> ports;
	
	public policy() {
		filters = new ArrayList<filter>();
		ports = new ArrayList<port>();
	}
	
	public policy(ArrayList<filter> filters, ArrayList<port> ports) {
		this.filters = filters;
		this.ports = ports;
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
}