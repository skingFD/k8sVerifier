package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import analyzer.BVgenerator;
import analyzer.Intentanalyzer;
import analyzer.Policygenerator;
import bean.bitMatrix;
import bean.resources.pod;
import bean.yaml.podYaml;
import utils.yamlutil;

public class driver{ 
	public static void main(String args[]) {
		
		// TODO Finish main function
		bitMatrix unNeededLinks;
		bitMatrix neededLinks;
		
		//init podYamls
		ArrayList<String> podYamls = new ArrayList<String>();
		podYamls.add("testdep1.yaml");
		podYamls.add("testdep2.yaml");
		podYamls.add("testdep3.yaml");
		
		//init intent
		Intentanalyzer testIntentAnalyzer = new Intentanalyzer("testintent2.yaml");
		
		//init configuration now
		BVgenerator testBVgenerator = new BVgenerator();
		testBVgenerator.tempInit();
		testBVgenerator.setIntent(testIntentAnalyzer.getIntent());
		testBVgenerator.calculateIntentMatrixs();
		
		//init needed links and unneededlinks
		unNeededLinks = new bitMatrix(podYamls.size());
		neededLinks = new bitMatrix(podYamls.size());
		unNeededLinks.or(testBVgenerator.getIntentMatrix());
		unNeededLinks.xor(testBVgenerator.getReachabilityMatrix());
		neededLinks.or(unNeededLinks);
		unNeededLinks.and(testBVgenerator.getReachabilityMatrix());
		neededLinks.and(testBVgenerator.getIntentMatrix());
		
		//generate needed policy
		Policygenerator testPolicyGenerator = new Policygenerator(testIntentAnalyzer);
		System.out.println("Need to add policy:");
		System.out.println(testPolicyGenerator.getPolicyYamlList().get(0).yamlString);
	}
}