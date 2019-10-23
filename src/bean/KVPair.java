package bean;

public class KVPair{
	String Key;
	String Value;
	
	public KVPair() {
		Key = "";
		Value = "";
	}
	
	public KVPair(String Key, String Value) {
		this.Key = Key;
		this.Value = Value;
	}
	
	public String getKey() {
		return Key;
	}
	public void setKey(String key) {
		Key = key;
	}
	public String getValue() {
		return Value;
	}
	public void setValue(String value) {
		Value = value;
	}
	
	public boolean equals(Object obj) {
		if(obj instanceof KVPair) {
			KVPair other = (KVPair) obj;
			return Key.equals(other.Key)&&Value.equals(other.Value);
		}
		return super.equals(obj);
	}
	
	public int hashCode() {
		return (Key+Value).hashCode();
	}
}