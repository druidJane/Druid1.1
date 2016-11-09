package com.druid;

import com.cn.common.core.model.entity.Player;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by 1115 on 2016/9/23.
 */
@Component
public class ServerMain {
    private static final Logger log = Logger.getLogger(ServerMain.class.getName());
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        Server server = applicationContext.getBean(Server.class);

        RedisTemplate<String,Player> bean = applicationContext.getBean(RedisTemplate.class);
        //System.out.println(bean.boundValueOps("fff").get().getPlayerName());
        Player player = new Player();
        player.setPlayerName("fdffdfdfdf");
        for (int i=0;i<10;i++) {
            player.setPlayerId(i);
            bean.boundValueOps("fff"+i).expire(0, TimeUnit.SECONDS);
            log.info("set i="+i);
        }
        server.start();
        /*ConfigInfo cf = (ConfigInfo) applicationContext.getBean("redisConfig");
        log.info(cf.getRedisUrl());
        bean.boundValueOps("fff0").expire(0, TimeUnit.SECONDS);

       // System.out.println(bean.boundValueOps("fff").get().getPlayerName());
        RedisTemplate<String,Set<String>> setBean = applicationContext.getBean(RedisTemplate.class);
        Set<String> set = new HashSet();
        for(int i=0;i<300;i++){
            set.add(i+"ABCDEABCDEABCDEABCDEABCDEABCDEABCDE");
        }
        for(int i=0;i<100;i++){
            setBean.boundSetOps("RFID"+i).add(set);
        }*/
        try {
            ClassLoader classLoader = Server.class.getClassLoader();
            File file = new File(classLoader.getResource("redis.properties").getFile());
            FileInputStream fis = new FileInputStream(file);//属性文件流
        Properties pro = new Properties();
            pro.load(fis);
            System.out.println(pro.get("redis.host"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
