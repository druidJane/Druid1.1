package com.cn.server.module.player.service;

import com.cn.common.core.exception.ErrorCodeException;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.entity.Player;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;
import com.cn.common.core.session.SessionManager;
import org.springframework.stereotype.Component;

/**
 * Created by 1115 on 2016/10/9.
 */
@Component
public class PlayerServiceImpl implements PlayerService {
    @Override
    public ResponseProto.Login login(Session session, String playerName, String passward) {
        Player player = new Player();
        player.setPlayerId(1l);
        //加入在线玩家回话
        if(SessionManager.putSession(1,session)){
            session.setAttachment(player);
        }else{
            throw new ErrorCodeException(ResultCode.LOGIN_FAIL);
        }
        ResponseProto.Login.Builder resLogin = ResponseProto.Login.newBuilder();
        resLogin.setToken("token");
        return resLogin.build();
    }
}
