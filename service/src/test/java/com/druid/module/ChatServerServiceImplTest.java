package com.druid.module;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by 1115 on 2016/11/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath*:META-INF/spring/spring-module-mall.xml")
public class ChatServerServiceImplTest {
    @Resource
    ChatServerService chatServerService;

    @Test
    public void testGetName(){
        chatServerService.getUserName("fd");
    }
}
