package com.druid.module.player.service;

import com.cn.common.core.exception.ErrorCodeException;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.entity.Player;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;
import com.cn.common.core.session.SessionManager;
import com.druid.module.player.dao.PlayerDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            //创建消息对象
            ResponseProto.Message message= ResponseProto.Message.newBuilder()
                    .setMsg("您的账号在异地登录")
                    .setFromUser("系统")
                    .setGroup("")
                    .build();
            ResponseProto.Rsp responseMsg = ResponseProto.Rsp.newBuilder()
                    .setCmd(ResponseProto.CmdType.PRIVATE_CHAT)
                    .setModule(ResponseProto.ModuleType.CHAT)
                    .setData(message.toByteString())
                    .setResultCode(ResultCode.SUCCESS)
                    .build();
            SessionManager.sendMessage(existplayer.getPlayerId(),responseMsg);
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
