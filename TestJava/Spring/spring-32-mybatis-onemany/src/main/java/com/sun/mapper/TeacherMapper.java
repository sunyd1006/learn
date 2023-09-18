package com.sun.mapper;

import com.sun.pojo.Teacher;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {
    public Teacher getTeacher(int tid);
}
