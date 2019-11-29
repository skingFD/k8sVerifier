package bean;

import java.util.ArrayList;

import bean.template.template;

public class serviceChain{
	ArrayList<String> podYamls;
	
	ArrayList<allowLink> links;
	ArrayList<template> templates;
	
	public serviceChain() {
		podYamls = new ArrayList<String>();
		links = new ArrayList<allowLink>();
		templates = new ArrayList<template>();
	}
	
	public void addPodYaml(String PodYaml) {
		podYamls.add(PodYaml);
	}
	
	public String getPodYaml(int i) {
		return podYamls.get(i);
	}
	
	public void addLink(allowLink link) {
		links.add(link);
	}
	
	public allowLink getLink(int i) {
		return links.get(i);
	}
	
	public void addTemplate(template Template) {
		templates.add(Template);
	}
	
	public template getTemplate(int i) {
		return templates.get(i);
	}

	public ArrayList<String> getPodYamls() {
		return podYamls;
	}

	public void setPodYamls(ArrayList<String> podYamls) {
		this.podYamls = podYamls;
	}

	public ArrayList<allowLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<allowLink> links) {
		this.links = links;
	}

	public ArrayList<template> getTemplates() {
		return templates;
	}

	public void setTemplates(ArrayList<template> templates) {
		this.templates = templates;
	}
}