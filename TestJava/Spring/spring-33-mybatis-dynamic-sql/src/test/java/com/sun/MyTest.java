package com.sun;

import com.sun.mapper.BlogMapper;
import com.sun.pojo.Blog;
import com.sun.utils.IDUtils;
import com.sun.utils.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.*;

public class MyTest {

    /**
     * 为blog数据库，添加数据
     */
    @Test
    public void addBlog(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Blog blog = new Blog();
//            blog.setId(IDUtils.getId());
//            blog.setTitle("毛泽东选集1");
//            blog.setAuthor("毛泽东");
//            blog.setCreateTime(new Date());
//            blog.setViews(99);
//            mapper.addBlog(blog);
//
//            blog.setId(IDUtils.getId());
//            blog.setTitle("毛泽东选集2");
//            mapper.addBlog(blog);
//
//            blog.setId(IDUtils.getId());
//            blog.setTitle("毛泽东选集3");
//            mapper.addBlog(blog);
//
//            blog.setId(IDUtils.getId());
//            blog.setTitle("毛泽东选集4");
//            mapper.addBlog(blog);
//
//            blog.setId(IDUtils.getId());
//            blog.setTitle("苏世民经验");
//            blog.setAuthor("苏世民");
//            mapper.addBlog(blog);


            blog.setId("1");
            blog.setTitle("毛泽东选集1");
            blog.setAuthor("毛泽东");
            blog.setCreateTime(new Date());
            blog.setViews(99);
            mapper.addBlog(blog);

            blog.setId("2");
            blog.setTitle("毛泽东选集2");
            mapper.addBlog(blog);

            blog.setId("3");
            blog.setTitle("毛泽东选集3");
            mapper.addBlog(blog);

            blog.setId(IDUtils.getId());
            blog.setTitle("毛泽东选集4");
            mapper.addBlog(blog);

            blog.setId("4");
            blog.setTitle("苏世民经验");
            blog.setAuthor("苏世民");
            mapper.addBlog(blog);

            sqlSession.commit();
        }
    }

    @Test
    public void queryBlogByIf(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Map map = new HashMap();
//            map.put("author", "毛泽东");
            map.put("title", "苏世民经验");

            List<Blog> blogs = mapper.queryBlogByIf(map);
            for (Blog blog : blogs) {
                System.out.println(blog);
            }
        }
    }

    /**
     * 根据，ids是否add("1"), add("2") 来确定是否 foreach 添加参数
     */

    @Test
    public void queryByForEach(){
        try(SqlSession sqlSession = MybatisUtils.getSqlSession()){
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            Map map = new HashMap();
            ArrayList<String> ids = new ArrayList<>();
            map.put("ids", ids);

            // 可添加需要 ForEach的元素
            ids.add("1");
            ids.add("2");

            List<Blog> blogs = mapper.queryByForEach(map);
            for (Blog blog : blogs) {
                System.out.println(blog);
            }
        }
    }

}
