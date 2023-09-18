package com.sun.mapper;

import com.sun.pojo.Blog;

import java.util.List;
import java.util.Map;

public interface BlogMapper {
    int addBlog(Blog blog);

    List<Blog> queryBlogByIf(Map map);
    List<Blog> queryByForEach(Map map);
}
