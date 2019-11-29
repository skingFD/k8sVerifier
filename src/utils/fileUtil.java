package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class fileUtil {
	public static void writeFile(String fileName) {
		try {
			BufferedWriter bw = new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(new File("output/" + fileName)), "UTF-8"));
			bw.write("test0");
			bw.newLine();
			bw.write("test1");
			bw.close();
		} catch (Exception e) {
			System.err.println("write errors :" + e);
		}
	}
	
	public static void main(String args[]) {
		writeFile("test.yaml");
	}
}