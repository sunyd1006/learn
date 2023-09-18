package com.sun.mapper;

import com.sun.pojo.User;
import com.sun.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMapper {

    static Logger logger = Logger.getLogger(TestMapper.class);

    @Test
    public void testSqlSessionNoFlushCache(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User userById = mapper.getUserById(5);
            System.out.println(userById);

            System.out.println("============= 测试对 用户1 是否用缓存 =======");
            User userById1 = mapper.getUserById(5);
            System.out.println(userById1);

            // 只调用1次SQL
        }
    }


    /**
     * // 调用2次SQL
     */
    @Test
    public void testSqlSessionFlushCache(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User userById = mapper.getUserById(5);
            System.out.println(userById);

            System.out.println("============= 测试 更改改变 改变缓存 =======");
            int res = mapper.updateUserById(new User(5,"new user name" , "12354"));
            assert res>0: "更改不成功！";
        }
    }


}
