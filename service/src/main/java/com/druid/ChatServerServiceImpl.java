package com.druid;

import com.druid.module.ChatServerService;

/**
 * Created by 1115 on 2016/11/8.
 */
public class ChatServerServiceImpl implements ChatServerService{
    @Override
    public void getUserName(String fd) {
        System.out.println("Dubbo Test!!!!");
    }
}
