package cn.edu.jumy.oa.bean;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hyphenate.easeui.domain.EaseUser;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.OaPreference;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;

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

    public static boolean isFlag() {
        return flag;
    }

    public static void setFlag(boolean flag) {
        User.flag = flag;
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

    private static User mUser = null;
    private static boolean flag = true;

    public static final boolean saveUserInfo(@Nullable User user, @Nullable final EaseUser easeUser) {
        if (user != null) {
            try {
                mUser = user;
                mUser.setDevice_token(MyApplication.DEVICE_ID);
                ACache.get(MyApplication.getContext()).put("user", mUser);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }
        if (easeUser != null) {
            try {
                mUser = (User) ACache.get(MyApplication.getContext()).getAsObject("user");
                if (mUser == null){
                    mUser = new User();
                }
                if (!TextUtils.isEmpty(easeUser.getAvatar())){
                    mUser.setAvatar(easeUser.getAvatar());
                }
                if (!TextUtils.isEmpty(easeUser.getUsername())){
                    mUser.setUsername(easeUser.getUsername());
                }
                if (!TextUtils.isEmpty(easeUser.getNick())) {
                    mUser.setNickname(easeUser.getNick());
                }
                ACache.get(MyApplication.getContext()).put("user", mUser);
            } catch (Exception e) {
                flag = false;
                e.printStackTrace();
            }
        }

        try {
            OaPreference preference = OaPreference.getInstance();
            preference.set("username", mUser.getUsername()==null?"":mUser.getUsername());
            preference.set("avatar", mUser.getAvatar()==null?"": mUser.getAvatar());
            preference.set("device", MyApplication.DEVICE_ID);
            preference.set("nickname", mUser.getNickname()==null?"":mUser.getNickname());
            preference.set("oid", mUser.getOid()==null?"":mUser.getOid());
            preference.set("id", mUser.getId()==null?"":mUser.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }
}
