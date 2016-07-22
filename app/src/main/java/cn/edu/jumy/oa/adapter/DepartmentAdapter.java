package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexableAdapter;

/**
 * Created by Jumy on 16/7/15 09:52.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DepartmentAdapter extends IndexableAdapter<Account> {

    private Context mContext;
    public DepartmentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    protected TextView onCreateTitleViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tv_title_department, parent, false);
        return (TextView) view.findViewById(R.id.tv_title);
    }

    @Override
    protected ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_department_select, parent, false);
        return new DViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, final Account item) {
        try {
            DViewHolder viewHolder = (DViewHolder) holder;
            viewHolder.tvName.setText(item.name);
            viewHolder.checkBox.setChecked(item.checked);
            viewHolder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.checked = !item.checked;
                    notifyDataSetChanged();
                }
            });
            viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.checked = isChecked;
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class DViewHolder extends IndexableAdapter.ViewHolder{
        TextView tvName;
        CheckBox checkBox;
        RelativeLayout root;
        public DViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.tv_name);
            checkBox = (CheckBox) view.findViewById(R.id.radioButton);
            root = (RelativeLayout) view.findViewById(R.id.root_layout);
        }
    }
}
