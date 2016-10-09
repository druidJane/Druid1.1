package com.cn.server.module.chat;

import com.cn.common.core.model.Result;

/**
 * Created by 1115 on 2016/10/9.
 */
public class ChatHandlerImpl implements ChatHandler {
    @Override
    public Result<?> publicChat(byte[] data) {

        return Result.SUCCESS();
    }
}
