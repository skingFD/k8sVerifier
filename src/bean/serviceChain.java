package bean;

import java.util.ArrayList;

import bean.resources.pod;
import bean.template.template;
import bean.yaml.podYaml;

public class serviceChain{
	ArrayList<podYaml> podYamls;
	ArrayList<pod> pods;
	ArrayList<KVPair> links;
	ArrayList<template> templates;
	
	public serviceChain() {
		podYamls = new ArrayList<podYaml>();
		pods = new ArrayList<pod>();
		links = new ArrayList<KVPair>();
		templates = new ArrayList<template>();
	}
	
	public void addPodYaml(podYaml PodYaml) {
		podYamls.add(PodYaml);
	}
	
	public podYaml getPodYaml(int i) {
		return podYamls.get(i);
	}
	
	public void addPod(pod Pod) {
		pods.add(Pod);
	}
	
	public pod getPod(int i) {
		return pods.get(i);
	}
	
	public void addLink(KVPair link) {
		links.add(link);
	}
	
	public KVPair getLink(int i) {
		return links.get(i);
	}
	
	public void addTemplate(template Template) {
		templates.add(Template);
	}
	
	public template getTemplate(int i) {
		return templates.get(i);
	}
}