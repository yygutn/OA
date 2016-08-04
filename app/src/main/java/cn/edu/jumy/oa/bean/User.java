package cn.edu.jumy.oa.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Jumy on 16/6/14 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class User implements Serializable {

    /**
     * id : 4
     * account : user2
     * name : 用户2
     * oid : 3
     * code :
     * phone : 123
     * phone2 :
     * phone3 :
     * level : 0
     * ismain : 0
     * isuse : 0
     * remark :
     * cuid : 1
     * uuid : 3
     * createTime : 1467264132000
     * updataTime : 1467515416000
     * orderBy : null
     * objList : null
     * orgname : 省委办公厅,其他部门
     * orgid : null
     */

    public String id;
    public String account;
    public String name;
    public String oid;
    public String code;
    public String phone;
    public String phone2;
    public String phone3;
    public int level;
    public int ismain;
    public int isuse;
    public String remark;
    public String cuid;
    public String uuid;
    public long createTime;
    public long updataTime;
    public Object orderBy;
    public Object objList;
    public String orgname;
    public Object orgid;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", oid='" + oid + '\'' +
                ", code='" + code + '\'' +
                ", phone='" + phone + '\'' +
                ", phone2='" + phone2 + '\'' +
                ", phone3='" + phone3 + '\'' +
                ", level=" + level +
                ", ismain=" + ismain +
                ", isuse=" + isuse +
                ", remark='" + remark + '\'' +
                ", cuid='" + cuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", createTime=" + createTime +
                ", updataTime=" + updataTime +
                ", orderBy=" + orderBy +
                ", objList=" + objList +
                ", orgname='" + orgname + '\'' +
                ", orgid=" + orgid +
                '}';
    }
}
