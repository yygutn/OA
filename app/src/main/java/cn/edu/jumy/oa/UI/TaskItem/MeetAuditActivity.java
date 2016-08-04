package cn.edu.jumy.oa.UI.TaskItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;

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
import cn.edu.jumy.oa.widget.customview.ItemTableRowAudit_;

/**
 * Created by Jumy on 16/7/6 11:37.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * 会议审核详情
 */
@EActivity(R.layout.activity_meet_audit)
public class MeetAuditActivity extends BaseActivity {
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_join)
    TableLayout mTableSigned;
    @ViewById(R.id.sign_details_table_unsigned)
    TableLayout mTableUnPass;
    @ViewById(R.id.sign_details_table_listen)
    TableLayout mTablePassed;

    AuditAdapter adapter;

    public static final String DELETE = "TABLE_AUDIT_ITEM";

    //会议ID
    @Extra("mid")
    String mid = "";


    ArrayList<AuditUser> mListSigned = new ArrayList<>();
    ArrayList<AuditUser> mListPassed = new ArrayList<>();
    ArrayList<AuditUser> mListUnPass = new ArrayList<>();

    @AfterExtras
    protected void initList() {
        if (TextUtils.isEmpty(mid)) {
            backToPreActivity();
            return;
        }
        OAService.getMEntryByPassStatus(mid, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                if (response.code == 0 && response.data != null) {
                    removeItemViews(mTableUnPass);
                    removeItemViews(mTableSigned);
                    removeItemViews(mTablePassed);
                    mListSigned.clear();
                    mListPassed.clear();
                    mListUnPass.clear();
                    for (AuditUser auditUser : response.data) {
                        switch (auditUser.passStatus) {
                            case 0: {
                                mListPassed.add(auditUser);
                                break;
                            }
                            case 1: {
                                mListUnPass.add(auditUser);
                                break;
                            }
                            default: {
                                mListSigned.add(auditUser);
                                break;
                            }
                        }
                    }
                    updateView();
                }
            }
        });
    }


    @AfterViews
    void go() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        registerReceiver(deleteReceiver,new IntentFilter(DELETE));
    }

    private void removeItemViews(TableLayout tableLayout) {
        try {
            tableLayout.removeAllViews();
            tableLayout.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_item_table_row_audit, null));
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    private void updateView() {
        for (AuditUser auditUser : mListSigned) {
            mTableSigned.addView(ItemTableRowAudit_.build(mContext, auditUser, this));
        }
        for (AuditUser auditUser : mListPassed) {
            mTablePassed.addView(ItemTableRowAudit_.build(mContext, auditUser, this));
        }
        for (AuditUser auditUser : mListUnPass) {
            mTableUnPass.addView(ItemTableRowAudit_.build(mContext, auditUser, this));
        }
    }

    @Click(R.id.out)
    void click() {
        showToast("导出...");
    }

    BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DELETE)) {
                initList();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (deleteReceiver != null) {
            unregisterReceiver(deleteReceiver);
        }
    }
}
