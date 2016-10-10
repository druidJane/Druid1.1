package com.cn.server.module.chat.handler;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.proto.RequestProto;
import com.cn.common.core.session.Session;

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
    @SocketCommand(cmd = RequestProto.CmdType.PUBLIC_CHAT_VALUE)
    public Result<?> publicChat(Session session,byte[]data);

    @SocketCommand(cmd = RequestProto.CmdType.PRIVATE_CHAT_VALUE)
    public Result<?> privateChat(Session session,byte[]data);
}
