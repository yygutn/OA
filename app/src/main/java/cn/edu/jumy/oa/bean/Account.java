package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexEntity;


/**
 * Created by Jumy on 16/7/6 16:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Account extends IndexEntity implements Parcelable {

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
//    public String typeid;
//    public String pid;
//    public String sort;
//    public int level;
//    public String contact1;
//    public String contact2;
//    public String contact3;
//    public int isdef;
//    public int isuse;
//    public String remark;
//    public String cuid;
//    public String uuid;
//    public long createTime;
//    public long updataTime;
//    public String orderBy;
//    public List<Account> organizationList;

    public boolean checked = false;

    public Account(String id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Account() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    protected Account(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.checked = in.readByte() != 0;
    }

    public static final Creator<Account> CREATOR = new Creator<Account>() {
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
