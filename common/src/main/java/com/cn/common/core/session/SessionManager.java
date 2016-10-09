package com.cn.common.core.session;

import java.util.concurrent.ConcurrentHashMap;

/**会话管理者
 * Created by 1115 on 2016/10/9.
 */
public class SessionManager {
    private static final ConcurrentHashMap<Long,Session> onlineSessions = new ConcurrentHashMap<>();

    /**
     * 加入
     * @param playerId
     * @param channel
     * @return
     */
    public static boolean putSession(long playerId, Session session){
        if(!onlineSessions.contains(playerId)){
            boolean success = onlineSessions.putIfAbsent(playerId, session) == null ? true : false;
            return success;
        }
        return false;
    }

}
