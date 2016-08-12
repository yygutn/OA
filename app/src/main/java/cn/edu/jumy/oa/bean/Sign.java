package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jumy on 16/7/27 16:34.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Sign implements Parcelable {
    public String id;
    /**
     * 会议id
     */
    public String pid;
    /**
     * 姓名
     */
    public String name;
    /**
     * 职位
     */
    public String post;
    /**
     * 性别(0:男  1:女)
     */
    public int sex;
    /**
     * 电话
     */
    public String phone;
    /**
     * 状态(0:参会 1:听会 2:请假)
     */
    public int type;
    /**
     * 审批状态(0:审批通过,1:审批未通过,2:正在审批,3:未审批)
     */
    public int passStatus;
    /**
     * 备注
     */
    public String remark;
    /**
     * 审批备注
     */
    public String passRemark;
    //额外属性
    /**
     * 组织的名字
     */
    public String organame;

    public String orderBy;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.pid);
        dest.writeString(this.name);
        dest.writeString(this.post);
        dest.writeValue(this.sex);
        dest.writeString(this.phone);
        dest.writeValue(this.type);
        dest.writeValue(this.passStatus);
        dest.writeString(this.remark);
        dest.writeString(this.passRemark);
        dest.writeString(this.organame);
        dest.writeString(this.orderBy);
    }

    public Sign() {
    }
    public Sign(AuditUser auditUser) {
        this.name = auditUser.name;
        this.post = auditUser.post;
        this.id = auditUser.id;
        this.remark = auditUser.remark;
        this.phone = auditUser.phone;
        this.passRemark = auditUser.passRemark;
        this.passStatus = auditUser.passStatus;
        this.organame = auditUser.organame;
        this.type = auditUser.type;
    }

    protected Sign(Parcel in) {
        this.id = in.readString();
        this.pid = in.readString();
        this.name = in.readString();
        this.post = in.readString();
        this.sex = (int) in.readValue(int.class.getClassLoader());
        this.phone = in.readString();
        this.type = (int) in.readValue(int.class.getClassLoader());
        this.passStatus = (int) in.readValue(int.class.getClassLoader());
        this.remark = in.readString();
        this.passRemark = in.readString();
        this.organame = in.readString();
        this.orderBy = in.readString();
    }

    public static final Parcelable.Creator<Sign> CREATOR = new Parcelable.Creator<Sign>() {
        @Override
        public Sign createFromParcel(Parcel source) {
            return new Sign(source);
        }

        @Override
        public Sign[] newArray(int size) {
            return new Sign[size];
        }
    };
}
