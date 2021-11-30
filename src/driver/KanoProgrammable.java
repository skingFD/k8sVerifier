package driver;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

import analyzer.BVgenerator;
import bean.allowLink;
import bean.intent.LinkDefinition;
import bean.intent.PodDefinition;
import bean.intent.PolicyDefinition;
import bean.resources.filter;
import bean.resources.pod;
import bean.resources.policies;
import bean.resources.policy;
import utils.fileUtil;
import analyzer.Policygenerator;

public class KanoProgrammable{
	public String userLabel = "user";
	public BVgenerator bvgenerator = new BVgenerator();
	String filePath = "";
	HashMap<String, PodDefinition> podDefinitions = new HashMap<String, PodDefinition>();
	HashMap<String, PolicyDefinition> policyDefinitions = new HashMap<String, PolicyDefinition>();
	HashMap<String, LinkDefinition> linkDefinitions = new HashMap<String, LinkDefinition>();
	HashMap<String, ArrayList<PodDefinition>> properties = new HashMap<String, ArrayList<PodDefinition>>();
	HashSet<String> invariants = new HashSet<String>();
	ArrayList<allowLink> linksToAdd = new ArrayList<allowLink>();
	
	public KanoProgrammable() {
		this.initInvariants();
        Scanner scanner = new Scanner(System.in);// 获得控制台输入流
		while(true) {
			System.out.println(">>");// 提示用户输入字符串
	        String text = scanner.nextLine();// 获得用户输入
	        if(text.equals("quit")) {
	        	break;
	        }else {
	        	analyzeCommand(text);
	        }
		}
		scanner.close();
	}
	
	public KanoProgrammable(String filePath) {
		this.initInvariants();
		ArrayList<String> commands = fileUtil.readKano(filePath);
		for(String command :commands) {
			analyzeCommand(command);
		}
	}
	
	public void initInvariants() {
		this.invariants.add("allReachable");
		this.invariants.add("allIsolated");
		this.invariants.add("certainIsolated");
		this.invariants.add("userReachable");
		this.invariants.add("policyCoverage");
		this.invariants.add("policyConflict");
	}
	
	public void initProperties() {
		this.properties.put("System", new ArrayList<PodDefinition>());//can reach everyone
		this.properties.put("Independent", new ArrayList<PodDefinition>());//cannot reach anyone
		this.properties.put("allReachable", new ArrayList<PodDefinition>());//can be reached by everyone
		this.properties.put("allIsolated", new ArrayList<PodDefinition>());//cannot be reached by anyone
	}
	
	public void analyzeCommand(String command) {
		if(command.startsWith("Pod")) {
			HashMap<String, String> labelMap = new HashMap<String, String>();
			String[] labels = command.split("(")[1].split(")")[0].split(",");
			for(String label : labels) {
				labelMap.put(label.split(":")[0], label.split(":")[1]);
			}
			String podName = command.split(" ")[1];
			if(this.podDefinitions.containsKey(podName) || this.policyDefinitions.containsKey(podName)) {
				System.out.print("Error, duplicated definition " + command);
			}
			this.podDefinitions.put(podName, new PodDefinition(podName, labelMap));
		}else if(command.startsWith("Policy")) {
			HashMap<String, String> selectMap = new HashMap<String, String>();
			HashMap<String, String> allowMap = new HashMap<String, String>();
			String[] selects = command.split("(")[1].split(")")[0].split(";")[0].split(",");
			String[] allows = command.split("(")[1].split(")")[0].split(";")[1].split(",");
			for(String select : selects) {
				selectMap.put(select.split(":")[0], select.split(":")[1]);
			}
			for(String allow : allows) {
				allowMap.put(allow.split(":")[0], allow.split(":")[1]);
			}
			String policyName = command.split(" ")[1];
			if(this.podDefinitions.containsKey(policyName) || this.policyDefinitions.containsKey(policyName)) {
				System.out.print("Error, duplicated definition " + command);
			}
			this.policyDefinitions.put(policyName, new PolicyDefinition(policyName, selectMap, allowMap));
		}else if(command.startsWith("Set")) {
			String objectName = command.split("(")[1].split(")")[0].split(",")[0];
			String propertyName = command.split("(")[1].split(")")[0].split(",")[1];
			if(!this.podDefinitions.containsKey(objectName)) {
				System.out.print("Error, pod not defined in " + command);
			}else {
				this.properties.get(propertyName).add(this.podDefinitions.get(objectName));
			}
		}else if(command.startsWith("Add")) {
			// add new pod/policy to bvgenerator
			String objectName = command.split("(")[1].split(")")[0];
			if(this.podDefinitions.containsKey(objectName)) {
				pod p = new pod();
				p.setName(objectName);
				p.setLabels(this.podDefinitions.get(objectName).getArg());
				this.bvgenerator.addPod(p);
			}else if(this.policyDefinitions.containsKey(objectName)) {
				policies p = new policies();
				p.setName(objectName);
				p.setPods(this.policyDefinitions.get(objectName).getSelectLabels());
				policy p0 = new policy();
				filter aFilter = new filter(false);
				if(this.policyDefinitions.get(objectName).getAllowLabels().size() > 0) {
					aFilter.setHavePodSelector(true);
					aFilter.setPodSelector(null);
				}
				p0.addToFilters(aFilter);
				p.addToIn(p0);
				this.bvgenerator.addPolicy(p);
			}else {
				//Unknown command
				assert false;
			}
		}else if(command.startsWith("Del")) {
			// del pod/policy from bvgenerator
			String objectName = command.split("(")[1].split(")")[0];
			if(this.podDefinitions.containsKey(objectName)) {
				int objectIndex = -1;
				for(int i = 0; i < this.bvgenerator.getPods().size(); i++) {
					if(this.bvgenerator.getPods().get(i).getName() == objectName) {
						objectIndex = i;
						break;
					}
				}
				if(objectIndex == -1) {
					System.out.println("Deletion before adding: " + command);
				}
				this.bvgenerator.removePod(objectIndex);
			}else if(this.policyDefinitions.containsKey(objectName)) {
				int objectIndex = -1;
				for(int i = 0; i < this.bvgenerator.getPolicies().size(); i++) {
					if(this.bvgenerator.getPolicies().get(i).getName() == objectName) {
						objectIndex = i;
						break;
					}
				}
				if(objectIndex == -1) {
					System.out.println("Deletion before adding: " + command);
				}
				this.bvgenerator.removePolicy(objectIndex);
			}else {
				//Unknown command
				assert false;
			}
		}else if(command.startsWith("Link")) {
			String name = command.split(" ")[1];
			String pod0 = command.split("(")[1].split(")")[0].split(",")[0];
			String pod1 = command.split("(")[1].split(")")[0].split(",")[1];
			this.linkDefinitions.put(command, new LinkDefinition(name, pod0, pod1));
		}else if(command.startsWith("Spec")) {
			this.bvgenerator.addNotSparse(command.split("(")[1].split(")")[0]);
		}else if(command.startsWith("Check")) {
			String invariant = command.split("(")[1].split(")")[0];
			if(!this.bvgenerator.isRunning()) {
				this.bvgenerator.prefilterVerify();
			}
			if(this.linkDefinitions.containsKey(invariant)) {
				this.verifyLink(this.linkDefinitions.get(invariant));
			}else if(this.invariants.contains(invariant)) {
				if(invariant == "allReachable") {
					this.verifyAllReachable();
				}else if(invariant == "allIsolated") {
					this.verifyAllIsolated();
				}else if(invariant == "UserReachable") {
					this.verifyUserReachable();
				}else if(invariant == "CertainIsolated") {
					this.verifyCertainIsotated();
				}else if(invariant == "PolicyConflict") {
					this.verifyPolicyConflict();
				}else {
					this.verifyPolicyCoverage();
				}
			}
		}else if(command.equals("Fix")) {
			Policygenerator pg = new Policygenerator(this.bvgenerator);
			pg.setAddLinks(this.linksToAdd);
		}else if(command.equals("Run")) {
			ArrayList<String> cmds = fileUtil.readKano(command.split(" ")[1]);
			for(String cmd :cmds) {
				analyzeCommand(cmd);
			}
		}
	}
	
	public void setUserLabel(String userLabel) {
		this.userLabel = userLabel;
	}
	
	public void addLink(String k1, String v1, String k2, String v2) {
		
	}
	
	public void addIsolation(String k1, String v1, String k2, String v2) {
		
	}
	
	public void setPublic(String k, String v) {
		
	}
	
	public void setIsolated(String k, String v) {
		
	}
	
	public void generateMatrix() {
		this.bvgenerator.prefilterVerify();
		this.bvgenerator.calculateAllowMatrixs();
	}
	
	public void addLink() {
		allowLink link0 = new allowLink(0, 4);
		allowLink link1 = new allowLink(0, 5);
		Policygenerator pg = new Policygenerator(this.bvgenerator);
		pg.getAddLinks().add(link0);
		pg.getAddLinks().add(link1);
		pg.generateFix();
		pg.mergePolicy();
		pg.generatePolicies();
	}
	
	public void allVerify() {
		for(String name : this.linkDefinitions.keySet()) {
			this.verifyLink(this.linkDefinitions.get(name));
		}
		this.bvgenerator.allReachableVerifier();
		this.bvgenerator.allIsolatedVerifier();
		this.bvgenerator.certainIsolatedVerifier();
		this.bvgenerator.userReachableVerifier();
		this.bvgenerator.policyCoverageVerifier();
		this.bvgenerator.policyConflictVerifier();
	}
	
	public void verifyLink(LinkDefinition link) {
		int srcIndex = -1;
		int dstIndex = -1;
		for(int i = 0; i < this.bvgenerator.getPods().size(); i++) {
			if(this.bvgenerator.getPods().get(i).getName() == link.getPod1()) {
				srcIndex = i;
			}
			if(this.bvgenerator.getPods().get(i).getName() == link.getPod2()) {
				dstIndex = i;
			}
		}
		if(srcIndex == -1 || dstIndex == -1) {
			System.out.print("Link between not-existing pods: " + link.getName());
		}
		this.bvgenerator.linkVerifier(new allowLink(srcIndex, dstIndex));
	}
	
	public void verifyAllReachable() {
		this.bvgenerator.allReachableVerifier();
	}
	
	public void verifyAllIsolated() {
		this.bvgenerator.allIsolatedVerifier();
	}
	
	public void verifyCertainIsotated() {
		this.bvgenerator.certainIsolatedVerifier();
	}
	
	public void verifyUserReachable() {
		this.bvgenerator.userReachableVerifier();
	}
	
	public void verifyPolicyCoverage() {
		this.bvgenerator.policyCoverageVerifier();
	}
	
	public void verifyPolicyConflict() {
		this.bvgenerator.policyConflictVerifier();
	}
	
	public static void main(String args[]) {
		// When testing incremental verification, you need to check:
		// 1. policy: selected pods, inAllow, eAllow
		// 2. pod: selected index, AllowPodE, AllowPodIn, AllowEIndex, AllowInIndex
		KanoProgrammable kano = new KanoProgrammable("examples\\test\\");
		kano.generateMatrix();
		kano.addLink();
		//Incremental
		//kano.bvgenerator.addPolicy("examples\\test_add\\testpolicy_add.yaml");
		//kano.bvgenerator.addPod("examples\\test_add\\testpod_add.yaml");
		//kano.bvgenerator.removePolicy(0);
		//kano.bvgenerator.removePod(0);
		//kano.allVerify();
	}
}