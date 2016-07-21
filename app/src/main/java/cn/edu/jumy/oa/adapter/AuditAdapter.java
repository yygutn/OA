package cn.edu.jumy.oa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.bean.AuditUser;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/6 12:52.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditAdapter extends CommonAdapter<AuditUser> {
    public AuditAdapter(Context context, int layoutId, List datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(final ViewHolder holder, final AuditUser user) {
        int position = holder.getLayoutPosition();
        Logger.d(position + "," + user.oid + "," + (position == 0 ? "" : mDatas.get(position - 1).oid));
        if (position == 0 || !user.oid.equals(mDatas.get(position - 1).oid)) {
            holder.setVisible(R.id.audit_meet_header, true);
            holder.setText(R.id.audit_meet_header_tv, user.organame);
        }
        holder.setText(R.id.audit_item_name, user.uname);
        holder.setOnClickListener(R.id.audit_item_pass, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letGo(user,true,holder.getLayoutPosition(),holder);
            }
        });
        holder.setOnClickListener(R.id.audit_item_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letGo(user,false,holder.getLayoutPosition(),holder);
            }
        });

    }

    /**
     * 承办方：会议报名审核
     * @param user
     * @param pass true 通过  false 退回
     * @param position
     * @param holder
     */
    private void letGo(AuditUser user, final boolean pass, final int position, final ViewHolder holder){
        OAService.meetUserPass(user.id,String.valueOf(pass),"", new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Toast.makeText(mContext,"当前网络不可用,审核报名失败",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                if (baseResponse.code == 0){
                    //审核通过/退回
                    Toast.makeText(mContext, "审核通过", Toast.LENGTH_SHORT).show();
//                    holder.setText(R.id.audit_item_pass,"已通过");
                    Intent intent = new Intent("POSITION");
                    intent.putExtra("passed_position",position);
                    intent.putExtra("passed_flag",pass);
                    mContext.sendBroadcast(intent);
                } else {
                    Toast.makeText(mContext,"当前网络不可用,审核报名失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setList(ArrayList<AuditUser> list) {
        if (list != null) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }
}
