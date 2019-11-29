package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import analyzer.BVgenerator;
import analyzer.Intentanalyzer;
import analyzer.Policygenerator;
import bean.resources.pod;
import utils.yamlutil;

public class driver{ 
	public static void main(String args[]) {
		// TODO Finish main function
		//init podYamls
		ArrayList<String> podYamls = new ArrayList<String>();
		
		//init intent
		Intentanalyzer testIntentAnalyzer = new Intentanalyzer("testintent2.yaml");
		
		//init configuration now
		BVgenerator testBVgenerator = new BVgenerator();
		
		//generate needed policy
		Policygenerator testPolicyGenerator = new Policygenerator(testIntentAnalyzer);
		System.out.println("Need to add policy:");
		System.out.println(testPolicyGenerator.getPolicyYamlList().get(0).yamlString);
	}
}