package bean;

import java.util.ArrayList;

public class abstractPod{
	String podYaml; // the configuration file of pod
	int type; // 0:Deployment, 1:DaemonSet, 2:StatefulSet
	ArrayList<Integer> ports;
	
}