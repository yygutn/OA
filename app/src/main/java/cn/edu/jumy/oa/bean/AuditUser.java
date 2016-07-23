package cn.edu.jumy.oa.bean;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jumy on 16/7/6 15:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditUser {
    /**
     * 姓名
     */
    public String name = "";
    /**
     * 所在单位
     */
    public String oid;
    /**
     * id : c2250522ed8b48a5a594b8f6c96dbbee
     * uid : 2
     * did : 5a5d082687b24c0a9abb1c2f55253514
     * type : 2
     * signStatus : 0
     * passStatus : 2
     * remark : null
     * passRemark : null
     * orderBy : null
     * organame : 省委办公厅
     * uname : 用户
     */

    public String id;
    public String uid;
    public String did;
    public String post = "";
    public String sex = "";
    public String phone = "";
    public int type;
    public int signStatus;
    public int passStatus;
    public Object remark;
    public Object passRemark;
    public Object orderBy;
    public String organame;
    public String uname;

    public AuditUser(String name, String oid) {
        this.name = name;
        this.oid = oid;
    }

}
