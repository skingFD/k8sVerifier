package bean.resources;

import java.util.ArrayList;

public class ingressRule{
	String host;
	ArrayList<ingressPath> httpPaths;
	ArrayList<ingressPath> httpsPaths;
	
	public ingressRule() {
		host = "";
		httpPaths = new ArrayList<ingressPath>();
		httpsPaths = new ArrayList<ingressPath>();
	}
	
	public ingressRule(String host) {
		this.host = host;
		httpPaths = new ArrayList<ingressPath>();
		httpsPaths = new ArrayList<ingressPath>();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public ArrayList<ingressPath> getHttpPaths() {
		return httpPaths;
	}

	public void setHttpPaths(ArrayList<ingressPath> httpPaths) {
		this.httpPaths = httpPaths;
	}

	public ArrayList<ingressPath> getHttpsPaths() {
		return httpsPaths;
	}

	public void setHttpsPaths(ArrayList<ingressPath> httpsPaths) {
		this.httpsPaths = httpsPaths;
	}
	
	public void addHttpPath(ingressPath path) {
		httpPaths.add(path);
	}
	
	public ingressPath getHttpPath(int i) {
		return httpPaths.get(i);
	}
	
	public void addHttpsPath(ingressPath path) {
		httpsPaths.add(path);
	}
	
	public ingressPath getHttpsPath(int i) {
		return httpsPaths.get(i);
	}
}