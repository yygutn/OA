package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Jumy on 16/7/22 12:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OrganizationOften implements Parcelable{
    /**
     * id : acb796e14cb8415eb0f2f912173dd3fb
     * uid : 1
     * value : c051a80921d24103946505df89f40ad4,de8df8b0f7784d47b2ce930cc26f96f7
     * name : sss
     * sort : 2
     * departmentName : 省委机要局,省信访局
     */

    public String id;
    public String uid;
    public String value;
    public String name;
    public int sort;
    public String departmentName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.uid);
        dest.writeString(this.value);
        dest.writeString(this.name);
        dest.writeInt(this.sort);
        dest.writeString(this.departmentName);
    }

    public OrganizationOften() {
    }

    protected OrganizationOften(Parcel in) {
        this.id = in.readString();
        this.uid = in.readString();
        this.value = in.readString();
        this.name = in.readString();
        this.sort = in.readInt();
        this.departmentName = in.readString();
    }

    public static final Parcelable.Creator<OrganizationOften> CREATOR = new Parcelable.Creator<OrganizationOften>() {
        @Override
        public OrganizationOften createFromParcel(Parcel source) {
            return new OrganizationOften(source);
        }

        @Override
        public OrganizationOften[] newArray(int size) {
            return new OrganizationOften[size];
        }
    };
}
