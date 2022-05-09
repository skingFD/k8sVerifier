package driver;

import java.io.File;
import java.util.ArrayList;

import analyzer.BVgenerator;
import bean.allowLink;
import analyzer.Policygenerator;

public class Kano{
	public String userLabel = "user";
	public BVgenerator bvgenerator = new BVgenerator();
	String filePath = "";
	
	public Kano() {
		
	}
	
	public Kano(String filePath) {
		this.bvgenerator.init(filePath);
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
		this.bvgenerator.allReachableVerifier();
		this.bvgenerator.allIsolatedVerifier();
		this.bvgenerator.certainIsolatedVerifier();
		this.bvgenerator.userReachableVerifier();
		this.bvgenerator.policyCoverageVerifier();
		this.bvgenerator.policyConflictVerifier();
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
		int index = Integer.parseInt(args[0]);
		int naive = Integer.parseInt(args[1]);
		// When testing incremental verification, you need to check:
		// 1. policy: selected pods, inAllow, eAllow
		// 2. pod: selected index, AllowPodE, AllowPodIn, AllowEIndex, AllowInIndex
		/**
		 * doing full relationship experiments
		 */
		/*
		System.out.println("Full relationship * 103");
		int[] podNum = {1000, 2000, 3000, 4000, 5000};
		int[] policyNum = {1000, 2000, 3000, 4000, 5000};
		int[] labelNum = {6, 8, 12, 14};
		for(int pod : podNum) {
			System.out.println("Pod Num: " + pod);
			for(int i = 0; i < 103; i++) {
				BVgenerator bg = new BVgenerator();
				bg.tempInitFullRelation(5000, 10, 5000, pod, 1000);
			}
		}
		for(int policy : policyNum) {
			System.out.println("Policy Num: " + policy);
			for(int i = 0; i < 103; i++) {
				BVgenerator bg = new BVgenerator();
				bg.tempInitFullRelation(5000, 10, 5000, 2000, policy);
			}
		}
		System.out.println("Label Num: 10");
		for(int i = 0; i < 103; i++) {
			BVgenerator bg = new BVgenerator();
			bg.tempInitFullRelation(5000, 10, 5000, 2000, 1000);
		}
		for(int label : labelNum) {
			System.out.println("Label Num: " + label);
			for(int i = 0; i < 103; i++) {
				BVgenerator bg = new BVgenerator();
				bg.tempInitFullRelation(2000, label, 1000, 2000, 1000);
			}
		}
		*/
		
		/**
		 * doing Full Comparison experiments
		 */
		
		BVgenerator bg = new BVgenerator();
		int[] podNum = {500, 1000, 2000, 5000, 10000, 20000, 50000, 100000};
		int[] policyNum = {345, 685, 1352, 3390, 6777, 13569, 34033, 68111};
		int i = index;

		
		int pod = podNum[i];
		int policy = policyNum[i];
		System.out.println("Pod Num: " + pod);
		bg = new BVgenerator();
		if(naive == 1) {
			System.out.println("Naive:");
			bg.tempFullComparison(pod, pod/100, policy, true);
		}else if(naive == 0) {
			System.out.println("Prefil and verifier:");
			bg.tempFullComparison(pod, pod/100, policy, false);
		}
		
		
		//System.out.println("Prefil and verifier:");
		//bg.tempInitIncre(150000, 1500, 75000);
		
		/**
		 * Incre and fix
		 */
		
		// BVgenerator bg = new BVgenerator();
		// int[] podNum = {500, 1000, 2000, 5000, 10000, 20000, 50000, 100000};
		// int[] policyNum = {200, 500, 1000, 2000, 5000, 10000, 20000, 50000};
		/*
		int pod = podNum[i];
		int policy = policyNum[i];
		System.out.println("Pod Num: " + pod);
		System.out.println("Incre and fix:");
		bg.tempInitIncre(pod, pod/100, policy);
		*/
	}
}