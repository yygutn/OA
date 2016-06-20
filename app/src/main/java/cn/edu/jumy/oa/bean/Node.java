package cn.edu.jumy.oa.bean;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Jumy on 16/6/20 15:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Node implements Serializable{
    private String dispatchUnit;//发文单位
    private String dispatchTime;//发文时间
    private String undertakingUnit;//承办单位
    private String meetingTime;//会议时间
    private String meetingLocation;//会议地点
    private String documentNumber;//文件文号
    private String title;//
    private File file;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDispatchUnit() {
        return dispatchUnit;
    }

    public void setDispatchUnit(String dispatchUnit) {
        this.dispatchUnit = dispatchUnit;
    }

    public String getDispatchTime() {
        return dispatchTime;
    }

    public void setDispatchTime(String dispatchTime) {
        this.dispatchTime = dispatchTime;
    }

    public String getUndertakingUnit() {
        return undertakingUnit;
    }

    public void setUndertakingUnit(String undertakingUnit) {
        this.undertakingUnit = undertakingUnit;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
