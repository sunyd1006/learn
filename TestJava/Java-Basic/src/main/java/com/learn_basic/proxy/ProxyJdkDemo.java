package com.learn_basic.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

interface Car {
    public void run();
}

class CarImpl implements Car {
    public void run() {
        System.out.println("car running");
    }
}

class CarHandler implements InvocationHandler {
    //真实类的对象
    private Object car;
    
    //构造方法赋值给真实的类
    public CarHandler(Object obj) {
        this.car = obj;
    }
    
    //代理类执行方法时，调用的是这个方法
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("before");
        Object res = method.invoke(car, args);
        System.out.println("after");
        return res;
    }
}

// https://cloud.tencent.com/developer/article/1461796

/**
 * JDK的动态代理依靠接口实现，入参必须有被代理类的接口，也就是 carImpl.getClass().getInterfaces()
 * 如果有些类并没有实现接口，则不能使用JDK代理，这就要使用cglib动态代理了
 * <p>
 * 作者：激情的狼王
 * 链接：https://www.jianshu.com/p/89135092accb
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class ProxyJdkDemo {
    public static void main(String[] args) {
        CarImpl carImpl = new CarImpl();
        CarHandler carHandler = new CarHandler(carImpl);
        
        // System.out.println(carImpl.getClass().getInterfaces().toString());
        
        Car proxy = (Car) Proxy.newProxyInstance(
                ProxyJdkDemo.class.getClassLoader(), //第一个参数，获取ClassLoader
                carImpl.getClass().getInterfaces(), //第二个参数，获取被代理类的接口
                carHandler);                        //第三个参数，一个InvocationHandler对象，表示的是当我这个动态代理对象在调用方法的时候，会关联到哪一个InvocationHandler对象上
        proxy.run();
    }
}
