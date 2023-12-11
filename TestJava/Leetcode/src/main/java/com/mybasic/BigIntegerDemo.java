package com.mybasic;


import org.junit.Test;

/**
 * <li>Hulu 浮点数加法 外加 科学技术法表示</li>
 * <li>WXG 大数加法</li>
 */
public class BigIntegerDemo {
	@Test
	public void testInt() {
		BigInteger big = new BigInteger();
		big.add("43243243243200000000000000");
		System.out.println(big);
		
		// big.add("2423423");
		// System.out.println(big);
		System.out.println(big.addPrecision("99000000.123"));
		
		System.out.println(big.toSciFormat());
	}
}

class BigInteger {
	private String value;
	
	public String toSciFormat() {
		String[] strings = splitBigSmall(value);
		int idxNonZero = -1, idxPoint = -1;
		for (int i = 0; i < value.length(); i++) {
			char curChar = value.charAt(i);
			if (curChar != '0' && curChar != '.') {
				if (idxNonZero == -1) {
					idxNonZero = i;
				}
			}
			if (curChar == '.') {
				idxPoint = i;
			}
			if (idxNonZero != -1 && idxPoint != -1) {
				break;
			}
		}
		
		char big = value.charAt(idxNonZero);
		String small = value.substring(idxNonZero + 1).replace(".", "");
		small = small.length() > 2 ? small.substring(0, 3) : small;
		int e = idxPoint > idxNonZero ? idxPoint - idxNonZero - 1 : idxNonZero + 1 - idxPoint;
		String resE = e > 0 ? e + "" : "-" + e;
		String res = big + "." + small + "E" + resE;
		return res;
	}
	
	public String addPrecision(String need) {
		String[] needs = splitBigSmall(need);
		String[] values = splitBigSmall(value);
		
		// 小数相加
		String small = addTwo(values[1], needs[1]);
		String big = addTwo(values[0], needs[0]);
		char overFlowSmall = small.length() > Math.max(values[1].length(), needs[1].length()) ? small.charAt(0) : '0';
		if (overFlowSmall != '0') {
			small = small.substring(1);
			big = addTwo(big, overFlowSmall + "");
		}
		if (small == "0" || small == "") {
			value = big;
		} else {
			value = big + "." + small;
		}
		return value;
	}
	
	public String[] splitBigSmall(String need) {
		if (need == null || need.length() == 0) {
			throw new RuntimeException("ERROR INPUT");
		}
		String[] split = need.split("\\.");
		String[] res = new String[2];
		res[0] = split[0];
		res[1] = "0";
		if (split.length > 2) {
			throw new RuntimeException("ERROR INPUT");
		} else if (split.length == 2) {
			res[1] = split[1];
		}
		return res;
	}
	
	public void add(String need) {
		add(new BigInteger(need));
	}
	
	public void add(BigInteger need) {
		value = addTwo(value, need.getValue());
	}
	
	public String addTwo(String num1, String num2) {
		if (num1 == null && num2 == null) return "";
		if (num1 == null) return num2;
		if (num2 == null) return num1;
		
		int i = num1.length() - 1, j = num2.length() - 1;
		int over = 0;
		String res = "";
		while (i >= 0 || j >= 0 || over > 0) {
			int x = i >= 0 ? num1.charAt(i) - '0' : 0;
			int y = j >= 0 ? num2.charAt(j) - '0' : 0;
			int tmp = x + y + over;
			over = tmp / 10;
			res += tmp % 10;
			i--;
			j--;
		}
		return new StringBuilder(res).reverse().toString();
	}

	public BigInteger() {
		this.value = "";
	}
	
	public BigInteger(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
