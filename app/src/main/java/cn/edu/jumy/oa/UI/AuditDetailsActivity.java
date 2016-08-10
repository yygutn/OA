package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.Relay2Response;
import cn.edu.jumy.oa.bean.Relay;
import cn.edu.jumy.oa.widget.customview.ItemTableRowAuditDetails_;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/5 16:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_audit_details)
public class AuditDetailsActivity extends BaseActivity {
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_done)
    TableLayout mTableDone;
    @ViewById(R.id.sign_details_table_undo)
    TableLayout mTableUndo;

    @Extra("id")
    String id = "";
    @Extra("type")
    String type = "";

    ArrayList<Relay> mListUndo = new ArrayList<>();
    ArrayList<Relay> mListDone = new ArrayList<>();

    @AfterExtras
    public void getData() {
        OAService.findapproved(id, type, new RelayCallBack());
    }

    @AfterViews
    void go() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateView() {
        for (Relay auditUser : mListDone) {
            mTableDone.addView(ItemTableRowAuditDetails_.build(mContext, auditUser, this));
        }
        for (Relay auditUser : mListUndo) {
            mTableUndo.addView(ItemTableRowAuditDetails_.build(mContext, auditUser, this));
        }
    }

    private void removeItemViews(TableLayout tableLayout) {
        try {
            tableLayout.removeAllViews();
            tableLayout.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_item_table_row_audit_details, null));
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    private class RelayCallBack extends Callback<Relay2Response> {

        @Override
        public Relay2Response parseNetworkResponse(Response response, int ID) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, Relay2Response.class);
            } else {
                return new Relay2Response(baseResponse);
            }
        }

        @Override
        public void onError(Call call, Exception e, int ID) {
            showToast("服务器错误,获取审批意见失败");
        }

        @Override
        public void onResponse(Relay2Response response, int ID) {
            if (response.code == 0) {
                if (response.data == null) return;
                mListUndo.clear();
                mListDone.clear();
                removeItemViews(mTableDone);
                removeItemViews(mTableUndo);
                for (Relay relay : response.data) {
                    if (relay.remark.equals("0")){
                        mListDone.add(relay);
                    }else {
                        mListUndo.add(relay);
                    }
                }
                updateView();
            } else {
                showToast(response.msg);
            }
        }
    }
}
