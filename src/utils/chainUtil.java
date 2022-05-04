package utils;

import java.util.ArrayList;
import java.util.HashMap;

import bean.resources.filter;
import bean.resources.pod;
import bean.resources.policies;
import bean.resources.policy;
import bean.resources.namespace;
import utils.fileUtil;

public class chainUtil{
	public static int userNum = 100;
	public static int podNum = 10000;
	public static int keyNum = 100;
	public static int podIndex = 0;
	public static int polIndex = 0;
	public static ArrayList<pod> podList = new ArrayList<pod>();
	public static ArrayList<policies> policyList = new ArrayList<policies>();
	public static void generateServiceChain(int tmpUserNum, int tmpPodNum) {
		userNum = tmpUserNum;
		podNum = tmpPodNum;
		keyNum = tmpUserNum;
		fileUtil.dic = "examples/full_compare/test" + podNum + "_" + userNum + "_" + userNum + "/";
		
		while(podList.size() < podNum) {
			int chainIndex = randomUtil.getRandomInt(0, 8);
			if(chainIndex == 0) {
				generateBoard();
			}else if(chainIndex == 1) {
				generateWasteBinSensor();
			}else if(chainIndex == 2) {
				generateSurveillanceCamera();
			}else if(chainIndex == 3) {
				generateAnomalyDetection();
			}else if(chainIndex == 4) {
				generateEmail();
			}else if(chainIndex == 5) {
				generatePhotoPrism();
			}else if(chainIndex == 6) {
				generateDatabase();
			}else if(chainIndex == 7) {
				generateElasticSearch();
			}else {
				assert false;
			}
		}
		for(int i = 0; i < podList.size(); i++) {
			fileUtil.writeResource(podList.get(i), "testpod" + i + ".yaml");
		}
		for(int i = 0; i < policyList.size(); i++) {
			fileUtil.writeResource(policyList.get(i), "testpolicy" + i + ".yaml");
		}
		namespace n = new namespace("default");
		fileUtil.writeResource(n, "testns0.yaml");
		return;
	}
	
	public static boolean isPrivate() {
		return randomUtil.getRandomInt(0, 5) < 3;
	}
	
	public static void generateBoard() {
		// 1 Load balance
		// a front end
		// a redis
		//pod
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_load_balance");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "load_balance");
		if(private_flag) {
			p.addLabel(private_key, "load_balance");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_front_end");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "front_end");
		if(private_flag) {
			p.addLabel(private_key, "front_end");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_redis");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "redis");
		if(private_flag) {
			p.addLabel(private_key, "redis");
		}
		podList.add(p);
		
		//policy
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "redis");
		if(private_flag) {
			f.putPodSelector(private_key, "redis");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "front_end");
		if(private_flag) {
			pols.putToPods(private_key, "front_end");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "front_end");
		if(private_flag) {
			f.putPodSelector(private_key, "front_end");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "load_balance");
		if(private_flag) {
			pols.putToPods(private_key, "load_balance");
		}
		policyList.add(pols);
	}
	
	public static void generateWasteBinSensor() {
		// a API
		// b Database
		// c Route Planner
		// d Server
		//pod
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_API");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "API");
		if(private_flag) {
			p.addLabel(private_key, "API");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_database");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "database");
		if(private_flag) {
			p.addLabel(private_key, "database");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_route_planner");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "route_planner");
		if(private_flag) {
			p.addLabel(private_key, "route_planner");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_server");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "server");
		if(private_flag) {
			p.addLabel(private_key, "server");
		}
		podList.add(p);
		
		//policy
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "server");
		if(private_flag) {
			f.putPodSelector(private_key, "server");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "route_planner");
		if(private_flag) {
			pols.putToPods(private_key, "route_planner");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "route_planner");
		if(private_flag) {
			f.putPodSelector(private_key, "route_planner");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "database");
		if(private_flag) {
			pols.putToPods(private_key, "database");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "database");
		if(private_flag) {
			f.putPodSelector(private_key, "database");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "API");
		if(private_flag) {
			pols.putToPods(private_key, "API");
		}
		policyList.add(pols);
	}
	
	public static void generateSurveillanceCamera() {
		// a Face detection
		// b Face matching
		// c Database
		// d Dashboard
		//pod
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_face_detection");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "face_detection");
		if(private_flag) {
			p.addLabel(private_key, "face_detection");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_face_matching");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "face_matching");
		if(private_flag) {
			p.addLabel(private_key, "face_matching");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_database");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "database");
		if(private_flag) {
			p.addLabel(private_key, "database");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_dashboard");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "dashboard");
		if(private_flag) {
			p.addLabel(private_key, "dashboard");
		}
		podList.add(p);
		
		//policy
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "dashboard");
		if(private_flag) {
			f.putPodSelector(private_key, "dashboard");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "database");
		if(private_flag) {
			pols.putToPods(private_key, "database");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "database");
		if(private_flag) {
			f.putPodSelector(private_key, "database");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "face_matching");
		if(private_flag) {
			pols.putToPods(private_key, "face_matching");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "face_matching");
		if(private_flag) {
			f.putPodSelector(private_key, "face_matching");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "face_detection");
		if(private_flag) {
			pols.putToPods(private_key, "face_detection");
		}
		policyList.add(pols);
	}
	
	public static void generateAnomalyDetection() {
		// pod
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		// policy
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		// birch-api
		// birch-cassandra
		p.setName("pod_" + podIndex + "_birch_api");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "birch_api");
		if(private_flag) {
			p.addLabel(private_key, "birch_api");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_birch_cassandra");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "birch_cassandra");
		if(private_flag) {
			p.addLabel(private_key, "birch_cassandra");
		}
		podList.add(p);
		
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "birch_api");
		if(private_flag) {
			f.putPodSelector(private_key, "birch_api");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "birch_cassandra");
		if(private_flag) {
			pols.putToPods(private_key, "birch_cassandra");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "birch_cassandra");
		if(private_flag) {
			f.putPodSelector(private_key, "birch_cassandra");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "birch_api");
		if(private_flag) {
			pols.putToPods(private_key, "birch_api");
		}
		policyList.add(pols);
		

		// robust
		p = new pod();
		p.setName("pod_" + podIndex + "_robust");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "robust");
		if(private_flag) {
			p.addLabel(private_key, "robust");
		}
		podList.add(p);
		
		// kmeans-api
		// kmeans-cassandra
		p = new pod();
		p.setName("pod_" + podIndex + "_kmeans_api");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "kmeans_api");
		if(private_flag) {
			p.addLabel(private_key, "kmeans_api");
		}
		podList.add(p);
		p = new pod();
		p.setName("pod_" + podIndex + "_kmeans_cassandra");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "kmeans_cassandra");
		if(private_flag) {
			p.addLabel(private_key, "kmeans_cassandra");
		}
		podList.add(p);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "kmeans_api");
		if(private_flag) {
			f.putPodSelector(private_key, "kmeans_api");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "kmeans_cassandra");
		if(private_flag) {
			pols.putToPods(private_key, "kmeans_cassandra");
		}
		policyList.add(pols);
		
		pols = new policies();
		pol = new policy();
		f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "kmeans_cassandra");
		if(private_flag) {
			f.putPodSelector(private_key, "kmeans_cassandra");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "kmeans_api");
		if(private_flag) {
			pols.putToPods(private_key, "kmeans_api");
		}
		policyList.add(pols);
		
		// isolation-api
		p = new pod();
		p.setName("pod_" + podIndex + "_isolation_api");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "isolation_api");
		if(private_flag) {
			p.addLabel(private_key, "isolation_api");
		}
		podList.add(p);
	}
	
	public static void generateEmail() {
		// 1 proxy
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_email_proxy");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "email_proxy");
		if(private_flag) {
			p.addLabel(private_key, "email_proxy");
		}
		podList.add(p);
		// 1 mailserver
		p = new pod();
		p.setName("pod_" + podIndex + "_email_mailserver");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "email_mailserver");
		if(private_flag) {
			p.addLabel(private_key, "email_mailserver");
		}
		podList.add(p);
		//policies
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "email_mailserver");
		if(private_flag) {
			f.putPodSelector(private_key, "email_mailserver");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "email_proxy");
		if(private_flag) {
			pols.putToPods(private_key, "email_proxy");
		}
		policyList.add(pols);
	}
	
	public static void generatePhotoPrism() {
		// 1 PhotoPrism
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		// pod
		pod p = new pod();
		p.setName("pod_" + podIndex + "_photoPrism");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "photoPrism");
		if(private_flag) {
			p.addLabel(private_key, "photoPrism");
		}
		podList.add(p);
		// policies
		/*
		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "mySQL");
		pol.addToFilters(f);
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "mySQL");
		policyList.add(pols);
		*/
	}
	
	public static void generateDatabase() {
		// n mysql
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_mySQL");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "mySQL");
		if(private_flag) {
			p.addLabel(private_key, "mySQL");
		}
		podList.add(p);

		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "mySQL");
		if(private_flag) {
			f.putPodSelector(private_key, "mySQL");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "mySQL");
		if(private_flag) {
			pols.putToPods(private_key, "mySQL");
		}
		policyList.add(pols);
	}
	
	public static void generateElasticSearch() {
		// n eck-operator
		boolean private_flag = false;
		String private_key = "private_key_" + randomUtil.getRandomInt(0, keyNum);
		if(isPrivate()) {
			private_flag = true;
		}
		String user_name = "user_" + randomUtil.getRandomInt(0, userNum);
		pod p = new pod();
		p.setName("pod_" + podIndex + "_elasticSearch");
		podIndex++;
		p.addLabel(user_name, "user");
		p.addLabel("role" + user_name, "elasticSearch");
		if(private_flag) {
			p.addLabel(private_key, "mySQL");
		}
		podList.add(p);

		policies pols = new policies();
		policy pol = new policy();
		filter f = new filter();
		f.setHavePodSelector(true);
		f.putPodSelector(user_name, "user");
		f.putPodSelector("role" + user_name, "elasticSearch");
		if(private_flag) {
			f.putPodSelector(private_key, "mySQL");
		}
		pol.addToFilters(f);
		pols.setName("pol_" + polIndex);
		polIndex++;
		pols.addToIn(pol);
		pols.putToPods(user_name, "user");
		pols.putToPods("role" + user_name, "elasticSearch");
		if(private_flag) {
			pols.putToPods(private_key, "mySQL");
		}
		policyList.add(pols);
	}

	public static void main(String args[]) {
		generateServiceChain(5, 500);
		generateServiceChain(10, 1000);
		generateServiceChain(20, 2000);
		generateServiceChain(50, 5000);
		generateServiceChain(100, 10000);
		generateServiceChain(200, 20000);
		generateServiceChain(500, 50000);
		generateServiceChain(1000, 100000);
	}
}