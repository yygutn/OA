package cn.edu.jumy.oa.bean;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jumy on 16/6/20 15:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@JsonObject
public class Node extends DataSupport implements Serializable {
    /**
     * 类型 0-会议 1-公文 2－公告
     */
    @JsonField
    private int type;
    @JsonField
    private String id;//主键
    /**
     * 创建人员
     * --发文单位
     */
    @JsonField(name = "cuid")
    private String dispatchUnit;
    /**
     * 发文时间
     */
    @JsonField(name = "createTime")
    private String dispatchTime;
    /**
     * 承办单位
     */
    @JsonField(name = "meetCompany")
    private String undertakingUnit;
    /**
     * 会议时间
     */
    @JsonField(name = "meetTime")
    private String meetingTime;
    /**
     * 会议地点
     */
    @JsonField(name = "addr")
    private String meetingLocation;
    /**
     * 文件文号
     */
    @JsonField(name = "docNo")
    private String documentNumber;
    /**
     * 标题
     */
    @JsonField(name = "name")
    private String title;
    /**
     * 内容-标题(eg:经XX研究决定:)
     */
    private String contentHead;
    /**
     * 内容--正文摘要
     */
    @JsonField(name = "docSummary")
    private String content;
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

    public Node(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }
    public Node(){

    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPersonnel() {
        return personnel;
    }

    public void setPersonnel(String personnel) {
        this.personnel = personnel;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }
}
