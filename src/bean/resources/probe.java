package bean.resources;

import java.util.ArrayList;

public class probe{
	
	int probeType; // -1:null 0:startupProbe 1:livenessProbe 2:readinessProbe
	int handler; // -1:null 0:exec 1:httpGet
	String path;
	String port;
	ArrayList<String> commands;
	int initialDelaySeconds;
	int periodSeconds;
	int failureThreshold;
	
	public probe() {
		probeType = -1;
		handler = -1;
		path = "";
		port = "";
		commands = new ArrayList<String>();
		initialDelaySeconds = -1;
		periodSeconds = -1;
		failureThreshold = -1;
	}

	public int getProbeType() {
		return probeType;
	}

	public void setProbeType(int probeType) {
		this.probeType = probeType;
	}

	public int getHandler() {
		return handler;
	}

	public void setHandler(int handler) {
		this.handler = handler;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	public int getInitialDelaySeconds() {
		return initialDelaySeconds;
	}

	public void setInitialDelaySeconds(int initialDelaySeconds) {
		this.initialDelaySeconds = initialDelaySeconds;
	}

	public int getPeriodSeconds() {
		return periodSeconds;
	}

	public void setPeriodSeconds(int periodSeconds) {
		this.periodSeconds = periodSeconds;
	}

	public int getFailureThreshold() {
		return failureThreshold;
	}

	public void setFailureThreshold(int failureThreshold) {
		this.failureThreshold = failureThreshold;
	}
	
	public void addToCommands(String command) {
		this.commands.add(command);
	}
	
	public String getFromCommands(int i) {
		return this.commands.get(i);
	}
}