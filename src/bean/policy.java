package bean;

import java.util.ArrayList;
import java.util.HashMap;

public class policy{
	boolean inout; //0:ingress, 1:egress
	boolean allPods;
	boolean haveCidr;
	boolean haveNsSelector;
	boolean havePodSelector;
	String name;
	String namespace;
	ArrayList<String> cidr;
	ArrayList<String> except;
	HashMap<String,String> pods; //spec.podSelector
	HashMap<String,String> nsSelector;
	HashMap<String,String> podSelector;
	
	public policy() {
		this.inout = false;
		this.allPods = false;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.name = "";
		this.namespace = "";
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.pods = new HashMap<String,String>();
		this.nsSelector = new HashMap<String,String>();
		this.podSelector = new HashMap<String,String>();
	}
	
	public policy(boolean inout) {
		this.inout = inout;
		this.allPods = false;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.name = "";
		this.namespace = "";
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.pods = new HashMap<String,String>();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
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

	public HashMap<String, String> getPods() {
		return pods;
	}

	public void setPods(HashMap<String, String> pods) {
		this.pods = pods;
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