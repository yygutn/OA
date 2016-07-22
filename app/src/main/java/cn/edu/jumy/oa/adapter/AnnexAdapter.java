package cn.edu.jumy.oa.adapter;

import android.content.Context;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Annex;

/**
 * Created by Jumy on 16/7/22 17:38.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AnnexAdapter extends CommonAdapter<Annex>{
    public AnnexAdapter(Context context, int layoutId, List<Annex> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Annex annex) {
        holder.setText(R.id.subtitle,annex.getFileName());
    }
    public void setList(ArrayList<Annex> list) {
        if (list != null) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }
}
