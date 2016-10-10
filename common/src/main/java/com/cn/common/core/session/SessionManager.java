package com.cn.common.core.session;

import com.google.protobuf.GeneratedMessage;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**会话管理者
 * Created by 1115 on 2016/10/9.
 */
public class SessionManager {
    private static final ConcurrentHashMap<Long,Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 加入
     * @param playerId
     * @return
     */
    public static boolean putSession(long playerId, Session session){
        if(!onlineSessions.contains(playerId)){
            boolean success = onlineSessions.putIfAbsent(playerId, session) == null ? true : false;
            return success;
        }
        return false;
    }
    public static <T extends GeneratedMessage> void sendMessage(Long playerId,T msg){
        Session session = onlineSessions.get(playerId);
        if(session!=null&&session.isConnected()){
            BinaryWebSocketFrame frame = new BinaryWebSocketFrame(Unpooled.wrappedBuffer(msg.toByteArray()));
            session.write(frame);
        }
    }
    public static Set<Long> getOnlinePlayer(){
        return Collections.unmodifiableSet(onlineSessions.keySet());
    }

    /**
     * 是否在线
     * @param playerId
     * @return
     */
    public static boolean isOnlinePlayer(Long playerId){
        return onlineSessions.containsKey(playerId);
    }
    /**
     * 移除
     * @param playerId
     */
    public static Session removeSession(Long playerId){
        return onlineSessions.remove(playerId);
    }
}
