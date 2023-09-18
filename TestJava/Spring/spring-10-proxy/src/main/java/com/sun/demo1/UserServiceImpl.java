package com.sun.demo1;

import com.sun.demo1.UserService;

public class UserServiceImpl implements UserService {
    public void  add(){
        System.out.println("origin: add function");
    }

    public void delete(){
        System.out.println("origin: delete function");
    }

    public void update(){
        System.out.println("origin: update function");
    }

    public void query(){
        System.out.println("origin: query function");
    }
}
