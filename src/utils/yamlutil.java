package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import analyzer.Policygenerator;
import bean.allowLink;

public class yamlutil{
	/*
	public static Policygenerator analyzeIntent(String intentYaml) {
		Yaml yaml = new Yaml();
		File f = new File(intentYaml);
		ArrayList<String> podList = new ArrayList<String>();
		ArrayList<String> nsList = new ArrayList<String>();
		ArrayList<allowLink> linkList = new ArrayList<allowLink>();
		try {
			LinkedHashMap intent = (LinkedHashMap) yaml.load(new FileInputStream(f));
			ArrayList podlist = (ArrayList) intent.get("podlist");
			for(Object podyaml: podlist) {
				podList.add((String)podyaml);
			}
			ArrayList nslist = (ArrayList) intent.get("nslist");
			for(Object nsyaml: nslist) {
				nsList.add((String)nsyaml);
			}
			ArrayList linklist = (ArrayList) intent.get("links");
			for(Object linkyaml: linklist) {
				LinkedHashMap linkmap = (LinkedHashMap) linkyaml;
				int port;
				boolean protocol;
				if(linkmap.get("port")!=null) {
					port = (int) linkmap.get("port");
				}
				if(linkmap.get("protocol") == null) {
				}else if(((String)linkmap.get("protocol")).equals("UDP")) {
					protocol = true;
				}else{
					protocol = false;
				}
				linkList.add(new allowLink((int)linkmap.get("src"),(int)linkmap.get("dst"),80,false));
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return new Policygenerator(podList,linkList);
	}
	*/
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