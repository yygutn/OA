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
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.AuditCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AuditResponse;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.widget.customview.ItemTableRowSignDetails_;

/**
 * Created by Jumy on 16/7/14 15:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_details)
public class SignDetailsActivity extends BaseActivity {

    public static final String UPDATE = "SignDetailsActivity.UPDATE";

    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_signed)
    TableLayout mTableSigned;
    @ViewById(R.id.sign_details_table_unsigned)
    TableLayout mTableUnSign;

    public static boolean flag = true;

    @Extra("id")
    String id = "";

    ArrayList<AuditUser> mListSigned = new ArrayList<>();
    ArrayList<AuditUser> mListUnSign = new ArrayList<>();


    @AfterViews
    void go() {
        getData();
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
    }

    private void getData() {
        removeItemViews(mTableSigned);
        removeItemViews(mTableUnSign);
        mListUnSign.clear();
        mListSigned.clear();
        OAService.getCheckInfo(id, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                if (response.code == 0) {
                    for (AuditUser user : response.data) {
                        if (user.signStatus == 0) {
                            mListSigned.add(user);
                        } else {
                            mListUnSign.add(user);
                        }
                    }
                    updateView();
                } else {
                    if (!TextUtils.isEmpty(response.msg)) {
                        showToast(response.msg);
                    }
                }
            }
        });
    }

    private void removeItemViews(TableLayout tableLayout) {
        try {
            tableLayout.removeAllViews();
            tableLayout.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_item_table_row_sign_details, null));
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    private void updateView() {
        for (AuditUser auditUser : mListUnSign) {
            mTableUnSign.addView(ItemTableRowSignDetails_.build(mContext, auditUser, this));
        }
        for (AuditUser auditUser : mListSigned) {
            mTableSigned.addView(ItemTableRowSignDetails_.build(mContext, auditUser, this));
        }
    }
}
