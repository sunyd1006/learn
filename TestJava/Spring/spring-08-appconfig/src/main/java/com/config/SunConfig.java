package com.config;

import com.pojo.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @Configuraiton: 开题配置类
 * @ConponentScan: 开启包注解
 * @Import(SunConfig2.class) 导入其他注解
 */
@Configuration
@ComponentScan("com.pojo")
//@Import(SunConfig2.class)
public class SunConfig {

    /**
     * @Bean: 等价于 <bean id="getUser" class="com.pojo.User" />
     */
    @Bean
    public User getUser(){
        return  new User();
    }
}
