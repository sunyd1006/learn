package com.sun.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.sun.pojo.Teacher;
import java.util.List;

public interface TeacherMapper {
    @Select("select * from teacher where id=#{tid}")
    public Teacher getTeacher(@Param("tid") int id);

    public List<Teacher> getTeachers();
}
