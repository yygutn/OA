package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.SparseArray;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.Utils.CardGenerator;

/**
 * Created by Jumy on 16/6/20 16:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DocumentAdapter extends CommonAdapter<Doc> {
    public DocumentAdapter(Context context, int layoutId, List<Doc> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final Doc node) {
        try {
            holder.setTextColor(R.id.subtitle, Color.BLACK);
            holder.setTextColor(R.id.supportingText, Color.BLACK);
            ((CardView)holder.getView(R.id.cardView)).setCardBackgroundColor(Color.parseColor("#30DDDDDD"));

            holder.setText(R.id.subtitle,node.docTitle);

            String message = "";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            message += "发文单位: " + node.department + "\n";
            message += "发文时间: " + sdf.format(new Date(node.createTime)) + "\n";
            holder.setText(R.id.supportingText, node.docSummary);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList(List<Doc> list){
        if (list != null){
            super.mDatas.clear();
            super.mDatas = list;
            notifyDataSetChanged();
        }
    }
}
