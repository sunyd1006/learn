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
    public void getUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User userById = mapper.getUserById(5);
            System.out.println(userById);
        }
    }

    @Test
    public void testLog4j(){
        logger.info("进入了 testLog4j ");
        logger.debug("进入了 testLog4j ");
        logger.error("进入了 testLog4j ");
    }

    @Test
    public void getUserByLimit(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            Map<String,Integer> map = new HashMap<>();
            map.put("startIndex", 1);
            map.put("pageSize", 3);
            List<User> listUser = mapper.getUserByLimit(map);
            for (User user : listUser) {
                System.out.println(user);
            }
        }
    }
}
