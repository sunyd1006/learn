package com.learn_basic;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * https://juejin.cn/post/6844903917835419661
 * 这是一个泛型的 Demo  , 泛型方法
 */
public class GenericsDemo {
    public static void main(String[] args)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<Integer> list = new ArrayList<Integer>();

        list.add(12);
        //这里直接添加会报错
//        list.add("a");
        Class<? extends List> clazz = list.getClass();
        Method add = clazz.getDeclaredMethod("add", Object.class);
        //但是通过反射添加，是可以的
        add.invoke(list, "kl");

        System.out.println(list);
    }

    /**
     * ？ 表示不确定的 java 类型
     * T (type) 表示具体的一个java类型
     * K V (key value) 分别代表java键值中的Key Value
     * E (element) 代表Element e
     */
    private <T> void test(List<? super T> dst, List<T> src){
        for (T t : src) {
            dst.add(t);
        }
    }

    public static void mainTest() {
        List<Dog> dogs = new ArrayList<Dog>();
        List<Animal> animals = new ArrayList<Animal>();
        new GenericsDemo().test(animals,dogs);
    }
    // Dog 是 Animal 的子类
    class Dog extends Animal {
    }

    class Animal {
    }

}


