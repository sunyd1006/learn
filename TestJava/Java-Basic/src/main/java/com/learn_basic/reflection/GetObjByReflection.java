package com.learn_basic.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GetObjByReflection {
	public static void main(String[] args) throws Exception{
		Class<?> aClass = Class.forName("com.learn_basic.reflection.User");
		
		// 方法1：通过 Class.forName加载类对象， 在用类对象调用构造方法去 new 一个对象
		User user = (User) aClass.newInstance(); // 隐式调用无参构造
		System.out.println(user);
	
		// 方法2： 显示调用有参构造
		Constructor<?> declaredConstructor = aClass.getDeclaredConstructor(String.class, int.class);
		User qinJang = (User) declaredConstructor.newInstance("QinJiang", 001);
		System.out.println(qinJang);
		
		// 通过反射调用方法
		Method setName = aClass.getDeclaredMethod("setName", String.class);
		// 等价于 qinJang.setName("NewQingJiang");
		setName.invoke(qinJang, "NewQingJiang");
		System.out.println(qinJang.getName());
	
		// 通过反射操作属性
		// 不能直接操作私有属性，要先关闭安全检测
		Field id = aClass.getDeclaredField("id");
		id.setAccessible(true);   // 设置属性
		id.set(qinJang, 8888888);
		System.out.println(qinJang.getId());
	}
}