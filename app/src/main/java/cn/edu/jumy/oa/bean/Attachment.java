package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * 附件
 * Created by Jumy on 16/6/29 13:17.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Attachment extends DataSupport implements Serializable{
    @JsonField
    private String id;
    /**
     * 父id 当PID为UID的时候，他是用户头像
     */
    @JsonField
    private String pid;
    /**
     * 创建者
     */
    @JsonField
    private String cuid;
    /**
     * 1为公文;2为会议;3用户头像
     */
    @JsonField
    private Integer type;
    /**
     * 文件名称
     */
    @JsonField
    private String fileName;
    /**
     * 文件后缀名
     */
    @JsonField
    private String suffix;
    /**
     * 文件路径
     */
    @JsonField
    private String url;
    /**
     * 备注
     */
    @JsonField
    private String remark;
    /**
     * 是否启用(0:是  1:否)
     */
    @JsonField
    private Integer isuse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getIsuse() {
        return isuse;
    }

    public void setIsuse(Integer isuse) {
        this.isuse = isuse;
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "id='" + id + '\'' +
                ", pid='" + pid + '\'' +
                ", cuid='" + cuid + '\'' +
                ", type=" + type +
                ", fileName='" + fileName + '\'' +
                ", suffix='" + suffix + '\'' +
                ", url='" + url + '\'' +
                ", remark='" + remark + '\'' +
                ", isuse=" + isuse +
                '}';
    }
}