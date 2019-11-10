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
		ArrayList<policyYaml> policyYamlList = new ArrayList<policyYaml>();
		ArrayList<podYaml> podYamlList = new ArrayList<podYaml>();
		ArrayList<nsYaml> nsYamlList = new ArrayList<nsYaml>();
		ArrayList<policies> policyList = new ArrayList<policies>();
		ArrayList<pod> podList = new ArrayList<pod>();
		ArrayList<namespace> nsList = new ArrayList<namespace>();
		ArrayList<allowLink> links = new ArrayList<allowLink>();
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

	}
}