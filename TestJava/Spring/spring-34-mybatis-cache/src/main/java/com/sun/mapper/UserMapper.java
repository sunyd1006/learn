package com.sun.mapper;

import com.sun.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
//    List<User> getUserList();
////    int addUser(User user);
////    int addUserByMap(Map<String, Object> map);
////    int deleteUserById(int id);
////    int updateUserById(User user);
    User getUserById(int id);
    List<User> getUserByLimit(Map<String, Integer> map);
    int updateUserById(User user);
}
