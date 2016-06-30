package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:31.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Organization extends DataSupport{
    private String id;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 类型id
     */
    private String typeid;

    /**
     * 父id(顶层默认为0)
     */
    private String pid;
    /**
     * 序列号
     */
    private Integer sort;
    /**
     * 单位层级
     */
    private Integer level;
    /**
     * 联系人1
     */
    private String contact1;
    /**
     * 联系人2
     */
    private String contact2;
    /**
     * 联系人3
     */
    private String contact3;
    /**
     * 是否默认(0:默认   1:非默认)
     */
    private Integer isdef;
    /**
     * 是否启用(0:是  1:否)
     */
    private Integer isuse;
    /**
     * 备注
     */
    private String remark;
    /**
     * 创建人员
     */
    private String cuid;
    /**
     * 修改人员
     */
    private String uuid;

    // 额外属性

    private String orderBy;
    private List<Organization> organizationList;
}
