package com.sun;

import com.sun.mapper.UserMapper;
import com.sun.pojo.User;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class MyTest {

    @Test
    public void test(){
        System.out.println("test");
    }

    @Test
    public void testUserMapperImpl1(){
        ApplicationContext contenxt = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper = contenxt.getBean("UserMapper1", UserMapper.class);
        List<User> users = userMapper.selectUsers();
        for (User user : users) {
            System.out.println(user);
        }
    }
}
