package com.sun.mapper;

import com.sun.pojo.User;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import java.util.List;


/**
 * 继承：SqlSessionDaoSupport : 可以提供SqlSessionTemplate
 */
public class UserMapperImpl2 extends SqlSessionDaoSupport implements UserMapper {


    @Override
    public List<User> selectUsers() {
        return this.getSqlSession().getMapper(UserMapper.class).selectUsers();
    }

    @Override
    public int addUser(User user) {
        return 0;
    }

    @Override
    public int deleteUser(int id) {
        return 0;
    }
}
