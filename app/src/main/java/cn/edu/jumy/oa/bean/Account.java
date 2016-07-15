package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import me.yokeyword.indexablelistview.IndexEntity;

/**
 * Created by Jumy on 16/7/6 16:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Account extends IndexEntity implements Serializable, Parcelable {

    /**
     * id : 2
     * name : 省委办公厅  所属单位
     * typeid : null
     * pid : 0
     * sort : null
     * level : 0
     * contact1 : null
     * contact2 : null
     * contact3 : null
     * isdef : 1
     * isuse : 0
     * remark : null
     * cuid : 1
     * uuid : 1
     * createTime : 1467104287000
     * updataTime : 1467695810000
     * orderBy : null
     * organizationList : null  附件列表
     */
    public String id;
    public String name;
    public String typeid;
    public String pid;
    public String sort;
    public int level;
    public String contact1;
    public String contact2;
    public String contact3;
    public int isdef;
    public int isuse;
    public String remark;
    public String cuid;
    public String uuid;
    public long createTime;
    public long updataTime;
    public String orderBy;
    public List<Attachment> organizationList;

    public boolean checked = false;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.typeid);
        dest.writeString(this.pid);
        dest.writeString(this.sort);
        dest.writeInt(this.level);
        dest.writeString(this.contact1);
        dest.writeString(this.contact2);
        dest.writeString(this.contact3);
        dest.writeInt(this.isdef);
        dest.writeInt(this.isuse);
        dest.writeString(this.remark);
        dest.writeString(this.cuid);
        dest.writeString(this.uuid);
        dest.writeLong(this.createTime);
        dest.writeLong(this.updataTime);
        dest.writeString(this.orderBy);
        dest.writeList(this.organizationList);
    }

    public Account() {
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.typeid = in.readString();
        this.pid = in.readString();
        this.sort = in.readString();
        this.level = in.readInt();
        this.contact1 = in.readString();
        this.contact2 = in.readString();
        this.contact3 = in.readString();
        this.isdef = in.readInt();
        this.isuse = in.readInt();
        this.remark = in.readString();
        this.cuid = in.readString();
        this.uuid = in.readString();
        this.createTime = in.readLong();
        this.updataTime = in.readLong();
        this.orderBy = in.readString();
        this.organizationList = new ArrayList<Attachment>();
        in.readList(this.organizationList, Attachment.class.getClassLoader());
    }

    public static final Parcelable.Creator<Account> CREATOR = new Parcelable.Creator<Account>() {
        @Override
        public Account createFromParcel(Parcel source) {
            return new Account(source);
        }

        @Override
        public Account[] newArray(int size) {
            return new Account[size];
        }
    };
}
