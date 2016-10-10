package com.cn.server.module.player.service;

import com.cn.common.core.exception.ErrorCodeException;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.entity.Player;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;
import com.cn.common.core.session.SessionManager;
import com.cn.server.module.player.dao.PlayerDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by 1115 on 2016/10/9.
 */
@Component
public class PlayerServiceImpl implements PlayerService {
    private static final Logger log = Logger.getLogger(PlayerServiceImpl.class.getName());
    @Autowired
    private PlayerDao playerDao;
    @Override
    public ResponseProto.Login login(Session session, String playerName, String password) {
        // 判断当前会话是否已登录
        if(session.getAttachment()!=null){
            throw new ErrorCodeException(ResultCode.HAS_LOGIN);
        }

        if(StringUtils.isEmpty(playerName)||StringUtils.isEmpty(password)){
            throw new ErrorCodeException(ResultCode.PLAYERNAME_NULL);
        }
        Player existplayer = playerDao.getPlayerByName(playerName);
        // 玩家不存在
        if(existplayer==null){
            throw new ErrorCodeException(ResultCode.PLAYER_NO_EXIST);
        }
        if(!password.equals(existplayer.getPsw())){
            throw new ErrorCodeException(ResultCode.PASSWARD_ERROR);
        }
        // 判断是否在其他地方登录过
        if (SessionManager.isOnlinePlayer(existplayer.getPlayerId())) {
            Session oldSession = SessionManager.removeSession(existplayer.getPlayerId());
            oldSession.removeAttachment();
            oldSession.close();
        }
        //加入在线玩家会话
        if(SessionManager.putSession(existplayer.getPlayerId(),session)){
            session.setAttachment(existplayer);
        }else{
            throw new ErrorCodeException(ResultCode.LOGIN_FAIL);
        }
        ResponseProto.Login.Builder resLogin = ResponseProto.Login.newBuilder();
        //TODO 生成token
        resLogin.setToken("token");
        return resLogin.build();
    }

    @Override
    public ResponseProto.Register register(Session session, String username, String password) {
        if(StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
            throw new ErrorCodeException(ResultCode.PLAYERNAME_NULL);
        }
        Player player = playerDao.getPlayerByName(username);
        if(player!=null){
            throw new ErrorCodeException(ResultCode.PLAYER_EXIST);
        }
        player = new Player();
        player.setPlayerName(username);
        player.setPsw(password);
        player = playerDao.createPlayer(player);
        //加入在线玩家回话
        if(SessionManager.putSession(player.getPlayerId(),session)){
            session.setAttachment(player);
        }else{
            throw new ErrorCodeException(ResultCode.HAS_LOGIN);
        }
        ResponseProto.Register.Builder resRegister = ResponseProto.Register.newBuilder();
        //TODO 生成token
        resRegister.setToken("token");
        return resRegister.build();
    }
}
