package com.cn.server.module.player.service;

import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;

/**
 * Created by 1115 on 2016/10/9.
 */
public interface PlayerService{
    /**
     * 登录
     * @param playerName
     * @param passward
     * @return
     */
    public ResponseProto.Login login(Session session, String playerName, String passward);

    public ResponseProto.Register register(Session session, String username, String password);
}
