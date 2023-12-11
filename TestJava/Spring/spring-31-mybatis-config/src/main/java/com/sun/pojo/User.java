package com.sun.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


//@Alias("UserAliasByAnnotation")

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String name;

    // 和数据库 不一致
    private String password;
}
