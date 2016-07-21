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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.AuditCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AuditResponse;
import cn.edu.jumy.oa.adapter.AuditAdapter;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.bean.Meet;

/**
 * Created by Jumy on 16/7/6 11:37.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * 会议审核详情
 */
@EActivity(R.layout.activity_meet_audit)
public class MeetAuditActivity extends BaseActivity {
    @ViewById(R.id.audit_listView)
    RecyclerView mAuditListView;
    @ViewById(R.id.audit_pass)
    TextView mAuditPass;
    @ViewById(R.id.audit_not_pass)
    TextView mAuditNotPass;
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;

    AuditAdapter adapter;

    //会议ID
    @Extra("mid")
    String mid = "";

    @AfterExtras
    void init() {
        initList();
    }

    String mPassed = "";
    String mNotPass = "";

    ArrayList<AuditUser> list = new ArrayList<>();
    ArrayList<AuditUser> list_signed = new ArrayList<>();
    ArrayList<AuditUser> list_passed = new ArrayList<>();
    ArrayList<AuditUser> list_un_pass = new ArrayList<>();

    BroadcastReceiver successBroadCastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("POSITION")) {
                int position = intent.getIntExtra("passed_position", -1);
                boolean flag = intent.getBooleanExtra("passed_flag", true);
                if (position != -1) {
                    AuditUser user = list_signed.get(position);
                    list_signed.remove(user);
                    if (flag) {
                        user.passStatus = 0;
                    } else {
                        user.passStatus = 1;
                    }
                    switch (user.passStatus) {
                        case 0: {
                            list_passed.add(user);
                            if (TextUtils.isEmpty(mPassed)) {
                                mPassed = user.name;
                            } else {
                                mPassed += "、" + user.name;
                            }
                            break;
                        }
                        case 1: {
                            list_un_pass.add(user);
                            if (TextUtils.isEmpty(mNotPass)) {
                                mNotPass = user.name;
                            } else {
                                mNotPass += "、" + user.name;
                            }
                            break;
                        }
                    }
                    adapter.setList(list_signed);
                    mAuditPass.setText(mPassed);
                    mAuditNotPass.setText(mNotPass);
                }
            }
        }
    };

    @AfterViews
    void go() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        adapter = new AuditAdapter(mContext, R.layout.item_audit_meet, list);

        mAuditListView.setLayoutManager(new LinearLayoutManager(mContext));
        mAuditListView.setAdapter(adapter);


        IntentFilter filter = new IntentFilter();
        filter.addAction("POSITION");
        mContext.registerReceiver(successBroadCastReceiver, filter);
    }

    private void initList() {
        if (TextUtils.isEmpty(mid)) {
            return;
        }
        OAService.getMEntryByPassStatus(mid, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                if (response.code == 0 && response.data != null) {
                    list = response.data;
                    for (AuditUser auditUser : list) {
                        switch (auditUser.passStatus) {
                            case 0: {
                                list_passed.add(auditUser);
                                if (TextUtils.isEmpty(mPassed)) {
                                    mPassed = auditUser.name;
                                } else {
                                    mPassed += "、" + auditUser.name;
                                }
                                break;
                            }
                            case 1: {
                                list_un_pass.add(auditUser);
                                if (TextUtils.isEmpty(mNotPass)) {
                                    mNotPass = auditUser.name;
                                } else {
                                    mNotPass += "、" + auditUser.name;
                                }
                                break;
                            }
                            default: {
                                list_signed.add(auditUser);
                                break;
                            }
                        }
                    }
                    adapter.setList(list_signed);
                    mAuditPass.setText(mPassed);
                    mAuditNotPass.setText(mNotPass);
                }
            }
        });
    }

    @Click(R.id.out)
    void click() {
        showToast("导出...");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(successBroadCastReceiver);
    }
}
