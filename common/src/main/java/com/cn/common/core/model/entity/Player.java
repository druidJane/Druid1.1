package com.cn.common.core.model.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by 1115 on 2016/10/9.
 */
@Entity
@Table(name = "player")
public class Player implements Serializable{
    private static final long serialVersionUID = 6214790243416807050L;
    /**
     * 玩家id
     */
    @Id
    @GeneratedValue
    private long playerId;

    private String playerName;

    private String psw;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }
}
