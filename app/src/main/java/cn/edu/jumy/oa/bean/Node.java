package cn.edu.jumy.oa.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Jumy on 16/6/20 15:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Node extends DataSupport implements Serializable {
    /**
     * 类型 0-会议 1-公文 2－公告
     */
    
    private int type;
    
    private String id;//主键
    /**
     * 创建人员
     * --发文单位
     */
    private String dispatchUnit;
    /**
     * 发文时间
     */
    private String dispatchTime;
    /**
     * 承办单位
     */
    private String undertakingUnit;
    /**
     * 会议时间
     */
    private String meetingTime;
    /**
     * 会议地点
     */
    private String meetingLocation;
    /**
     * 文件文号
     */
    private String documentNumber;
    /**
     * 标题
     */
    private String title;
    /**
     * 内容-标题(eg:经XX研究决定:)
     */
    private String contentHead;
    /**
     * 内容--正文摘要
     */
    private String content;
    /**
     * 接收单位
     */
    
    private String department;
    /**
     * 接收人员
     */
    
    private String personnel;
    /**
     * 附件
     */
    
    private List<Attachment> attachmentList;
    /**
     * 联系人姓名
     */
    
    private String contactName;
    /**
     * 联系人电话
     */
    
    private String contactPhone;
    /**
     * 等级:(1.特急2.加急3.平急4.特提)
     */
    
    private Integer level;
    /**
     * 已签收人员
     */
    private String issuer;//签发人
    /**
     * 签收人数
     */
    
    private Integer signNum;
    private String other;// 其他

    public String tid;

    public Node(String title, String content, int type) {
        this.title = title;
        this.content = content;
        this.type = type;
    }
    public Node(){

    }
    public Node(Doc doc){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.type = 1;
        this.attachmentList = doc.attachmentList;
        this.id = doc.id;
        this.level = doc.level;
        this.content = doc.docSummary;
        this.department = doc.department;
        this.documentNumber = doc.docNo;
        this.attachmentList = doc.attachmentList;
        this.title = doc.docTitle;
        this.dispatchTime = sdf.format(new Date(doc.createTime));
        this.tid = doc.tid;
    }
    public Node(Meet meet){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        this.contactName = meet.contactName;
        this.type = 0;
        this.attachmentList = meet.attachmentList;
        this.id = meet.id;
        this.meetingLocation = meet.addr;
        this.meetingTime = sdf.format(new Date(meet.meetTime));
        this.undertakingUnit = meet.meetCompanyName;
        this.dispatchTime = sdf.format(new Date(meet.createTime));
        this.dispatchUnit = meet.sendDepartmentInfo;
        this.department = meet.departmentInfo;
        this.level = meet.level;
        this.content = meet.docSummary;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
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
