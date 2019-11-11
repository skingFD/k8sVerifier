package analyzer;

import java.util.ArrayList;
import java.util.BitSet;

import bean.allowLink;
import bean.filter;
import bean.namespace;
import bean.nsYaml;
import bean.pod;
import bean.podYaml;
import bean.policies;
import bean.policy;
import bean.policyYaml;
import utils.randomStrUtil;

public class Policygenerator{
	//input yamls
	ArrayList<policyYaml> policyYamlList;
	ArrayList<podYaml> podYamlList;
	ArrayList<nsYaml> nsYamlList;
	ArrayList<policies> policyList;
	ArrayList<pod> podList;
	ArrayList<namespace> nsList;
	//input intent
	ArrayList<allowLink> links;
	
	public Policygenerator() {
		policyYamlList = new ArrayList<policyYaml>();
		podYamlList = new ArrayList<podYaml>();
		nsYamlList = new ArrayList<nsYaml>();
		policyList = new ArrayList<policies>();
		podList = new ArrayList<pod>();
		nsList = new ArrayList<namespace>();
		links = new ArrayList<allowLink>();
	}
	
	public Policygenerator(ArrayList<String> podinput, ArrayList<String> nsinput, ArrayList<allowLink> linkinput) {
		policyYamlList = new ArrayList<policyYaml>();
		podYamlList = new ArrayList<podYaml>();
		nsYamlList = new ArrayList<nsYaml>();
		policyList = new ArrayList<policies>();
		podList = new ArrayList<pod>();
		nsList = new ArrayList<namespace>();
		links = new ArrayList<allowLink>();

		// initiate pod
		for(String podyaml: podinput) {
			podYamlList.add(new podYaml(podyaml));
		}
		for(podYaml podyaml: podYamlList) {
			podList.add(podyaml.getPod());
		}
		
		// initiate NS
		for(String nsyaml: nsinput) {
			nsYamlList.add(new nsYaml(nsyaml));
		}
		for(nsYaml nsyaml: nsYamlList) {
			nsList.add(nsyaml.getNS());
		}
		
		// initiate allowLink
		for(allowLink link: linkinput) {
			links.add(link);
		}
		generate();
	}
	
	public ArrayList<policyYaml> getPolicyYamlList() {
		return policyYamlList;
	}

	public void setPolicyYamlList(ArrayList<policyYaml> policyYamlList) {
		this.policyYamlList = policyYamlList;
	}

	public ArrayList<podYaml> getPodYamlList() {
		return podYamlList;
	}

	public void setPodYamlList(ArrayList<podYaml> podYamlList) {
		this.podYamlList = podYamlList;
	}

	public ArrayList<nsYaml> getNsYamlList() {
		return nsYamlList;
	}

	public void setNsYamlList(ArrayList<nsYaml> nsYamlList) {
		this.nsYamlList = nsYamlList;
	}

	public ArrayList<policies> getPolicyList() {
		return policyList;
	}

	public void setPolicyList(ArrayList<policies> policyList) {
		this.policyList = policyList;
	}

	public ArrayList<pod> getPodList() {
		return podList;
	}

	public void setPodList(ArrayList<pod> podList) {
		this.podList = podList;
	}

	public ArrayList<namespace> getNsList() {
		return nsList;
	}

	public void setNsList(ArrayList<namespace> nsList) {
		this.nsList = nsList;
	}

	public ArrayList<allowLink> getLinks() {
		return links;
	}

	public void setLinks(ArrayList<allowLink> links) {
		this.links = links;
	}

	public int getNS(String name) {
		for(int i = 0; i< nsList.size(); i++) {
			if(nsList.get(i).getName().equals(name)) {
				return i;
			}
		}
		return -1;
	}
	
	public void generate() {
		// 1.set allowpodin and allowpode
		// 2.record labels need to be attached
		// 3.attach labels
		BitSet haveIn = new BitSet();
		BitSet haveE = new BitSet();
		haveIn.clear(0, podList.size());
		haveE.clear(0, podList.size());
		for(int i = 0; i < links.size(); i++) {
			//record whether have intent, unused now
			haveIn.set(links.get(i).getDstIndex());
			haveE.set(links.get(i).getSrcIndex());
			podList.get(links.get(i).getDstIndex()).getIntentIn().set(links.get(i).getSrcIndex());
			podList.get(links.get(i).getSrcIndex()).getIntentE().set(links.get(i).getDstIndex());
		}
		for(int i = 0; i < podList.size(); i++) {
			boolean needPolicy = false;
			String podKey = randomStrUtil.getRandomStr(10);
			String podValue = randomStrUtil.getRandomStr(10);
			String NSKey = randomStrUtil.getRandomStr(10);
			String NSValue = randomStrUtil.getRandomStr(10);
			for(int j = 0; j < podList.size(); j++) {
				if(podList.get(i).getIntentIn().get(j)) {
					podList.get(j).addLabel(podKey, podValue);
					int nsIndex = getNS(podList.get(j).getName());
					if(nsIndex == -1) {
						// TODO if no NS
					}else {
						nsList.get(nsIndex).addLabel(NSKey, NSValue);
					}
					needPolicy = true;
				}
			}
			if(needPolicy) {
				String selectKey = randomStrUtil.getRandomStr(10);
				String selectValue = randomStrUtil.getRandomStr(10);
				String policyName = randomStrUtil.getRandomStr(10);
				podList.get(i).addLabel(selectKey, selectValue); // Attach selector label to pod
				policies testPolicies = new policies();
				policy inPolicy = new policy();
				filter inFilter = new filter(false);
				testPolicies.setHaveIn(true); // Ingress policy
				testPolicies.setNamespace(podList.get(i).getNamespace()); // Set policy namespace
				testPolicies.setName(policyName); // Policy name generated randomly
				testPolicies.putToPods(selectKey, selectValue); // Pod Selector 
				inFilter.setHaveNsSelector(true);
				inFilter.setHavePodSelector(true);
				inFilter.putPodSelector(podKey, podValue);
				inFilter.putNsSelector(NSKey, NSValue);
				inPolicy.addToFilters(inFilter);
				testPolicies.addToIn(inPolicy);
				policyList.add(testPolicies);
			}
		}
		//generate Yamls
		for(int i = 0; i < podList.size(); i++) {
			podYamlList.get(i).addLabels(podList.get(i).getLabels());
		}
		for(int i = 0; i < policyList.size(); i++) {
			policyYamlList.add(policyList.get(i).generateYaml());
		}
		for(int i = 0; i < nsList.size(); i++) {
			nsYamlList.add(nsList.get(i).generateYaml());
		}
	}
	public static void main(String args[]) {
		
		Policygenerator pg = new Policygenerator();
		// test main function

		// initiate policy

		// initiate pod
		podYaml podyaml1 = new podYaml("testdep1.yaml");
		podYaml podyaml2 = new podYaml("testdep2.yaml");
		pg.getPodYamlList().add(podyaml1);
		pg.getPodYamlList().add(podyaml2);
		pg.getPodList().add(podyaml1.getPod());
		pg.getPodList().add(podyaml2.getPod());
		
		// initiate NS
		nsYaml nsyaml1 = new nsYaml("testns1.yaml");
		nsYaml nsyaml2 = new nsYaml("testns2.yaml");
		pg.getNsList().add(nsyaml1.getNS());
		pg.getNsList().add(nsyaml2.getNS());
		
		// initiate allowLink
		allowLink alink = new allowLink(0,1,80,false);
		pg.getLinks().add(alink);
		
		pg.generate();
		System.out.println(pg);
	}
}