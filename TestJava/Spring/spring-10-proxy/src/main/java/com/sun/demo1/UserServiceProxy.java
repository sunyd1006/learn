package com.sun.demo1;

public class UserServiceProxy {
    private UserServiceImpl userServiceImpl;

    public UserServiceImpl getUserServiceImpl() {
        return userServiceImpl;
    }

    public void setUserServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public void add(){
        log("add");
        userServiceImpl.add();
    }

    public void delete(){
        log("delete");
        userServiceImpl.delete();
    }

    public void log(String msg){
        System.out.println("[Log]：" + msg + "方法");
    }
}
