package bean.intent;
//link(index_a, index_b)
public class LinkDefinition {
	String name;
	String pod1;
	String pod2;
	public LinkDefinition(String name, String pod1, String pod2){
		this.name = name;
		this.pod1 = pod1;
		this.pod2 = pod2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPod1() {
		return pod1;
	}
	public void setPod1(String pod1) {
		this.pod1 = pod1;
	}
	public String getPod2() {
		return pod2;
	}
	public void setPod2(String pod2) {
		this.pod2 = pod2;
	}
	
}