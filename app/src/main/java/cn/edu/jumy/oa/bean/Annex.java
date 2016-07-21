package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.io.File;

/**
 * Created by Jumy on 16/7/21 14:15.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Annex extends DataSupport{
    private String IDS;
    /**
     * 父id 当PID为UID的时候，他是用户头像
     */
    private String pid;
    /**
     * 创建者
     */
    private String cuid;
    /**
     * 1为公文;2为会议;3用户头像
     */
    private Integer type;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件后缀名
     */
    private String suffix;
    private File file;

    public Annex(Attachment attachment,File file) {
        this.IDS = attachment.getId();
        this.pid = attachment.getPid();
        this.cuid = attachment.getPid();
        this.type = attachment.getType();
        this.fileName = attachment.getFileName();
        this.suffix = attachment.getSuffix();
        this.file = file;
    }

    public Annex() {
    }

    public String getID() {
        return IDS;
    }

    public void setID(String IDS) {
        this.IDS = IDS;
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

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setAnnex(Attachment attachment,File file) {
        this.IDS = attachment.getId();
        this.pid = attachment.getPid();
        this.cuid = attachment.getPid();
        this.type = attachment.getType();
        this.fileName = attachment.getFileName();
        this.suffix = attachment.getSuffix();
        this.file = file;
    }

}
