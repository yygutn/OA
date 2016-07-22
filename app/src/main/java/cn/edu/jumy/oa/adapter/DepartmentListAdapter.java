package cn.edu.jumy.oa.adapter;

import android.content.Context;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.OrganizationOften;

/**
 * Created by Jumy on 16/7/22 15:06.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DepartmentListAdapter extends CommonAdapter<OrganizationOften>{
    public DepartmentListAdapter(Context context, int layoutId, List<OrganizationOften> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, OrganizationOften organizationOften) {
        holder.setText(R.id.tv_name,organizationOften.name);
    }

    public void setList(ArrayList<OrganizationOften> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
