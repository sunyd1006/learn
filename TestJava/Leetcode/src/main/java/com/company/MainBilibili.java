package com.company;

import java.io.FileNotFoundException;

// B站  驼峰 转换
public class MainBilibili {
	public static void main(String[] args) throws FileNotFoundException {
		// Scanner sc = new Scanner(new File("/Users/sunyindong/workspace/TestJava/Leetcode/src/main/resources/input.txt"));
		// Scanner sc = new Scanner(System.in);
		// String s = sc.nextLine();
		// String s = "hello_world";
		// String s = "This is a Demo!";
		String s = " tHis Is !sun yi!ng dong! h! ";
		
		if (s.length() == 0) {
			return;
		}
		StringBuffer stringBuffer = new StringBuffer();
		char temp = s.charAt(0);
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (!Character.isLetterOrDigit(c)) {
				temp = c;
				continue;
			}
			if (stringBuffer.length() == 0) {
				if (Character.isLetter(c)) {
					stringBuffer.append(Character.toLowerCase(c));
				} else if (Character.isDigit(c)) {
					stringBuffer.append(c);
				}
				temp = c;
				continue;
			}
			if (Character.isDigit(c)) {
				stringBuffer.append(c);
				temp = c;
				continue;
			}
			if (Character.isLetter(temp)) {
				stringBuffer.append(Character.toLowerCase(c));
			} else if (Character.isDigit(temp)) {
				stringBuffer.append(Character.toUpperCase(c));
			} else {
				if (Character.isLetter(c)) {
					stringBuffer.append(Character.toUpperCase(c));
				} else {
					System.out.println("不可能");
				}
			}
			temp = c;
		}
		System.out.println(stringBuffer);
	}
	
}

// ----------------
// if(i==0 && (Character.isLowerCase(c)) || Character.isUpperCase(c)){
// 	stringBuffer.append(Character.toLowerCase(c));
// 	continue;
// }else  if(i==0 && Character.isDigit(c)){
// 	stringBuffer.append(c);
// 	continue;
// }else if(i==0 && !Character.isLetterOrDigit(c)){
// 	continue;
// }

// if(!Character.isLetterOrDigit(temp) && (Character.isLowerCase(c) || Character.isUpperCase(c))){
// 	stringBuffer.append(Character.toUpperCase(c));
// }else if(Character.isLetterOrDigit(c)){
// 	stringBuffer.append(Character.toLowerCase(c));
// }
// temp = c;