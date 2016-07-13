package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jumy on 16/6/29 15:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Doc extends DataSupport implements Parcelable {

    public String id;
    /**
     * 公文接收单位
     */

    public String department;
    /**
     * 公文接收人员
     */

    public String personnel;

    /**
     * 公文等级(1:特急 2:加急 3:平急 4:特提)
     */

    public Integer level;
    /**
     * 公文编号
     */

    public String docNo;
    /**
     * 公文标题
     */

    public String docTitle;
    /**
     * 公文摘要
     */

    public String docSummary;

    /**
     * 签收人数
     */

    public Integer signNum;
    /**
     * 签收状态(0:是 1:否)
     */

    public Integer signStatus;
    /**
     * 是否启用(0:是  1:否)
     */

    public Integer isuse;
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

    /**
     * 已签收人员
     */
    public String signUid;

    public String orderBy;
    /**
     * 附件
     */
    public List<Attachment> attachmentList = new ArrayList<>();

    /**
     * 创建时间
     */
    public long createTime;

    /**
     * 发送单位名称-self
     */
    public String sendDepartmentInfo;
    /**
     * 接收单位多个以,隔开
     */
    public String departmentInfo;

    public String didtask;
    /**
     * 任务ID
     */
    public String tid;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.department);
        dest.writeString(this.personnel);
        dest.writeValue(this.level);
        dest.writeString(this.docNo);
        dest.writeString(this.docTitle);
        dest.writeString(this.docSummary);
        dest.writeValue(this.signNum);
        dest.writeValue(this.signStatus);
        dest.writeValue(this.isuse);
        dest.writeString(this.remark);
        dest.writeString(this.cuid);
        dest.writeString(this.uuid);
        dest.writeString(this.signUid);
        dest.writeString(this.orderBy);
        dest.writeList(this.attachmentList);
        dest.writeLong(this.createTime);
        dest.writeString(departmentInfo);
        dest.writeString(sendDepartmentInfo);
        dest.writeString(didtask);
        dest.writeString(tid);
    }

    public Doc() {
    }

    protected Doc(Parcel in) {
        this.id = in.readString();
        this.department = in.readString();
        this.personnel = in.readString();
        this.level = (Integer) in.readValue(Integer.class.getClassLoader());
        this.docNo = in.readString();
        this.docTitle = in.readString();
        this.docSummary = in.readString();
        this.signNum = (Integer) in.readValue(Integer.class.getClassLoader());
        this.signStatus = (Integer) in.readValue(Integer.class.getClassLoader());
        this.isuse = (Integer) in.readValue(Integer.class.getClassLoader());
        this.remark = in.readString();
        this.cuid = in.readString();
        this.uuid = in.readString();
        this.signUid = in.readString();
        this.orderBy = in.readString();
        this.attachmentList = new ArrayList<Attachment>();
        in.readList(this.attachmentList, Attachment.class.getClassLoader());
        this.createTime = in.readLong();
        this.departmentInfo = in.readString();
        this.sendDepartmentInfo = in.readString();
        this.didtask = in.readString();
        this.tid = in.readString();
    }

    public static final Parcelable.Creator<Doc> CREATOR = new Parcelable.Creator<Doc>() {
        @Override
        public Doc createFromParcel(Parcel source) {
            return new Doc(source);
        }

        @Override
        public Doc[] newArray(int size) {
            return new Doc[size];
        }
    };
}
