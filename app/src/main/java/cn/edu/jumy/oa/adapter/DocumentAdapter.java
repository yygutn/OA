package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DocumentDetailsActivity_;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.utils.CardGenerator;

/**
 * Created by Jumy on 16/6/20 16:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DocumentAdapter extends CommonAdapter<Node> {
    public DocumentAdapter(Context context, int layoutId, List<Node> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final Node node) {
        try {
            holder.setText(R.id.subtitle,"省委城市工作委会");
            holder.setText(R.id.supportingText, CardGenerator.generateNotifyString(0,new SparseArray<String>()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setList(List<Node> list){
        if (list != null){
            super.mDatas.clear();
            super.mDatas = list;
            notifyDataSetChanged();
        }
    }
}
