package utils;

import java.util.Random;

public class randomStrUtil{
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
}