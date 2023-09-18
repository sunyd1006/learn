package com.sun.demo2;


// done merge to Java-Basic
public class Client {

    public static void main(String[] args) {
        Host host = new Host();

        // 1. 每个 代理调用程序：可以动态返回代理类、动态代理方法
        ProxyInvocationHandler proxyInvocationHandler = new ProxyInvocationHandler();

        // 2. ProxyInvacationHandler 需要代理 1个对象
        proxyInvocationHandler.setTarget(host);

        // 3. 获取 代理的时一个接口
        Rent proxy = (Rent) proxyInvocationHandler.getProxy();

        // 4. 执行方法。 proxyInvocationHandler invoke 调用相应的方法
        proxy.rent();
    }
}
