package cn.edu.jumy.oa.bean;

import org.litepal.annotation.Column;
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
    @Column(nullable = false)
    private Date time;
    /**
     * 所属单位ID 或者存放Username作为标识
     */
    @Column(nullable = false)
    private String username;

    public Alarm() {
    }

    public Alarm(String content, Date time, String username) {
        this.content = content;
        this.time = time;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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

    @Override
    public String toString() {
        return "Alarm{" +
                "content='" + content + '\'' +
                ", time=" + time +
                ", username='" + username + '\'' +
                '}';
    }
}
