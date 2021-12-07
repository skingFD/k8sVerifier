package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashSet;

import analyzer.BVgenerator;
import analyzer.Policygenerator;
import bean.allowLink;
import bean.yaml.policyYaml;

public class fileUtil {
	public static int podLimit = 1000;
	public static int nsLimit = 10;
	public static int policyLimit = 500;
	
	public static int podLabelLimit = 5;
	public static int nsLabelLimit = 5;
	public static int keyLimit = 10;
	public static int valueLimit = 10;
	public static int userLimit = 5;
	public static int policySelectLabelLimit = 3;
	public static int policyNsLabelLimit = 3;
	public static int policyPodLabelLimit = 3;
	public static void writeFile(String fileName) {
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("examples/test/" + fileName)), "UTF-8"));
			bw.write("test0");
			bw.newLine();
			bw.write("test1");
			bw.close();
		} catch (Exception e) {
			System.err.println("write errors :" + e);
		}
	}
	//generate random pods;
	public static void generateRandomPod(String fileName, int podNum) {
		//Init random parameters
		int labels = randomUtil.getRandomInt(1, podLabelLimit+1);
		ArrayList<Integer> keyList = randomUtil.getRandomInt(0, keyLimit, labels);
		ArrayList<Integer> valueList = new ArrayList<Integer>();
		for(int i = 0;i<labels;i++) {
			valueList.add(randomUtil.getRandomInt(0, valueLimit));
		}
		//Generate
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("examples/test/" + fileName)), "UTF-8"));
			bw.write("apiVersion: apps/v1");
			bw.newLine();
			bw.write("kind: Deployment");
			bw.newLine();
			bw.write("metadata:");
			bw.newLine();
			bw.write("  name: pod" + podNum);
			bw.newLine();
			bw.write("  namespace: namespace" + randomUtil.getRandomInt(0, nsLimit));
			bw.newLine();
			bw.write("spec:");
			bw.newLine();
			bw.write("  replicas: 1");
			bw.newLine();
			bw.write("  selector:");
			bw.newLine();
			bw.write("    matchLabels:");
			bw.newLine();
			for(int i = 0; i < labels; i++) {
				bw.write("      key" + keyList.get(i) + ": value" + valueList.get(i));
				bw.newLine();
			}
			bw.write("  template:");
			bw.newLine();
			bw.write("    metadata:");
			bw.newLine();
			bw.write("      labels:");
			bw.newLine();
			for(int i = 0; i < labels; i++) {
				bw.write("        key" + keyList.get(i) + ": value" + valueList.get(i));
				bw.newLine();
			}
			bw.write("    spec:");
			bw.newLine();
			bw.write("      containers:");
			bw.newLine();
			bw.write("      - name: myweb");
			bw.newLine();
			bw.write("        image: tomcat:8.5-jre8");
			bw.newLine();
			bw.write("        ports:");
			bw.newLine();
			bw.write("        - containerPort: 80");
			bw.close();
		} catch (Exception e) {
			System.err.println("write errors :" + e);
		}
	}
	//Generate random namespaces
	public static void generateRandomNs(String fileName, int nsNum) {
		//Init random parameters
		int labels = randomUtil.getRandomInt(1, nsLabelLimit+1);
		ArrayList<Integer> keyList = randomUtil.getRandomInt(0, keyLimit, labels);
		ArrayList<Integer> valueList = new ArrayList<Integer>();
		for(int i = 0;i<labels;i++) {
			valueList.add(randomUtil.getRandomInt(0, valueLimit));
		}
		//Generate
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("examples/test/" + fileName)), "UTF-8"));
			bw.write("apiVersion: v1");
			bw.newLine();
			bw.write("kind: Namespace");
			bw.newLine();
			bw.write("metadata:");
			bw.newLine();
			bw.write("  name: namespace" + nsNum);
			bw.newLine();
			bw.write("  labels:");
			bw.newLine();
			bw.write("    user: user" + randomUtil.getRandomInt(0, userLimit));
			bw.newLine();
			for(int i = 0; i < labels; i++) {
				bw.write("    key" + keyList.get(i) + ": value" + valueList.get(i));
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			System.err.println("write errors :" + e);
		}
	}
	
	//Generate random policies
	public static void generateRandomPolicy(String fileName, int policyNum) {
		//Init random parameters
		int type = randomUtil.getRandomInt(0, 3);//0:ns 1:pod 2:ns+pod 
		int selectLabels = randomUtil.getRandomInt(1, policySelectLabelLimit + 1);
		int nsLabels = randomUtil.getRandomInt(1, policyNsLabelLimit + 1);
		int podLabels = randomUtil.getRandomInt(1, policyPodLabelLimit + 1);
		ArrayList<Integer> selectKeyList = randomUtil.getRandomInt(0, keyLimit, selectLabels);
		ArrayList<Integer> nsKeyList = randomUtil.getRandomInt(0, keyLimit, nsLabels);
		ArrayList<Integer> podKeyList = randomUtil.getRandomInt(0, keyLimit, podLabels);
		ArrayList<Integer> selectValueList = new ArrayList<Integer>();
		ArrayList<Integer> nsValueList = new ArrayList<Integer>();
		ArrayList<Integer> podValueList = new ArrayList<Integer>();
		for (int i = 0; i < selectLabels; i++) {
			selectValueList.add(randomUtil.getRandomInt(0, valueLimit));
		}
		for (int i = 0; i < nsLabels; i++) {
			nsValueList.add(randomUtil.getRandomInt(0, valueLimit));
		}
		for (int i = 0; i < podLabels; i++) {
			podValueList.add(randomUtil.getRandomInt(0, valueLimit));
		}
		//Generate
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("examples/test/" + fileName)), "UTF-8"));
			bw.write("apiVersion: networking.k8s.io/v1");
			bw.newLine();
			bw.write("kind: NetworkPolicy");
			bw.newLine();
			bw.write("metadata:");
			bw.newLine();
			bw.write("  name: policy" + policyNum);
			bw.newLine();
			bw.write("  namespace: namespace" + randomUtil.getRandomInt(0, nsLimit));
			bw.newLine();
			bw.write("spec:");
			bw.newLine();
			bw.write("  podSelector:");
			bw.newLine();
			bw.write("    matchLabels:");
			bw.newLine();
			for (int i = 0; i < selectLabels; i++) {
				bw.write("      key"+ selectKeyList.get(i) +": value" + selectValueList.get(i));
				bw.newLine();
			}
			bw.write("  policyTypes:");
			bw.newLine();
			bw.write("  ingress:");
			bw.newLine();
			bw.write("  - from:");
			bw.newLine();
			//ns and pod selector
			if(type == 0) {
				bw.write("    - namespaceSelector:");
				bw.newLine();
				bw.write("        matchLabels:");
				bw.newLine();
				for (int i = 0; i < nsLabels; i++) {
					bw.write("          key" + nsKeyList.get(i) + ": value" + nsValueList.get(i));
					bw.newLine();
				}
			}else if(type == 1) {
				bw.write("    - podSelector:");
				bw.newLine();
				bw.write("        matchLabels:");
				bw.newLine();
				for (int i = 0; i < podLabels; i++) {
					bw.write("          key" + podKeyList.get(i) + ": value" + podValueList.get(i));
					bw.newLine();
				}
			}else if(type == 2) {
				bw.write("    - namespaceSelector:");
				bw.newLine();
				bw.write("        matchLabels:");
				bw.newLine();
				for (int i = 0; i < nsLabels; i++) {
					bw.write("          key" + nsKeyList.get(i) + ": value" + nsValueList.get(i));
					bw.newLine();
				}
				bw.write("      podSelector:");
				bw.newLine();
				bw.write("        matchLabels:");
				bw.newLine();
				for (int i = 0; i < podLabels; i++) {
					bw.write("          key" + podKeyList.get(i) + ": value" + podValueList.get(i));
					bw.newLine();
				}
			}
			bw.close();
		} catch (Exception e) {
			System.err.println("write errors :" + e);
		}
	}
	
	public static ArrayList<String> readKano(String filename) {
		ArrayList<String> result = new ArrayList<String>();
		try {
			File file = new File(filename);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTXT = null;
				while ((lineTXT = bufferedReader.readLine()) != null) {
					String clearLine = lineTXT.toString().trim();
					if(clearLine.startsWith("include")) {
						result.addAll(readKano(clearLine.split(" ")[1]));
					}else {
						result.add(lineTXT.toString().trim());
					}
				}
				read.close();
			}else{
			    System.out.println("Can't find file: " + filename);
			}
		} catch (Exception e) {
			System.out.println("Error in reading " + filename);
			e.printStackTrace();
		}
		return result;
	}
	
	public static void generateRandomConfigs() {
		for(int i = 0; i < podLimit; i++) {
			generateRandomPod("testpod" + i + ".yaml",i);
		}
		for(int i = 0; i < nsLimit; i++) {
			generateRandomNs("testns" + i +".yaml",i);
		}
		for(int i = 0; i < policyLimit; i++) {
			generateRandomPolicy("testpolicy" + i +".yaml",i);
		}
	}
	
	public static void generateNotSparse(int podNum, int nsNum, int policyNum) {
		BVgenerator bg = new BVgenerator();
		bg.tempInitPods(podNum, nsNum, policyNum);
		Policygenerator pg = new Policygenerator(bg);
		int linkNum = podNum*podNum;
		HashSet<Integer> delLinks = new HashSet<Integer>();
		//for(int i = 0; i < linkNum/2; i++) {
		for(int i = 0; i < 9900; i++) {
				int newLink = randomUtil.getRandomInt(0, linkNum);
			while(delLinks.contains(newLink)) {
				newLink = randomUtil.getRandomInt(0, linkNum);
			}
			delLinks.add(newLink);
		}
		for(int delLink : delLinks) {
			allowLink link = new allowLink(delLink%podNum, delLink/podNum);
			pg.getDelLinks().add(link);
		}
		pg.generateFix();
		pg.mergePolicy();
		pg.generatePolicies();
		for(int i = podNum; i < bg.getPolicyYamlList().size(); i++) {
			policyYaml p = bg.getPolicyYamlList().get(i);
			try {
				File pFile = new File("examples/policy" + podNum + 
									"_" + nsNum + 
									"_" + policyNum + 
									"/testpolicy" + (i-podNum) + ".yaml");
				
				pFile.createNewFile();
				BufferedWriter bw = new BufferedWriter(
							new OutputStreamWriter(new FileOutputStream(pFile), "UTF-8"));
				bw.write(p.getYamlDump());
				bw.close();
			} catch (Exception e) {
				System.err.println("write errors :" + e);
			}
		}
		System.out.print("");
	}
	
	public static void main(String args[]) {
		int podNum = 100;
		int nsNum = 5;
		int policyNum = 50;
		generateNotSparse(podNum, nsNum, policyNum);
	}
}