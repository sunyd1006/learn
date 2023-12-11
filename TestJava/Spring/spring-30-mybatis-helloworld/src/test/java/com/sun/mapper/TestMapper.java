package com.sun.mapper;

import com.sun.pojo.User;
import com.sun.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMapper {

    @Test
    public void test(){
        System.out.println("======== TEST ========");
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            List<User> userList = mapper.getUserList();
            for (User user : userList) {
                System.out.println(user);
            }
        }
    }

    @Test
    public void addUser(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int res = mapper.addUser(new User(6,"sunyd", "abc"));
            assert res > 0: "插入不成功";

            // 增删改一定要 commit, commit!!!!!!!!!!!!!!!!!!
            sqlSession.commit();
        }
    }

    @Test
    public void addUserByMap(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> map = new HashMap<>();
            map.put("myid", 9999);
            map.put("myname", "this is name");

            int res = mapper.addUserByMap(map);
            assert res > 0: "插入不成功";

            // 增删改一定要 commit, commit!!!!!!!!!!!!!!!!!!
            sqlSession.commit();
        }
    }

    @Test
    public void updateUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int res = mapper.updateUserById(new User(6, "sunyd_new", "new pws"));
            assert res > 0: "更新不成功";

            // 增删改一定要 commit, commit!!!!!!!!!!!!!!!!!!
            sqlSession.commit();
        }
    }

    @Test
    public void deleteUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            int res = mapper.deleteUserById(9999);
            assert res > 0: "删除不成功";

            // 增删改一定要 commit, commit!!!!!!!!!!!!!!!!!!
            sqlSession.commit();
        }
    }

    @Test
    public void getUserById(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            UserMapper mapper = sqlSession.getMapper(UserMapper.class);
            User userById = mapper.getUserById(88);
            System.out.println(userById);
        }
    }
}
