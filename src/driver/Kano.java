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
		// When testing incremental verification, you need to check:
		// 1. policy: selected pods, inAllow, eAllow
		// 2. pod: selected index, AllowPodE, AllowPodIn, AllowEIndex, AllowInIndex
		//Kano kano = new Kano("examples\\test\\");
		//kano.generateMatrix();
		//kano.addLink();
		//Incremental
		//kano.bvgenerator.addPolicy("examples\\test_add\\testpolicy_add.yaml");
		//kano.bvgenerator.addPod("examples\\test_add\\testpod_add.yaml");
		//kano.bvgenerator.removePolicy(0);
		//kano.bvgenerator.removePod(0);
		//kano.allVerify();
		BVgenerator bg = new BVgenerator();
		bg.tempInit2(100, 5, 98);
	}
}