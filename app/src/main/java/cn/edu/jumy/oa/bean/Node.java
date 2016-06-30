package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Jumy on 16/6/20 15:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Node extends DataSupport implements Serializable {
    private int type;//类型 0-会议 1-公文 2－公告
    @JsonField
    private String id;//主键
    /**
     * 创建人员
     */
    @JsonField(name = "cuid")
    private String dispatchUnit;//发文单位
    @JsonField(name = "createTime")
    private String dispatchTime;//发文时间
    @JsonField(name = "meetCompany")
    private String undertakingUnit;//承办单位
    @JsonField(name = "meetTime")
    private String meetingTime;//会议时间
    @JsonField(name = "addr")
    private String meetingLocation;//会议地点
    @JsonField(name = "docNo")
    private String documentNumber;//文件文号
    @JsonField(name = "name")
    private String title;//标题
    private String subtitle;// 子标题
    private String contentHead;//内容-标题(接收单位？？？)
    private String content;//内容
    /**
     * 接收单位
     */
    @JsonField
    private String department;
    /**
     * 接收人员
     */
    @JsonField
    private String personnel;
    /**
     * 附件
     */
    @JsonField
    private List<Attachment> attachmentList;
    /**
     * 联系人姓名
     */
    @JsonField
    private String contactName;
    /**
     * 联系人电话
     */
    @JsonField
    private String contactPhone;
    /**
     * 等级:(1.特急2.加急3.平急4.特提)
     */
    @JsonField
    private String level;
    /**
     * 已签收人员
     */
    @JsonField(name = "signUid")
    private String issuer;//签发人
    /**
     * 签收人数
     */
    @JsonField
    private Integer signNum;
    private String other;// 其他

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
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getID() {
        return id;
    }

    public void setID(String ID) {
        this.id = ID;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getContentHead() {
        return contentHead;
    }

    public void setContentHead(String contentHead) {
        this.contentHead = contentHead;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
