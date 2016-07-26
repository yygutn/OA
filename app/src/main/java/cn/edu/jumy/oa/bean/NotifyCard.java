package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/7/26 16:13.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotifyCard extends DataSupport implements Parcelable {
    /**
     * 类型 0-会议 1-公文 2－公告
     */

    private int type = 0;

    private String ID = "";//主键
    /**
     * 创建人员
     * --发文单位
     */
    private String dispatchUnit = "";
    /**
     * 发文时间
     */
    private String dispatchTime = "";
    /**
     * 承办单位
     */
    private String undertakingUnit = "";
    /**
     * 会议时间
     */
    private String meetingTime = "";
    /**
     * 会议地点
     */
    private String meetingLocation = "";
    /**
     * 文件文号
     */
    private String documentNumber = "";
    /**
     * 标题
     */
    private String title = "";
    /**
     * 内容-标题(eg:经XX研究决定:)
     */
    private String contentHead = "";
    /**
     * 内容--正文摘要
     */
    private String content = "";
    /**
     * 接收单位
     */

    private String department = "";
    /**
     * 接收人员
     */

    private String personnel = "";
    /**
     * 附件
     */

    private List<Attachment> attachmentList;
    /**
     * 联系人姓名
     */

    private String contactName = "";
    /**
     * 联系人电话
     */

    private String contactPhone = "";
    /**
     * 等级:(1.特急2.加急3.平急4.特提)
     */

    private Integer level = 0;
    /**
     * 已签收人员
     */
    private String issuer = "";//签发人
    /**
     * 签收人数
     */

    private Integer signNum = 0;
    private String other = "";// 其他
    /**
     * 签收状态(0:是 1:否)
     */

    private Integer signStatus = 1;

    /**
     * 创建人员
     */

    private String cuid = "";
    /**
     * 修改人员
     */
    private String uuid = "";
    private String tid = "";
    private String summary = "";

    public NotifyCard() {
    }

    public NotifyCard(Node node) {
        this.type = node.type;
        this.ID = node.id;
        this.dispatchUnit = node.dispatchUnit;
        this.dispatchTime = node.dispatchTime;
        this.undertakingUnit = node.undertakingUnit;
        this.meetingTime = node.meetingTime;
        this.meetingLocation = node.meetingLocation;
        this.documentNumber = node.documentNumber;
        this.title = node.title;
        this.contentHead = node.contentHead;
        this.content = node.content;
        this.department = node.department;
        this.personnel = node.personnel;
        this.attachmentList = node.attachmentList;
        this.contactName = node.contactName;
        this.contactPhone = node.contactPhone;
        this.level = node.level;
        this.issuer = node.issuer;
        this.signNum = node.signNum;
        this.other = node.other;
        this.uuid = node.uuid;
        this.tid = node.tid;
        this.summary = node.summary;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return ID;
    }

    public void setId(String id) {
        this.ID = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getSignNum() {
        return signNum;
    }

    public void setSignNum(Integer signNum) {
        this.signNum = signNum;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public Integer getSignStatus() {
        return signStatus;
    }

    public void setSignStatus(Integer signStatus) {
        this.signStatus = signStatus;
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

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.ID);
        dest.writeString(this.dispatchUnit);
        dest.writeString(this.dispatchTime);
        dest.writeString(this.undertakingUnit);
        dest.writeString(this.meetingTime);
        dest.writeString(this.meetingLocation);
        dest.writeString(this.documentNumber);
        dest.writeString(this.title);
        dest.writeString(this.contentHead);
        dest.writeString(this.content);
        dest.writeString(this.department);
        dest.writeString(this.personnel);
        dest.writeList(this.attachmentList);
        dest.writeString(this.contactName);
        dest.writeString(this.contactPhone);
        dest.writeValue(this.level);
        dest.writeString(this.issuer);
        dest.writeValue(this.signNum);
        dest.writeString(this.other);
        dest.writeValue(this.signStatus);
        dest.writeString(this.cuid);
        dest.writeString(this.uuid);
        dest.writeString(this.tid);
        dest.writeString(this.summary);
    }

    protected NotifyCard(Parcel in) {
        this.type = in.readInt();
        this.ID = in.readString();
        this.dispatchUnit = in.readString();
        this.dispatchTime = in.readString();
        this.undertakingUnit = in.readString();
        this.meetingTime = in.readString();
        this.meetingLocation = in.readString();
        this.documentNumber = in.readString();
        this.title = in.readString();
        this.contentHead = in.readString();
        this.content = in.readString();
        this.department = in.readString();
        this.personnel = in.readString();
        this.attachmentList = new ArrayList<Attachment>();
        in.readList(this.attachmentList, Attachment.class.getClassLoader());
        this.contactName = in.readString();
        this.contactPhone = in.readString();
        this.level = (Integer) in.readValue(Integer.class.getClassLoader());
        this.issuer = in.readString();
        this.signNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.other = in.readString();
        this.signStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.cuid = in.readString();
        this.uuid = in.readString();
        this.tid = in.readString();
        this.summary = in.readString();
    }

    public static final Parcelable.Creator<NotifyCard> CREATOR = new Parcelable.Creator<NotifyCard>() {
        @Override
        public NotifyCard createFromParcel(Parcel source) {
            return new NotifyCard(source);
        }

        @Override
        public NotifyCard[] newArray(int size) {
            return new NotifyCard[size];
        }
    };
}
