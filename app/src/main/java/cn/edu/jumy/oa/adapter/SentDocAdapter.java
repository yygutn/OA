package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.SignDetailsActivity;
import cn.edu.jumy.oa.UI.TaskItem.SignDetailsActivity_;
import cn.edu.jumy.oa.bean.Doc;

/**
 * Created by Jumy on 16/7/13 11:42.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class SentDocAdapter extends CommonAdapter<Doc>{
    public SentDocAdapter(Context context, int layoutId, List<Doc> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, final Doc doc) {
        holder.setText(R.id.item_text,doc.docTitle);
        holder.setOnClickListener(R.id.item_text_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(doc.id)){
                    return;
                }
                SignDetailsActivity.flag = true;
                SignDetailsActivity_.intent(mContext).extra("id",doc.id).start();
            }
        });
    }


    public void setList(List<Doc> list){
        if (list != null){
            super.mDatas.clear();
            super.mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }
}
