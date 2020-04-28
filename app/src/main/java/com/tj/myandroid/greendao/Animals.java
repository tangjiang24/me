package com.tj.myandroid.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
@Entity
public class Animals implements Serializable {
    public static final long serialVersionUID = 10l;
    @Id
    private String id;
    private String name;
    private int hands;
    private boolean eatRou;
    @Generated(hash = 1467259076)
    public Animals(String id, String name, int hands, boolean eatRou) {
        this.id = id;
        this.name = name;
        this.hands = hands;
        this.eatRou = eatRou;
    }
    @Generated(hash = 1996800341)
    public Animals() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getHands() {
        return this.hands;
    }
    public void setHands(int hands) {
        this.hands = hands;
    }
    public boolean getEatRou() {
        return this.eatRou;
    }
    public void setEatRou(boolean eatRou) {
        this.eatRou = eatRou;
    }

}
