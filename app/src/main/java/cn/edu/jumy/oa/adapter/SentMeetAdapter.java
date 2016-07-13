package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.view.View;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Meet;

/**
 * Created by Jumy on 16/7/13 11:42.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class SentMeetAdapter extends CommonAdapter<Meet>{
    public SentMeetAdapter(Context context, int layoutId, List<Meet> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Meet meet) {
        holder.setText(R.id.item_text,meet.docTitle);
        holder.setOnClickListener(R.id.item_text_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    public void setList(List<Meet> list){
        if (list != null){
            super.mDatas.clear();
            super.mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }
}
