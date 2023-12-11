package com.learn_basic.proxy;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

class CarNoInterface {
    public void run() {
        System.out.println("car running");
    }
}

// done https://www.jianshu.com/p/d7835ab742e7

class CglibProxy implements MethodInterceptor {
    private Object car;
    /**
     * 创建代理对象
     * @param object
     * @return
     */
    public Object getInstance(Object object) {
        this.car = object;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.car.getClass());
        // 回调方法
        enhancer.setCallback(this);
        // 创建代理对象
        return enhancer.create();
    }
    
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        System.out.println("事物开始");
        proxy.invokeSuper(obj, args);
        System.out.println("事物结束");
        return null;
    }
}

public class ProxyCglibDemo {
    
    public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        CarNoInterface carNoInterface = (CarNoInterface)cglibProxy.getInstance(new CarNoInterface());
        carNoInterface.run();
    }
}
