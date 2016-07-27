package cn.edu.jumy.oa.adapter;

import android.content.Context;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Sign;

/**
 * Created by Jumy on 16/7/27 17:01.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class MultiSignListAdapter extends CommonAdapter<Sign>{
    public MultiSignListAdapter(Context context, int layoutId, List<Sign> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Sign sign) {
        holder.setText(R.id.subtitle,sign.name);
    }
}
