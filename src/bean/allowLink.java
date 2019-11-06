package bean;

//Allowed Link for policy generation
public class allowLink{
	int srcIndex;
	int dstIndex;
	int port;
	boolean protocol; // 0:TCP, 1:UDP
	
	public allowLink() {
		srcIndex = 0;
		dstIndex = 0;
		port = 0;
		protocol = false;
	}
	
	public allowLink(int srcIndex, int dstIndex, int port, boolean protocol) {
		this.srcIndex = srcIndex;
		this.dstIndex = dstIndex;
		this.port = port;
		this.protocol = protocol;
	}

	public int getSrcIndex() {
		return srcIndex;
	}

	public void setSrcIndex(int srcIndex) {
		this.srcIndex = srcIndex;
	}

	public int getDstIndex() {
		return dstIndex;
	}

	public void setDstIndex(int dstIndex) {
		this.dstIndex = dstIndex;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isProtocol() {
		return protocol;
	}

	public void setProtocol(boolean protocol) {
		this.protocol = protocol;
	}
}