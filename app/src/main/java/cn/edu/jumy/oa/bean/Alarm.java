package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by Jumy on 16/7/4 12:19.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * 闹钟，用于自定义日历提醒
 *
 */
public class Alarm extends DataSupport{
    /**
     * 提醒内容
     */
    private String content;
    /**
     * 时间
     */
    private Date time;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
