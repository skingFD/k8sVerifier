package bean;

public class port{
	String protocol;
	int port;
	
	public port() {
		protocol = "TCP";
		port = 80;
	}
	public port(String protocol, int port) {
		this.protocol = protocol;
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
	
}