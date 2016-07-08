package cn.edu.jumy.oa.bean;

import java.util.List;

/**
 * Created by Jumy on 16/7/6 16:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Account {

    /**
     * id : 2
     * name : 省委办公厅  所属单位
     * typeid : null
     * pid : 0
     * sort : null
     * level : 0
     * contact1 : null
     * contact2 : null
     * contact3 : null
     * isdef : 1
     * isuse : 0
     * remark : null
     * cuid : 1
     * uuid : 1
     * createTime : 1467104287000
     * updataTime : 1467695810000
     * orderBy : null
     * organizationList : null  附件列表
     */
    public String id;
    public String name;
    public String typeid;
    public String pid;
    public String sort;
    public int level;
    public String contact1;
    public String contact2;
    public String contact3;
    public int isdef;
    public int isuse;
    public String remark;
    public String cuid;
    public String uuid;
    public long createTime;
    public long updataTime;
    public String orderBy;
    public List<Attachment> organizationList;



}
