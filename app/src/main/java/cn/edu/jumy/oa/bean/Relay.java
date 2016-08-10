package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexEntity;

/**
 * Created by Jumy on 16/8/8 11:40.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Relay implements Parcelable {

    /**
     * id : 6b8dddc23a9840e9857c222054147a43
     * name : 测试会议01
     * meetTime : 1470491820000
     * meetCompany : 2
     * personnel : 2
     * department : 2,de8df8b0f7784d47b2ce930cc26f96f7,853aa3e6d7a64d07bfe2197c3696abd0,3
     * level : 1
     * docNo : 20160806-01
     * docTitle : 测试会议01
     * docSummary : 测试会议01
     * contactName : 徐志鹏
     * contactPhone : 17826800224
     * addr : 文一路115号
     * signNum : 1
     * signStatus : 1
     * passStatus : null
     * isuse : 0
     * remark : 0
     * cuid : 2
     * uuid : null
     * createTime : 1470564171000
     * updataTime : null
     * orderBy : null
     * attachmentList : null
     * meetTimeString : null
     * sendDepartmentInfo : 省委办公厅
     * departmentInfo : null
     * meetCompanyName : 省委办公厅
     * tid : null
     * didtask : null
     * oldid : dcf06c0f0f2c4b0d8e590d143f5898f4
     * isPass : null
     * signUid : 0
     */

    public String id;
    public String name;
    public long meetTime;
    public String meetCompany;
    public String personnel;
    public String department;
    public int level;
    public int type;
    public String docNo;
    public String docTitle;
    public String docSummary;
    public String contactName;
    public String contactPhone;
    public String addr;
    public int signNum;
    public int signStatus;
    public int passStatus;
    public int isuse;
    public String remark;
    public String cuid;
    public String uuid;
    public long createTime;
    public String orderBy;
    public ArrayList<Attachment> attachmentList;
    public String meetTimeString;
    public String sendDepartmentInfo;
    public String departmentInfo;
    public String meetCompanyName;
    public String tid;
    public String didtask;
    public String oldid;
    public int isPass;
    public String signUid;
    //
    public String uid;
    public String oid;
    public String did;
    public String organame;
    public String uname;
    public String passRemark;


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
        dest.writeString(this.personnel);
        dest.writeString(this.department);
        dest.writeInt(this.level);
        dest.writeInt(this.type);
        dest.writeString(this.docNo);
        dest.writeString(this.docTitle);
        dest.writeString(this.docSummary);
        dest.writeString(this.contactName);
        dest.writeString(this.contactPhone);
        dest.writeString(this.addr);
        dest.writeInt(this.signNum);
        dest.writeInt(this.signStatus);
        dest.writeInt(this.passStatus);
        dest.writeInt(this.isuse);
        dest.writeString(this.remark);
        dest.writeString(this.cuid);
        dest.writeString(this.uuid);
        dest.writeLong(this.createTime);
        dest.writeString(this.orderBy);
        dest.writeList(this.attachmentList);
        dest.writeString(this.meetTimeString);
        dest.writeString(this.sendDepartmentInfo);
        dest.writeString(this.departmentInfo);
        dest.writeString(this.meetCompanyName);
        dest.writeString(this.tid);
        dest.writeString(this.didtask);
        dest.writeString(this.oldid);
        dest.writeInt(this.isPass);
        dest.writeString(this.signUid);
        dest.writeString(this.uid);
        dest.writeString(this.oid);
        dest.writeString(this.did);
        dest.writeString(this.organame);
        dest.writeString(this.uname);
        dest.writeString(this.passRemark);
    }

    public Relay() {
    }

    protected Relay(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.meetTime = in.readLong();
        this.meetCompany = in.readString();
        this.personnel = in.readString();
        this.department = in.readString();
        this.level = in.readInt();
        this.type = in.readInt();
        this.docNo = in.readString();
        this.docTitle = in.readString();
        this.docSummary = in.readString();
        this.contactName = in.readString();
        this.contactPhone = in.readString();
        this.addr = in.readString();
        this.signNum = in.readInt();
        this.signStatus = in.readInt();
        this.passStatus = in.readInt();
        this.isuse = in.readInt();
        this.remark = in.readString();
        this.cuid = in.readString();
        this.uuid = in.readString();
        this.createTime = in.readLong();
        this.orderBy = in.readString();
        this.attachmentList = new ArrayList<Attachment>();
        in.readList(this.attachmentList, Attachment.class.getClassLoader());
        this.meetTimeString = in.readString();
        this.sendDepartmentInfo = in.readString();
        this.departmentInfo = in.readString();
        this.meetCompanyName = in.readString();
        this.tid = in.readString();
        this.didtask = in.readString();
        this.oldid = in.readString();
        this.isPass = in.readInt();
        this.signUid = in.readString();
        this.uid = in.readString();
        this.oid = in.readString();
        this.did = in.readString();
        this.organame = in.readString();
        this.uname = in.readString();
        this.passRemark = in.readString();
    }

    public static final Parcelable.Creator<Relay> CREATOR = new Parcelable.Creator<Relay>() {
        @Override
        public Relay createFromParcel(Parcel source) {
            return new Relay(source);
        }

        @Override
        public Relay[] newArray(int size) {
            return new Relay[size];
        }
    };
}
