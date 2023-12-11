package com.sun.log;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class BeforeLog implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("[before] "+target.getClass().getName()+" 的 " + method.getName() + " 被执行！");
    }
}
