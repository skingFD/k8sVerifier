package analyzer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.yaml.snakeyaml.Yaml;

import bean.allowLink;
import bean.template.template;

public class Intentanalyzer{
	String intentYaml;
	ArrayList<String> podList = new ArrayList<String>();
	ArrayList<String> nsList = new ArrayList<String>();
	ArrayList<allowLink> linkList = new ArrayList<allowLink>();
	
	public Intentanalyzer(String intentYaml) {
		this.intentYaml = intentYaml;
		analyzeIntent();
	}
	
	public String getIntentYaml() {
		return intentYaml;
	}

	public void setIntentYaml(String intentYaml) {
		this.intentYaml = intentYaml;
	}

	public ArrayList<String> getPodList() {
		return podList;
	}

	public void setPodList(ArrayList<String> podList) {
		this.podList = podList;
	}

	public ArrayList<String> getNsList() {
		return nsList;
	}

	public void setNsList(ArrayList<String> nsList) {
		this.nsList = nsList;
	}

	public ArrayList<allowLink> getLinkList() {
		return linkList;
	}

	public void setLinkList(ArrayList<allowLink> linkList) {
		this.linkList = linkList;
	}

	public void analyzeIntent() {
		Yaml yaml = new Yaml();
		File f = new File(intentYaml);
		try {
			LinkedHashMap intent = (LinkedHashMap) yaml.load(new FileInputStream(f));
			ArrayList podlist = (ArrayList) intent.get("podlist");
			if(podlist != null) {
				for(Object podyaml: podlist) {
					podList.add((String)podyaml);
				}
			}
			ArrayList nslist = (ArrayList) intent.get("nslist");
			if(nslist != null) {
				for(Object nsyaml: nslist) {
					nsList.add((String)nsyaml);
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
					linkList.add(new allowLink((int)linkmap.get("src"),(int)linkmap.get("dst"),80,false));
				}
			}
			ArrayList temlist = (ArrayList) intent.get("template");
			if(temlist != null) {
				for(Object temyaml: temlist) {
					template tem = new template((LinkedHashMap) temyaml);
					ArrayList<allowLink> temlinks = tem.analyzeTemplate();
					for(allowLink temlink: temlinks) {
						linkList.add(temlink);
					}
				}
			}
			
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}