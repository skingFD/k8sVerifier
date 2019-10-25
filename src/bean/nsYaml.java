package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

public class nsYaml{
	public Yaml yaml;
	public LinkedHashMap content;
	public String yamlString;
	
	public nsYaml() {
		this.yaml = new Yaml();
		this.content = new LinkedHashMap();
		this.yamlString = yaml.dump(content);
	}
	
	public nsYaml(String FilePath) {
		this.yaml = new Yaml();
		File f = new File(FilePath);
		try {
			this.content = (LinkedHashMap)yaml.load(new FileInputStream(f));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		this.yamlString = yaml.dump(content);
	}
	
	public String getYamlDump() {
		this.yamlString = yaml.dump(content);
		return this.yamlString;
	}
	
	public namespace getNS() {
		namespace result = new namespace();
		// metadata
		LinkedHashMap metadata = (LinkedHashMap) content.get("metadata");
		if (metadata.get("name") != null) { // metadata.name
			result.setName((String) metadata.get("name"));
			result.addLabel(new KVPair("name",result.getName()));
		}
		if (metadata.get("labels") != null) { // metadata.labels
			LinkedHashMap labels = (LinkedHashMap) metadata.get("labels");
			for(Object key: labels.keySet()) {
				result.addLabel(new KVPair((String)key, (String)labels.get((String)key)));
			}
		}
		return result;
	}
	
	public static void main(String args[]) {
		nsYaml test = new nsYaml("testns.yaml");
		namespace testNS = test.getNS();
		System.out.println(testNS);
	}
}