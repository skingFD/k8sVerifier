package bean.resources;

// portCast for policy generator
public class portCast{
	int type; // 0: ClusterIP, 1: NodePort, no LoadBalancer
	int podPort;
	int servicePort;
	int nodePort;
	String name;
	
	public portCast() {
		type = 0;
		podPort = -1;
		servicePort = -1;
		nodePort = -1;
		name = "";
	}

	public portCast(int type, int podPort, int servicePort, int nodePort, String name) {
		super();
		this.type = type;
		this.podPort = podPort;
		this.servicePort = servicePort;
		this.nodePort = nodePort;
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPodPort() {
		return podPort;
	}

	public void setPodPort(int podPort) {
		this.podPort = podPort;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}

	public int getNodePort() {
		return nodePort;
	}

	public void setNodePort(int nodePort) {
		this.nodePort = nodePort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}