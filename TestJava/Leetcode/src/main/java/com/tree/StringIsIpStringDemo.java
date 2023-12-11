package com.lee.tree;

public class StringIsIpStringDemo {
	public static void main(String[] args) {
		//Scanner in = new Scanner(System.in);
		//int a = in.nextInt();
		//System.out.println(a);
		StringIsIpStringDemo.getNumber("...1");
		StringIsIpStringDemo.getNumber("..1");
		StringIsIpStringDemo.getNumber("0.0.0.1");
	}
	
	public static int getNumber(String s) {
		String[] arr = s.split("\\.");
		int res = 0;
		int wight = 1;
		try {
			if (arr.length == 4 && isTrue(arr)) {
				for (int i = 3; i >= 0; i--) {
					if (i != 3) wight *= 255;
					res += wight * Integer.valueOf(arr[i]);
				}
				return res;
			}
		} catch (NumberFormatException e) {
		
		} finally {
			return -1;
		}
	}
	
	public static boolean isTrue(String[] res) throws NumberFormatException {
		for (String re : res) {
			if (Integer.valueOf(re) < 0 && Integer.valueOf(re) > 255) return false;
		}
		return true;
	}
	
}



