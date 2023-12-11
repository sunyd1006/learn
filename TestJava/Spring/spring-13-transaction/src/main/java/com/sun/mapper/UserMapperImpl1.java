package com.sun.mapper;

import com.sun.pojo.User;
import org.mybatis.spring.SqlSessionTemplate;

import java.util.List;

public class UserMapperImpl1 implements UserMapper {

    private SqlSessionTemplate sqlSessionTemplate;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    @Override
    public List<User> selectUsers() {
        User user = new User(88, "孙测试插入", "123456");
        UserMapper mapper = sqlSessionTemplate.getMapper(UserMapper.class);
        mapper.addUser(user);
        mapper.deleteUser(88);
        return mapper.selectUsers();
    }

    @Override
    public int addUser(User user) {
        return sqlSessionTemplate.getMapper(UserMapper.class).addUser(user);
    }

    @Override
    public int deleteUser(int id) {
        return sqlSessionTemplate.getMapper(UserMapper.class).deleteUser(id);
    }


}
