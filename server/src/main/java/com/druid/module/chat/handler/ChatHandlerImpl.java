package com.druid.module.chat.handler;

import com.cn.common.core.exception.ErrorCodeException;
import com.cn.common.core.model.Result;
import com.cn.common.core.model.ResultCode;
import com.cn.common.core.model.entity.Player;
import com.cn.common.core.model.proto.RequestProto;
import com.cn.common.core.model.proto.ResponseProto;
import com.cn.common.core.session.Session;
import com.cn.common.core.session.SessionManager;
import com.druid.module.player.dao.PlayerDao;
import com.google.protobuf.InvalidProtocolBufferException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Created by 1115 on 2016/10/9.
 */
@Component
public class ChatHandlerImpl implements ChatHandler {
    @Autowired
    private PlayerDao playerDao;
    @Override
    public Result<?> publicChat(Session session,byte[]data) {
        Object attachment = session.getAttachment();
        if(attachment==null){
            return Result.ERROR(ResultCode.LOGIN_PLEASE);
        }
        Player player = (Player) attachment;

        ResponseProto.Message result = null;
        try {
            RequestProto.Message.Builder reqMessage = RequestProto.Message.newBuilder();
            reqMessage.mergeFrom(data);
            //获取所有在线玩家
            Set<Long> onlinePlayers = SessionManager.getOnlinePlayer();
            ResponseProto.Message message= ResponseProto.Message.newBuilder()
                    .setMsg(reqMessage.getMsg())
                    .setFromUser(player.getPlayerName())
                    .setGroup(reqMessage.getGroup())
                    .build();
            ResponseProto.Rsp responseMsg = ResponseProto.Rsp.newBuilder()
                    .setCmd(ResponseProto.CmdType.PUBLIC_CHAT)
                    .setModule(ResponseProto.ModuleType.CHAT)
                    .setData(message.toByteString())
                    .setResultCode(ResultCode.SUCCESS)
                    .build();
            //发送消息
            for(long targetPlayerId : onlinePlayers){
                SessionManager.sendMessage(targetPlayerId,responseMsg);
            }
        } catch (ErrorCodeException e) {
            return Result.ERROR(e.getErrorCode());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ERROR(ResultCode.UNKOWN_EXCEPTION);
        }
        return Result.SUCCESS();
    }

    @Override
    public Result<?> privateChat(Session session, byte[] data) {
        Object attachment = session.getAttachment();
        if(attachment==null){
            return Result.ERROR(ResultCode.LOGIN_PLEASE);
        }
        Player player = (Player) attachment;
        try {
            RequestProto.Message.Builder reqMessage = RequestProto.Message.newBuilder();
            reqMessage.mergeFrom(data);
            //不能和自己私聊
            if(reqMessage.getToUser().equals(player.getPlayerName())){
                return Result.ERROR(ResultCode.CAN_CHAT_YOUSELF);
            }
            //判断目标是否存在
            Player toPlayer = playerDao.getPlayerByName(reqMessage.getToUser());
            if(toPlayer==null){
                return Result.ERROR(ResultCode.PLAYER_NO_EXIST);
            }
            //判断对方是否在线
            if(!SessionManager.isOnlinePlayer(toPlayer.getPlayerId())){
                return Result.ERROR(ResultCode.PLAYER_NO_ONLINE);
            }
            //创建消息对象
            ResponseProto.Message message= ResponseProto.Message.newBuilder()
                    .setMsg(reqMessage.getMsg())
                    .setFromUser(player.getPlayerName())
                    .setGroup(reqMessage.getGroup())
                    .build();
            ResponseProto.Rsp responseMsg = ResponseProto.Rsp.newBuilder()
                    .setCmd(ResponseProto.CmdType.PRIVATE_CHAT)
                    .setModule(ResponseProto.ModuleType.CHAT)
                    .setData(message.toByteString())
                    .setResultCode(ResultCode.SUCCESS)
                    .build();
            SessionManager.sendMessage(toPlayer.getPlayerId(),responseMsg);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return Result.SUCCESS();
    }
}
