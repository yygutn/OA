package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/14 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class User extends DataSupport implements Serializable {
    private String id = "";
    private String username = "";
    /**
     * 单位id
     */
    private String nickname = "";
    private String avatar = "";//头像
    /**
     * 特殊
     */
    private String device_token = "";//设备ID
    /**
     * 单位id
     */
    private String oid = "";
    /**
     * 电话
     */
    private String phone;
    /**
     * 权限等级(0:普通人员   1:平台管理人员  2:单位管理员)
     */
    private Integer level;

    // 额外属性

    private String orderBy;
    private List<Object> attachmentList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", nickname='" + nickname + '\'' +
                ", avatar='" + avatar + '\'' +
                ", device_token='" + device_token + '\'' +
                ", oid='" + oid + '\'' +
                '}';
    }
}
