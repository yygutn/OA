package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.bean.User;

/**
 * Created by Jumy on 16/7/6 12:52.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditAdapter extends CommonAdapter<AuditUser> {
    public AuditAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, AuditUser user) {
        int position = holder.getLayoutPosition();
        Logger.d(position +","+ user.oid+","+(position==0?"":mDatas.get(position - 1).oid));
        if (position == 0 || !user.oid.equals(mDatas.get(position - 1).oid)) {
            holder.setVisible(R.id.audit_meet_header, true);
            holder.setText(R.id.audit_meet_header_tv, user.oid);
        }
        holder.setText(R.id.audit_item_name, user.name);
        holder.setOnClickListener(R.id.audit_item_pass, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/7/6 通过
                Logger.d("pass");
            }
        });
        holder.setOnClickListener(R.id.audit_item_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: 16/7/6 退回
                Logger.d("back");
            }
        });

    }
}
