package com.cn.server;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by 1115 on 2016/9/23.
 */
public class ServerMain {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Server server = applicationContext.getBean(Server.class);
        RedisTemplate<String,String> bean = applicationContext.getBean(RedisTemplate.class);
        System.out.println(bean.boundValueOps("test").get());
        bean.boundValueOps("fff").set("fd");
        //server.start();
    }
}
