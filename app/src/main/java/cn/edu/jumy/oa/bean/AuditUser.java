package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by Jumy on 16/7/6 15:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class AuditUser {
    /**
     * 姓名
     */
    @JsonField
    public String name;
    /**
     * 所在单位
     */
    @JsonField
    public String oid;

    public AuditUser(String name, String oid) {
        this.name = name;
        this.oid = oid;
    }
}
