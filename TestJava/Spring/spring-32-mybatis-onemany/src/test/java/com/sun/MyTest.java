package com.sun;

import com.sun.mapper.StudentMapper;
import com.sun.mapper.TeacherMapper;
import com.sun.pojo.Student;
import com.sun.pojo.Teacher;
import com.sun.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class MyTest {

    @Test
    public void getTeacher(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
            Teacher teacher = mapper.getTeacher(1);
            System.out.println(teacher);
        }
    }

}
