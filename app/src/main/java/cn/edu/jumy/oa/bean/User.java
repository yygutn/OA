package cn.edu.jumy.oa.bean;

import cn.edu.jumy.oa.timchat.model.UserInfo;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Jumy on 16/6/14 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class User extends RealmObject{
    @Index
    @PrimaryKey
    private String id;
    private String userSig;//TOKEN_From_TX
    private String avatar;//头像
    private String device_token;//设备ID
    private String level;//所属单位

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserSig() {
        return userSig;
    }

    public void setUserSig(String userSig) {
        this.userSig = userSig;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
