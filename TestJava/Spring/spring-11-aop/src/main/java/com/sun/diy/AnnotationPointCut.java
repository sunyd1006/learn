package com.sun.diy;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class AnnotationPointCut {

    @Before("execution(* com.sun.service.UserServiceImpl.*(..))")
    public void before(){
        System.out.println("========== [before] ========");
    }
}
