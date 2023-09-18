package com.sun;

import com.sun.pojo.Student;
import com.sun.pojo.Teacher;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;
import com.sun.mapper.StudentMapper;
import com.sun.mapper.TeacherMapper;
import com.sun.utils.MybatisUtils;

import java.util.List;

public class MyTest {

    @Test
    public void mytest(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
//            List<Student> students = mapper.getStudents();
//            for (Student student : students) {
//                System.out.println(student);
//            }

            Teacher teacher = mapper.getTeacher(1);
            System.out.println(teacher);
        }
    }

    @Test
    public void getStudents(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> students = mapper.getStudents();
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }

    @Test
    public void getStudents2(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            StudentMapper mapper = sqlSession.getMapper(StudentMapper.class);
            List<Student> students = mapper.getStudents2();
            for (Student student : students) {
                System.out.println(student);
            }
        }
    }
}
