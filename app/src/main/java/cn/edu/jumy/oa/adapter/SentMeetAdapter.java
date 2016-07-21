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
    public void convert(ViewHolder holder, final Meet meet) {
        holder.setText(R.id.item_text,meet.docTitle);
        holder.setOnClickListener(R.id.item_text_button, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(meet.id)){
                    return;
                }
                SignDetailsActivity.flag = false;
                SignDetailsActivity_.intent(mContext).extra("id",meet.id).start();
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
