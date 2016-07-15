package cn.edu.jumy.oa.UI.TaskItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.AuditCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AuditResponse;
import cn.edu.jumy.oa.adapter.AuditAdapter2;
import cn.edu.jumy.oa.bean.AuditUser;

/**
 * Created by Jumy on 16/7/14 15:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_audit)
public class SignDetailsActivity extends BaseActivity{
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.audit_listView)
    RecyclerView mListView;
    @ViewById(R.id.audit_pass)
    TextView mPass;

    @Extra("id")
    String id = "";

    String mPassed = "";

    AuditAdapter2 adapter;
    ArrayList<AuditUser> list = new ArrayList<>();
    ArrayList<AuditUser> list_signed = new ArrayList<>();
    ArrayList<AuditUser> list_un_signed = new ArrayList<>();

    BroadcastReceiver successBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("POSITION")) {
                int position = intent.getIntExtra("passed_position", -1);
                if (position != -1) {
                    AuditUser user = list_un_signed.get(position);
                    list_un_signed.remove(user);
                    user.passStatus = 0;
                    list_signed.add(user);
                    dealStr();
                }
            }
        }
    };

    @AfterViews
    void go(){
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });

        adapter = new AuditAdapter2(mContext,R.layout.item_audit_meet,new ArrayList(list));
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setAdapter(adapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction("POSITION");
        mContext.registerReceiver(successBroadCastReceiver, filter);
    }

    @AfterExtras
    void after(){
        OAService.getCheckInfo(id, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                if (response.code == 0){
                    list = response.data;
                    for (AuditUser user : list){
                        if (user.passStatus == 0){
                            list_signed.add(user);
                        } else {
                            list_un_signed.add(user);
                        }
                    }
                    adapter.setList(new ArrayList<>(list_un_signed));
                    dealStr();
                } else {
                    if (!TextUtils.isEmpty(response.msg)){
                        showToast(response.msg);
                    }
                }
            }
        });
    }

    private void dealStr(){
        mPassed = "";
        for (AuditUser auditUser : list_signed){
            if (TextUtils.isEmpty(mPassed)){
                mPassed = auditUser.uname;
            } else {
                mPassed += "、"+auditUser.uname;
            }
        }
        mPass.setText(mPassed);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(successBroadCastReceiver);
    }
}
