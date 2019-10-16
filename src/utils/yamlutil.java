package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

public class yamlutil{
	public static void main(String args[]) {
		Yaml yaml = new Yaml();
		File f = new File("test.yaml");
		try {
			LinkedHashMap result = (LinkedHashMap)yaml.load(new FileInputStream(f));
			LinkedHashMap test0 = (LinkedHashMap)result.get("spec");
			Object test = test0.get("policyTypes");
			System.out.println(test.getClass());
			System.out.println(test);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
}