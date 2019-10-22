package bean;

import java.util.ArrayList;
import java.util.HashMap;

public class filter{
	boolean inout; //0:ingress, 1:egress
	boolean haveCidr;
	boolean haveNsSelector;
	boolean havePodSelector;
	ArrayList<String> cidr; // spec.ingress.from.ipblock.cidr
	ArrayList<String> except; // spec.ingress.from.ipblock.except
	HashMap<String,String> nsSelector; // spec.ingress.from.nsSelector spec.egress.to.nsSelector
	HashMap<String,String> podSelector; // spec.ingress.from.podSelector spec.egress.to.podSelector
	
	public filter() {
		this.inout = false;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.nsSelector = new HashMap<String,String>();
		this.podSelector = new HashMap<String,String>();
	}
	
	public filter(boolean inout) {
		this.inout = inout;
		this.haveCidr = false;
		this.haveNsSelector = false;
		this.havePodSelector = false;
		this.cidr = new ArrayList<String>();
		this.except = new ArrayList<String>();
		this.nsSelector = new HashMap<String,String>();
		this.podSelector = new HashMap<String,String>();
	}

	public boolean isInout() {
		return inout;
	}

	public void setInout(boolean inout) {
		this.inout = inout;
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