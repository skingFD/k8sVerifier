package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import bean.allowLink;
import bean.bitMatrix;
import bean.serviceChain;
import bean.template.template;

public class Intentanalyzer{
	serviceChain intent;
	String intentYaml;
	bitMatrix intentMatrix;
	
	public Intentanalyzer(String intentYaml) {
		this.intentYaml = intentYaml;
		this.intent = new serviceChain();
		analyzeIntent();
	}
	
	public Intentanalyzer(serviceChain intent) {
		this.intent = intent;
	}
	
	public String getIntentYaml() {
		return intentYaml;
	}

	public void setIntentYaml(String intentYaml) {
		this.intentYaml = intentYaml;
	}
	
	public serviceChain getIntent() {
		return intent;
	}

	public void setIntent(serviceChain intent) {
		this.intent = intent;
	}

	public void analyzeIntent() {
		Yaml yaml = new Yaml();
		File f = new File(intentYaml);
		try {
			LinkedHashMap intent = (LinkedHashMap) yaml.load(new FileInputStream(f));
			ArrayList podlist = (ArrayList) intent.get("podlist");
			if(podlist != null) {
				for(Object podyaml: podlist) {
					this.intent.addPodYaml((String)podyaml);
				}
			}
			ArrayList linklist = (ArrayList) intent.get("links");
			if(linklist != null) {
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
					this.intent.addLink(new allowLink((int)linkmap.get("src"),(int)linkmap.get("dst"),80,false));
				}
			}
			ArrayList temlist = (ArrayList) intent.get("template");
			if(temlist != null) {
				for(Object temyaml: temlist) {
					template tem = new template((LinkedHashMap) temyaml);
					ArrayList<allowLink> temlinks = tem.analyzeTemplate();
					for(allowLink temlink: temlinks) {
						this.intent.addLink(temlink);
					}
				}
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void generateMatrix() {
		intentMatrix = new bitMatrix(this.intent.getPodYamls().size());
		for(allowLink tempLink: this.intent.getLinks()) {
			intentMatrix.setBit(tempLink.getSrcIndex(), tempLink.getDstIndex());
		}
	}
}