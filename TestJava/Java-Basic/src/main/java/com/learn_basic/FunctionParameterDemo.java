package com.learn_basic;

import java.util.ArrayList;
import java.util.List;

public class FunctionParameterDemo {
	public static void main(String[] args) {
		Inner inner = new Inner(111);
		Mid mid = new Mid(222, inner);
		Outer outer = new Outer(333, mid);
		List<Integer> list = new ArrayList<>();
		
		System.out.println(outer); // 原始值 Outer{value=333, obj=Mid{value=222, obj=Inner{value=111}}}
		modifyXingParam(list, outer); // 修改引用类型变量
		for (int a : list) {
			System.out.println(a); // 1111  2222
		}
		System.out.println(outer); // 看看新值 Outer{value=444, obj=Mid{value=555, obj=Inner{value=555}}}
	}
	
	// 形参是引用类型时
	public static void modifyXingParam(List<Integer> list, Outer outer) { // 当形参是引用类型的时候, 传递的是地址，对形参进行修改会直接修改形参指向的地址的内容
		list.add(1111);
		list.add(2222);
		outer.value = 444;
		
		Mid mid = new Mid(555, new Inner(555));
		outer.obj = mid;
	}
	
	static class Outer {
		int value;
		Mid obj;
		
		public Outer(int value, Mid obj) {
			this.value = value;
			this.obj = obj;
		}
		
		@Override
		public String toString() {
			return "Outer{" +
					"value=" + value +
					", obj=" + obj.toString() +
					'}';
		}
	}
	
	static class Mid {
		int value;
		Inner obj;
		
		public Mid(int value, Inner obj) {
			this.value = value;
			this.obj = obj;
		}
		
		@Override
		public String toString() {
			return "Mid{" +
					"value=" + value +
					", obj=" + obj +
					'}';
		}
	}
	
	static class Inner {
		int value;
		
		public Inner(int value) {
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "Inner{" +
					"value=" + value +
					'}';
		}
	}
}