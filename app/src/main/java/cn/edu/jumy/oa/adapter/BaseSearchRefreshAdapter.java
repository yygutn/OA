package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/20 16:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class BaseSearchRefreshAdapter extends CommonAdapter<Node> {
    public BaseSearchRefreshAdapter(Context context, int layoutId, List<Node> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final Node node) {
        try {
            ((CardView) holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));
            holder.setText(R.id.subtitle, node.title);
            holder.setText(R.id.supportingText, node.content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList(List<Node> list) {
        if (list != null) {
            super.mDatas.clear();
            super.mDatas = list;
            notifyDataSetChanged();
        }
    }
}
