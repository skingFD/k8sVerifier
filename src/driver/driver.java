package driver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bean.pod;

public class driver{ 
	public static void main(String args[]) {
		// TODO Use Bit Vector to analysis porperties of pods and svcs
		ArrayList<pod> pods = new ArrayList<pod>();
		Map labels1 = new HashMap<String,String>();
		labels1.put("app", "test1");
		labels1.put("name", "test1");
		labels1.put("role", "test1");
		pod pod1 = new pod("test1","test1","10.33.0.21",labels1);
		Map labels2 = new HashMap<String,String>();
		labels2.put("app", "test2");
		labels2.put("name", "test2");
		labels2.put("role", "test2");
		pod pod2 = new pod("test2","test2","10.33.0.22",labels2);
		pods.add(pod1);
		pods.add(pod2);
	}
}