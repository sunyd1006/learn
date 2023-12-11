package com.learn_basic.reflection;

public class ClassLoaderTime {
    public static void main(String[] args) {
//         1 主动引用的情况
         Son a = new Son();

        // 反射也会主动引用，先加载父类，在加载之类
        // Class aClass = Class.forName("com.reflection.ClassLoaderTime");

        // 2 不会产生类的引用
        // 访问父类的 静态域
        // System.out.println(Son.b);

        // 数组定义的类，不会引发类的引用
        // Son[] son = new Son[5];

        // 常量不会引起 类的引用
        // System.out.println(Son.FINAL_N);

    }
}

class Father{
    static int b = 2;
    static {
        System.out.println("父类被加载！");
    }
}

class Son extends Father{
    static {
        System.out.println("子类被加载   ---  链接阶段执行的");
        m = 300;
    }

    static int m = 100;
    static final int FINAL_N = 999;

    public Son(){
        System.out.println("A类 无参构造 初始化");
    }
}
