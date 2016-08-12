package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:24.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Meet extends DataSupport implements Parcelable {
    
    public String id;
    /**
     * 会议名称
     */
    
    public String name;
    /**
     * 会议时间
     */
    
    public long meetTime;

    /**
     * 会议承办单位ID
     */
    
    public String meetCompany;
    /**
     * 接收单位
     */
    
    public String department;
    /**
     * 等级(1:特急 2:加急 3:平急 4:特提)
     */
    
    public int level;
    /**
     * 编号
     */
    
    public String docNo;
    /**
     * 标题
     */
    
    public String docTitle;
    /**
     * 摘要
     */
    public String docSummary;
    /**
     * 联系人姓名
     */
    
    public String contactName;
    /**
     * 联系人电话
     */
    
    public String contactPhone;
    /**
     * 会议地址
     */
    
    public String addr;
    /**
     * 签收人数
     */

    public int signNum;
    /**
     * 签收状态(0:是 1:否)
     */

    public int signStatus;
    /**
     * 签收(0:是  1:否)
     */
    
    public int isuse;
    /**
     * 备注
     */
    
    public String remark;
    /**
     * 创建人员
     */
    
    public String cuid;
    /**
     * 修改人员
     */
    
    public String uuid;

    // 额外属性

    public String orderBy;
    /**
     * 发文时间
     */
    public long createTime;
    /**
     * 附件
     */
    public List<Attachment> attachmentList = new ArrayList<>();
    /**
     * 会议时间-转化
     */
    public String meetTimeString;
    /**
     * 发送单位名称-self
     */
    public String sendDepartmentInfo;
    /**
     * 接收单位多个以,隔开
     */
    public String departmentInfo;
    /**
     * 会议承办单位名称
     */
    public String meetCompanyName;
    /**
     * tid 任务ID
     */
    public String tid;
    /**
     * didtask
     */
    public String didtask;
    /**
     * passStatus : 3
     * updataTime : null
     */

    public int passStatus;
    public long updataTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeLong(this.meetTime);
        dest.writeString(this.meetCompany);
        dest.writeString(this.department);
        dest.writeValue(this.level);
        dest.writeString(this.docNo);
        dest.writeString(this.docTitle);
        dest.writeString(this.docSummary);
        dest.writeString(this.contactName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.addr);
        dest.writeValue(this.signNum);
        dest.writeValue(this.signStatus);
        dest.writeValue(this.isuse);
        dest.writeString(this.remark);
        dest.writeString(this.cuid);
        dest.writeString(this.uuid);
        dest.writeString(this.orderBy);
        dest.writeLong(this.createTime);
        dest.writeList(this.attachmentList);
        dest.writeString(this.meetTimeString);
        dest.writeString(this.sendDepartmentInfo);
        dest.writeString(this.departmentInfo);
        dest.writeString(this.meetCompanyName);
        dest.writeString(this.tid);
        dest.writeString(this.didtask);
        dest.writeInt(this.passStatus);
        dest.writeLong(this.updataTime);
    }

    public Meet() {
    }

    protected Meet(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.meetTime = in.readLong();
        this.meetCompany = in.readString();
        this.department = in.readString();
        this.level = (int) in.readValue(int.class.getClassLoader());
        this.docNo = in.readString();
        this.docTitle = in.readString();
        this.docSummary = in.readString();
        this.contactName = in.readString();
        this.contactPhone = in.readString();
        this.addr = in.readString();
        this.signNum = (int) in.readValue(int.class.getClassLoader());
        this.signStatus = (int) in.readValue(int.class.getClassLoader());
        this.isuse = (int) in.readValue(int.class.getClassLoader());
        this.remark = in.readString();
        this.cuid = in.readString();
        this.uuid = in.readString();
        this.orderBy = in.readString();
        this.createTime = in.readLong();
        this.attachmentList = new ArrayList<Attachment>();
        in.readList(this.attachmentList, Attachment.class.getClassLoader());
        this.meetTimeString = in.readString();
        this.sendDepartmentInfo = in.readString();
        this.departmentInfo = in.readString();
        this.meetCompanyName = in.readString();
        this.tid = in.readString();
        this.didtask = in.readString();
        this.passStatus = in.readInt();
        this.updataTime = in.readLong();
    }

    public static final Parcelable.Creator<Meet> CREATOR = new Parcelable.Creator<Meet>() {
        @Override
        public Meet createFromParcel(Parcel source) {
            return new Meet(source);
        }

        @Override
        public Meet[] newArray(int size) {
            return new Meet[size];
        }
    };

    @Override
    public String toString() {
        return "Meet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", meetTime=" + meetTime +
                ", meetCompany='" + meetCompany + '\'' +
                ", department='" + department + '\'' +
                ", level=" + level +
                ", docNo='" + docNo + '\'' +
                ", docTitle='" + docTitle + '\'' +
                ", docSummary='" + docSummary + '\'' +
                ", contactName='" + contactName + '\'' +
                ", contactPhone='" + contactPhone + '\'' +
                ", addr='" + addr + '\'' +
                ", signNum=" + signNum +
                ", signStatus=" + signStatus +
                ", isuse=" + isuse +
                ", remark='" + remark + '\'' +
                ", cuid='" + cuid + '\'' +
                ", uuid='" + uuid + '\'' +
                ", orderBy='" + orderBy + '\'' +
                ", createTime=" + createTime +
                ", attachmentList=" + attachmentList +
                ", meetTimeString='" + meetTimeString + '\'' +
                ", sendDepartmentInfo='" + sendDepartmentInfo + '\'' +
                ", departmentInfo='" + departmentInfo + '\'' +
                ", meetCompanyName='" + meetCompanyName + '\'' +
                ", tid='" + tid + '\'' +
                ", didtask='" + didtask + '\'' +
                ", passStatus=" + passStatus +
                ", updataTime=" + updataTime +
                '}';
    }
}
