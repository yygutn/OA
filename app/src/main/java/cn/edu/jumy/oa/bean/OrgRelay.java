package cn.edu.jumy.oa.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexEntity;

/**
 * Created by Jumy on 16/8/8 11:40.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OrgRelay extends IndexEntity implements Parcelable {
    /**
     * id : 2
     * name : 省委办公厅
     * children : [{"id":"14a43bef96914ac98e155ba5bead7af9","name":"综合处","children":[]},{"id":"94c7ea07086445349436794f8a4efd78","name":"秘书一处","children":[]},{"id":"3d75914b0bd34be98507c67707098685","name":"秘书二处","children":[]},{"id":"a6ecd51cec87450ba4e30b7daa207cec","name":"例外","children":[]},{"id":"3","name":"其他部门","children":[{"id":"79cd38a0d33348059b2cb65e0df18ad4","name":"其他2","children":[]}]}]
     */

    public String id;
    public String name;
    public boolean checked = false;
    /**
     * id : 14a43bef96914ac98e155ba5bead7af9
     * name : 综合处
     * children : []
     */

    public ArrayList<OrgRelay> children;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeList(this.children);
    }

    public OrgRelay() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    protected OrgRelay(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.children = new ArrayList<OrgRelay>();
        in.readList(this.children, OrgRelay.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrgRelay> CREATOR = new Parcelable.Creator<OrgRelay>() {
        @Override
        public OrgRelay createFromParcel(Parcel source) {
            return new OrgRelay(source);
        }

        @Override
        public OrgRelay[] newArray(int size) {
            return new OrgRelay[size];
        }
    };
}
