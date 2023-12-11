package com.sun.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private int id;
    private String name;

    // next line: 是（1个学生关联1个老师）的写法， 参考：mybatis-manyone
    // private Teacher teacher;

    private int tid;
}
