package cn.edu.jumy.oa.UI;

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
import cn.edu.jumy.oa.widget.customview.ItemTableRow_;

/**
 * Created by Jumy on 16/7/5 16:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity
public class SignUpDetailsActivity extends BaseActivity {
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_signed)
    TableLayout mTableSigned;
    @ViewById(R.id.sign_details_table_unsigned)
    TableLayout mTableUnsigned;
    @ViewById(R.id.sign_details_skip_approval)
    Button mSignApproval;

    @Extra("tid")
    String tid = "";

    @Extra("pid")
    String pid = "";

    ArrayList<AuditUser> mListLeaved = new ArrayList<>();
    ArrayList<AuditUser> mListSigned = new ArrayList<>();

    /**
     * 进度框
     */
    private LoadingDialog mLoadingDialog;
    Handler mHandler = new Handler();

    @AfterExtras
    void aVoid() {

        OAService.getMEntryByPid(tid, new AuditCallback() {
            @Override
            public void onResponse(AuditResponse response, int id) {
                mListLeaved.clear();
                mListSigned.clear();
                for (AuditUser auditUser : response.data) {
                    if (auditUser.type == 2) {
                        mListLeaved.add(auditUser);
                    }
                }
                mListSigned.addAll(response.data);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.dismiss();
                        setContentView(R.layout.activity_sign_up_details);
                        updateView();
                    }
                }, 1500);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoadingDialog = new LoadingDialog();
        mLoadingDialog.show(getSupportFragmentManager(), LoadingDialog.TAG);
    }

    @AfterViews
    void go() {
        mLoadingDialog = null;
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void updateView() {
        for (AuditUser auditUser : mListSigned) {
            mTableSigned.addView(ItemTableRow_.build(mContext,auditUser));
        }
        for (AuditUser auditUser : mListLeaved) {
            mTableUnsigned.addView(ItemTableRow_.build(mContext,auditUser));
        }
    }

    @Click(R.id.sign_details_skip_approval)
    void click() {
        MeetAuditActivity_.intent(mContext).extra("mid",pid).start();
    }
}
