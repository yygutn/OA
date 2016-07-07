package cn.edu.jumy.oa.bean;

/**
 * Created by Jumy on 16/7/6 15:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditUser {
    /**
     * 姓名
     */
    public String name;
    /**
     * 所在单位
     */
    public String oid;

    public AuditUser(String name, String oid) {
        this.name = name;
        this.oid = oid;
    }
}
