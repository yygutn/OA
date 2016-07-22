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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.UI.TaskItem.SignDetailsActivity;
import cn.edu.jumy.oa.bean.AuditUser;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/6 12:52.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditAdapter2 extends CommonAdapter<AuditUser> {
    public AuditAdapter2(Context context, int layoutId, List datas) {
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
        holder.setText(R.id.audit_item_pass, "催收");
        holder.setOnClickListener(R.id.audit_item_pass, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letGo(user,true,holder.getLayoutPosition(),holder);
            }
        });
        holder.setVisible(R.id.audit_item_back,false);

    }

    /**
     * 会议/公文：催收
     * @param user
     * @param pass true 通过  false 退回
     * @param position
     */
    private void letGo(AuditUser user, final boolean pass, final int position, final ViewHolder holder){
        if (SignDetailsActivity.flag){
            //公文
            OAService.docUrge(user.oid, user.did, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(mContext,"网络异常,催收失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                    if (baseResponse.code == 0){
                        holder.setText(R.id.audit_item_pass, "已催收");
                        holder.getView(R.id.audit_item_pass).setClickable(false);
                    } else {
                        Toast.makeText(mContext,"催收失败,"+baseResponse.msg,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //会议
            OAService.meetUrge(user.oid, user.did, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(mContext,"网络异常,催收失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                    if (baseResponse.code == 0){
                        holder.setText(R.id.audit_item_pass, "已催收");
                        holder.getView(R.id.audit_item_pass).setClickable(false);
                    } else {
                        Toast.makeText(mContext,"催收失败,"+baseResponse.msg,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void setList(ArrayList<AuditUser> list) {
        if (list != null) {
            mDatas.clear();
            mDatas.addAll(list);
            notifyDataSetChanged();
        }
    }
}
