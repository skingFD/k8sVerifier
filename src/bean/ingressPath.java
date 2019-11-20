package bean;

public class ingressPath{
	String path;
	String serviceName;
	int servicePort;
	
	public ingressPath() {
		path = "";
		serviceName = "";
		servicePort = 80;
	}
	
	public ingressPath(String path, String serviceName, int servicePort) {
		this.path = path;
		this.serviceName = serviceName;
		this.servicePort = servicePort;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServicePort() {
		return servicePort;
	}

	public void setServicePort(int servicePort) {
		this.servicePort = servicePort;
	}
}