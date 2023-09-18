package com.learn_basic.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class GetObjClassInfoByReflection {
	public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {
		Class c1 = Class.forName("com.learn_basic.reflection.User");
		System.out.println(c1.getName());
		System.out.println(c1.getSimpleName());
		
		Field[] fields = c1.getFields();  // 获取 public field
		printGenerics(fields);
		
		fields = c1.getDeclaredFields();  // 获取全部属性， public protected private
		printGenerics(fields);
		
		Field name = c1.getDeclaredField("name");
		printGenerics(name);
		System.out.println();
		
		Method[] methods = c1.getMethods(); // 返回public 包含超类继承的方法
		printGenerics(methods);
		
		methods = c1.getDeclaredMethods(); // 返回自定义的方法
		printGenerics(methods);
		
		Constructor[] constructors = c1.getConstructors();
		printGenerics(constructors);
		
		constructors = c1.getDeclaredConstructors();
		printGenerics(constructors);
		
		Constructor declaredConstructor = c1.getDeclaredConstructor(String.class, int.class);
		printGenerics(declaredConstructor);
	}
	
	public static <T>  void printGenerics(T name){
		Class clsObj = name.getClass();
		System.out.println(clsObj.getSimpleName() + " " + name);
	}
	
	public static <T> void printGenerics(T[] name){
		for(T item: name){
			printGenerics(item);
		}
		System.out.println();
	}
}