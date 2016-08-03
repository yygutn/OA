package cn.edu.jumy.oa.UI;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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
import cn.edu.jumy.oa.UI.TaskItem.MeetAuditActivity_;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.fragment.LoadingDialog;
import cn.edu.jumy.oa.widget.customview.ItemTableRow;
import cn.edu.jumy.oa.widget.customview.ItemTableRow_;

/**
 * Created by Jumy on 16/7/5 16:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up_details)
public class SignUpDetailsActivity extends BaseActivity {
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_join)
    TableLayout mTableJoin;
    @ViewById(R.id.sign_details_table_unsigned)
    TableLayout mTableUnsigned;
    @ViewById(R.id.sign_details_table_listen)
    TableLayout mTableListen;

    public static final String DELETE = "TABLE_SIGN_ITEM";

    @Extra("tid")
    public String tid = "";

    @Extra("pid")
    public String pid = "";

    ArrayList<AuditUser> mListLeaved = new ArrayList<>();
    ArrayList<AuditUser> mListJoined = new ArrayList<>();
    ArrayList<AuditUser> mListListen = new ArrayList<>();

    ArrayList<ItemTableRow> mItemLeaved = new ArrayList<>();
    ArrayList<ItemTableRow> mItemJoined = new ArrayList<>();
    ArrayList<ItemTableRow> mItemListen = new ArrayList<>();

    @AfterExtras
    public void getData() {
        OAService.getMEntryByPid(tid, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                removeItemViews(mTableUnsigned);
                removeItemViews(mTableJoin);
                removeItemViews(mTableListen);
                mListLeaved.clear();
                mListJoined.clear();
                mListListen.clear();
                mItemLeaved.clear();
                mItemJoined.clear();
                mItemListen.clear();
                for (AuditUser auditUser : response.data) {
                    if (auditUser.type == 2) {
                        mListLeaved.add(auditUser);
                    } else if (auditUser.type == 1) {
                        mListListen.add(auditUser);
                    } else if (auditUser.type == 0) {
                        mListJoined.add(auditUser);
                    }
                }
                updateView();
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

        IntentFilter filter = new IntentFilter(DELETE);
        registerReceiver(deleteReceiver, filter);
    }

    private void updateView() {
        for (AuditUser auditUser : mListJoined) {
            mItemJoined.add(ItemTableRow_.build(mContext, auditUser, this));
            mTableJoin.addView(mItemJoined.get(mItemJoined.size() - 1));
        }
        for (AuditUser auditUser : mListListen) {
            mItemListen.add(ItemTableRow_.build(mContext, auditUser, this));
            mTableListen.addView(mItemListen.get(mItemListen.size() - 1));
        }
        for (AuditUser auditUser : mListLeaved) {
            mItemLeaved.add(ItemTableRow_.build(mContext, auditUser, this));
            mTableUnsigned.addView(mItemLeaved.get(mItemLeaved.size() - 1));
        }
    }

    private void removeItemViews(TableLayout tableLayout) {
        int len = tableLayout.getChildCount();
        for (int i = 1; i < len; i++) {
            tableLayout.removeViewAt(i);
        }
    }

    BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DELETE)) {
                getData();
            }
        }
    };
}
