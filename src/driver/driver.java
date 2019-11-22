package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import analyzer.Intentanalyzer;
import analyzer.Policygenerator;
import bean.resources.pod;
import utils.yamlutil;

public class driver{ 
	public static void main(String args[]) {
		// TODO Use Bit Vector to analysis porperties of pods and svcs
		Intentanalyzer testIntentAnalyzer = new Intentanalyzer("testintent2.yaml");
		Policygenerator testPolicyGenerator = new Policygenerator(testIntentAnalyzer);
		System.out.println("Need to add policy:");
		System.out.println(testPolicyGenerator.getPolicyYamlList().get(0).yamlString);
	}
}