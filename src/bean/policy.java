package bean;

import java.util.ArrayList;
import java.util.HashMap;

public class policy{
	boolean inout; //0:ingress, 1:egress
	boolean allPods;
	boolean haveCidr;
	boolean haveNsSelector;
	boolean havePodSelector;
	ArrayList<String> cidr; // spec.ingress.from.ipblock.cidr
	ArrayList<String> except; // spec.ingress.from.ipblock.except
	ArrayList<port> ports; // spec.ingress.ports
	HashMap<String,String> nsSelector; // spec.ingress.from.nsSelector spec.egress.to.nsSelector
	HashMap<String,String> podSelector; // spec.ingress.from.podSelector spec.egress.to.podSelector
	
	public policy() {
		this.inout = false;
		this.allPods = false;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.ports = new ArrayList<port>();
		this.nsSelector = new HashMap<String,String>();
		this.podSelector = new HashMap<String,String>();
	}
	
	public policy(boolean inout) {
		this.inout = inout;
		this.allPods = false;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.ports = new ArrayList<port>();
		this.nsSelector = new HashMap<String,String>();
		this.podSelector = new HashMap<String,String>();
	}

	public boolean isInout() {
		return inout;
	}

	public void setInout(boolean inout) {
		this.inout = inout;
	}

	public boolean isAllPods() {
		return allPods;
	}

	public void setAllPods(boolean allPods) {
		this.allPods = allPods;
	}

	public boolean isHaveCidr() {
		return haveCidr;
	}

	public void setHaveCidr(boolean haveCidr) {
		this.haveCidr = haveCidr;
	}

	public boolean isHaveNsSelector() {
		return haveNsSelector;
	}

	public void setHaveNsSelector(boolean haveNsSelector) {
		this.haveNsSelector = haveNsSelector;
	}

	public boolean isHavePodSelector() {
		return havePodSelector;
	}

	public void setHavePodSelector(boolean havePodSelector) {
		this.havePodSelector = havePodSelector;
	}

	public ArrayList<String> getCidr() {
		return cidr;
	}

	public void setCidr(ArrayList<String> cidr) {
		this.cidr = cidr;
	}

	public ArrayList<String> getExcept() {
		return except;
	}

	public void setExcept(ArrayList<String> except) {
		this.except = except;
	}

	public ArrayList<port> getPorts(){
		return ports;
	}
	
	public void setPorts(ArrayList<port> ports) {
		this.ports = ports;
	}

	public HashMap<String, String> getNsSelector() {
		return nsSelector;
	}

	public void setNsSelector(HashMap<String, String> nsSelector) {
		this.nsSelector = nsSelector;
	}

	public HashMap<String, String> getPodSelector() {
		return podSelector;
	}

	public void setPodSelector(HashMap<String, String> podSelector) {
		this.podSelector = podSelector;
	}
}