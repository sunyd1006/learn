package com.learn_basic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

interface Speaker {
    public void speak();
}

/**
 * 被代理对象：
 */
class SpeakerImpl implements Speaker {
    
    @Override
    public void speak() {
        System.out.println("我受伤害了！");
    }
}


/**
 * 静态代理：一个代理类只能代理一个
 */
class StaticLayer implements Speaker {
    SpeakerImpl speakerImpl = new SpeakerImpl();
    
    @Override
    public void speak() {
        System.out.println("StaticLayer 补充说明（增强）****** !");
        speakerImpl.speak();
        System.out.println("StaticLayer 补充说明（增强）****** !");
        System.out.println();
    }
}

/**
 * 动态代理：
 * 1. 一个代理类，可以代理很多个被代理的对象
 * 2. 可以在运行时确定代理的是谁
 * <p>
 * JDK 代理的限制：
 * 1. 被代理类需要实现接口
 */
class JDKProxyLayer implements InvocationHandler {
    Object obj;
    
    JDKProxyLayer(Object obj) {
        this.obj = obj;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 该代理可以代理很多个方法，故而要判断
        if (method.getName().equals("speak")) {
            System.out.println("JDKproxyLayer 补充说明（增强）****** !");
            method.invoke(obj, args);
            System.out.println("JDKproxyLayer 补充说明（增强）****** !");
            System.out.println("");
        }
        return null;
    }
    
}


/**
 * 动态代理：
 * 1. 一个代理类，可以代理很多个被代理的对象
 * 2. 可以在运行时确定代理的是谁
 * <p>
 * CGLib 代理的限制：(目前不知道）
 * 1. 可以不实现接口
 */
class ManNoInterface {
    public void speak(){
        System.out.println("Man no Interface 我受伤了");
    }
}
class CGlibProxyLayer implements MethodInterceptor {
    Object obj;
    
    CGlibProxyLayer(Object obj) {
        this.obj = obj;
    }
    
    @Override
    public Object intercept(Object proxy, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // objects = args
        if (method.getName().equals("speak")) {
            System.out.println("CGlibProxyLayer 补充说明（增强）****** !");
            method.invoke(obj, objects);
            System.out.println("CGlibProxyLayer 补充说明（增强）****** !");
            System.out.println("");
        }
        return null;
    }
}

// 参考：https://www.bilibili.com/video/BV1cz41187Dk?from=search&seid=11956165488692565583
public class ProxyDemo {
    public static void main(String[] args) {
        // 静态代理
        Speaker speaker = new StaticLayer();
        speaker.speak();
        
        // JDKProxy
        SpeakerImpl speakerImpl = new SpeakerImpl();
        JDKProxyLayer jdkProxyLayer = new JDKProxyLayer(speakerImpl);
        // 注意是：import java.lang.reflect.Proxy;
        // new Class[]{Speaker.class} = speakerImpl.getClass().getInterfaces()
        Speaker jdkProxy = (Speaker) Proxy.newProxyInstance(ProxyDemo.class.getClassLoader(), new Class[]{Speaker.class}, jdkProxyLayer);
        jdkProxy.speak();

        // CGlib
        CGlibProxyLayer cglibProxyLayer = new CGlibProxyLayer(new ManNoInterface());
        ManNoInterface cglibProxy = (ManNoInterface) Enhancer.create(ManNoInterface.class, cglibProxyLayer);
        cglibProxy.speak();
    }
}
