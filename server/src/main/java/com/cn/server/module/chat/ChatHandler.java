package com.cn.server.module.chat;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.proto.RequestProto;

/**
 * Created by 1115 on 2016/10/9.
 */
@SocketModule(module = RequestProto.ModuleType.CHAT_VALUE)
public interface ChatHandler {
    /**
     * 广播消息
     * @param playerId
     * @param data {@link PublicChatRequest}
     * @return
     */
    @SocketCommand(cmd = RequestProto.CmdType.LOGIN_VALUE)
    public Result<?> publicChat(byte[] data);
}
