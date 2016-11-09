package com.druid.module.player.dao;

import com.cn.common.core.model.entity.Player;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by 1115 on 2016/10/10.
 */
@Component
public class PlayerDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    /**
     * 获取玩家通过id
     * @param playerId
     * @return
     */
    public Player getPlayerById(long playerId){
        return hibernateTemplate.get(Player.class, playerId);
    }
    /**
     * 获取玩家通过玩家名
     * @param playerName
     * @return
     */
    public Player getPlayerByName(final String username){
        return hibernateTemplate.execute(new HibernateCallback<Player>() {
            @Override
            public Player doInHibernate(Session session) throws HibernateException, SQLException {

                SQLQuery query = session.createSQLQuery("select * from player where playername = ?");
                query.setString(0,username);
                query.addEntity(Player.class);
                List<Player> list = query.list();
                if(list!=null&&list.size()>0){
                    return list.get(0);
                }
                return null;
            }
        });
    }
    /**
     * 创建玩家
     * @param player
     * @return
     */
    public Player createPlayer(Player player){
        long playerId = (Long) hibernateTemplate.save(player);
        player.setPlayerId(playerId);
        return player;
    }
}
