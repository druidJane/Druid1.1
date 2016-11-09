package com.druid.module.player.handler;

import com.cn.common.core.annotion.SocketCommand;
import com.cn.common.core.annotion.SocketModule;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.proto.RequestProto;
import com.cn.common.core.session.Session;

/**
 * Created by 1115 on 2016/10/9.
 */
@SocketModule(module = RequestProto.ModuleType.PLAYER_VALUE)
public interface PlayerHandler {
    @SocketCommand(cmd = RequestProto.CmdType.LOGIN_VALUE)
    public Result<byte[]> login(Session session, byte[] data);
    @SocketCommand(cmd = RequestProto.CmdType.REGISTER_AND_LOGIN_VALUE)
    public Result<byte[]> register(Session session, byte[] data);
}
