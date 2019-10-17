package bean;

public class policies{
	boolean haveIn;
	boolean haveE;
	policy inPolicy;
	policy ePolicy;
	
	public policies() {
		haveIn = false;
		haveE = false;
		inPolicy = new policy();
		ePolicy = new policy();
	}
	
	public policies(boolean haveIn, boolean haveE ,policy inPolicy, policy ePolicy) {
		this.haveIn = haveIn;
		this.haveE = haveE;
		this.inPolicy = inPolicy;
		this.ePolicy = ePolicy;
	}
	
	public boolean isHaveIn() {
		return haveIn;
	}

	public void setHaveIn(boolean haveIn) {
		this.haveIn = haveIn;
	}

	public boolean isHaveE() {
		return haveE;
	}

	public void setHaveE(boolean haveE) {
		this.haveE = haveE;
	}

	public policy getInPolicy() {
		return inPolicy;
	}
	public void setInPolicy(policy inPolicy) {
		this.inPolicy = inPolicy;
	}
	public policy getePolicy() {
		return ePolicy;
	}
	public void setePolicy(policy ePolicy) {
		this.ePolicy = ePolicy;
	}
}