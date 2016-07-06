package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

/**
 * Created by Jumy on 16/7/6 16:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Account {

    /**
     * id : 2
     * name : 省委办公厅
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
     * organizationList : null
     */

    private String id;
    private String name;
    private String typeid;
    private String pid;
    private String sort;
    private int level;
    private String contact1;
    private String contact2;
    private String contact3;
    private int isdef;
    private int isuse;
    private String remark;
    private String cuid;
    private String uuid;
    private long createTime;
    private long updataTime;
    private String orderBy;
    private List<Attachment> organizationList;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getContact1() {
        return contact1;
    }

    public void setContact1(String contact1) {
        this.contact1 = contact1;
    }

    public String getContact2() {
        return contact2;
    }

    public void setContact2(String contact2) {
        this.contact2 = contact2;
    }

    public String getContact3() {
        return contact3;
    }

    public void setContact3(String contact3) {
        this.contact3 = contact3;
    }

    public int getIsdef() {
        return isdef;
    }

    public void setIsdef(int isdef) {
        this.isdef = isdef;
    }

    public int getIsuse() {
        return isuse;
    }

    public void setIsuse(int isuse) {
        this.isuse = isuse;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdataTime() {
        return updataTime;
    }

    public void setUpdataTime(long updataTime) {
        this.updataTime = updataTime;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<Attachment> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Attachment> organizationList) {
        this.organizationList = organizationList;
    }


}
