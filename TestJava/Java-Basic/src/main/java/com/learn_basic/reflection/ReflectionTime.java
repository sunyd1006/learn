package com.learn_basic.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTime {

    public static void test01(){
        User user = new User();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            user.getName();
        }
        long endTime = System.currentTimeMillis();

        System.out.println("普通 ：" + (endTime - startTime) + "ms");
    }

    // 反射
    public static void test02() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User user = new User();
        Class c1 = user.getClass();

        Method getName = c1.getDeclaredMethod("getName", null);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            getName.invoke(user, null);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("反射 ：" + (endTime - startTime) + "ms");
    }

    // 反射 不检查权限
    public static void test03() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        User user = new User();
        Class c1 = user.getClass();

        Method getName = c1.getDeclaredMethod("getName", null);
        getName.setAccessible(true);        // 关不权限监测，就是getName如果是private，也是可以调用的
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000000; i++) {
            getName.invoke(user, null);
        }
        long endTime = System.currentTimeMillis();

        System.out.println("反射（不检查权限） ：" + (endTime - startTime) + "ms");
    }

    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        test01();
        test02();
        test03();
    }
}


// 普通 ：7ms
// 反射 ：133ms
// 反射（不检查权限） ：42ms