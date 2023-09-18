package com.sun.demo1;

public class Client {
    public static void main(String[] args) {

//        System.out.println("hahah");
        UserServiceImpl userService = new UserServiceImpl();
//      没用代理： 要更改userService的原代码
        userService.add();

//        用静态代理：就不必更改 userServiceImpl的原代码
        UserServiceProxy userServiceProxy = new UserServiceProxy();
        userServiceProxy.setUserServiceImpl(userService);
        userServiceProxy.add();
    }
}
