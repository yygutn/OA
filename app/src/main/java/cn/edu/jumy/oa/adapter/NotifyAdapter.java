package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Notify;

/**
 * Created by Jumy on 16/7/22 17:15.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotifyAdapter extends CommonAdapter<Notify>{
    public NotifyAdapter(Context context, int layoutId, List<Notify> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Notify node) {
        ((CardView)holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));
        holder.setText(R.id.subtitle,node.title);
        holder.setText(R.id.supportingText, node.summary);
    }
    public void setList(ArrayList<Notify> list){
        mDatas.clear();
        mDatas.addAll(list);
        notifyDataSetChanged();
    }
}
