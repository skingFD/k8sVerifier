package utils;

import java.util.ArrayList;
import java.util.Random;

public class randomUtil{
	/**
	 * generate random string with
	 * @param length
	 * @return
	 */
	public static String getRandomStr(int length) {
		//String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_";
		String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < length; i++) {
			int number = random.nextInt(53);
			sb.append(str.charAt(number));
		}
		return sb.toString();
	}
	
	/**
	 * generate [min, max-1]
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max-min) + min;
	}
	/**
	 * generate [min, max-1] * num
	 * @param min
	 * @param max
	 * @param num
	 * @return
	 */
	public static ArrayList<Integer> getRandomInt(int min, int max, int num) {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		ArrayList<Integer> result = new ArrayList<Integer>();
		int tempIndex = -1;
		for(int i = min; i < max; i++) {
			temp.add(i);
		}
		Random random = new Random();
		for(int i = 0; i < num; i++) {
			tempIndex = random.nextInt(temp.size());
			result.add(temp.get(tempIndex));
			temp.set(tempIndex, temp.get(temp.size()-1));
			temp.remove(temp.size()-1);
		}
		return result;
	}
}