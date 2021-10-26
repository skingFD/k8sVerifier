package driver;

import java.io.File;
import java.util.ArrayList;

import analyzer.BVgenerator;

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
		Kano kano = new Kano("examples\\test\\");
		kano.generateMatrix();
		kano.bvgenerator.addPolicy("examples\\test_add\\testpolicy_add.yaml");
		kano.bvgenerator.addPod("examples\\test_add\\testpod_add.yaml");
		kano.allVerify();
	}
}