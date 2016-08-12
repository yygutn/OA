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
import android.widget.Toast;

import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.AuditCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AuditResponse;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.adapter.AuditAdapter;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.widget.customview.ItemTableRowAudit_;
import cn.qqtheme.framework.picker.FilePicker;
import okhttp3.Call;

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
    TableLayout mTableLeaved;
    @ViewById(R.id.sign_details_table_listen)
    TableLayout mTableListen;

    AuditAdapter adapter;

    public static final String DELETE = "TABLE_AUDIT_ITEM";

    //会议ID
    @Extra("mid")
    String mid = "";
    //会议时间
    @Extra("time")
    long time = -1;


    ArrayList<AuditUser> mListSigned = new ArrayList<>();
    ArrayList<AuditUser> mListListen = new ArrayList<>();
    ArrayList<AuditUser> mListLeaved = new ArrayList<>();

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
                    removeItemViews(mTableLeaved);
                    removeItemViews(mTableSigned);
                    removeItemViews(mTableListen);
                    mListSigned.clear();
                    mListListen.clear();
                    mListLeaved.clear();
                    for (AuditUser auditUser : response.data) {
                        switch (auditUser.type) {
                            case 1: {
                                mListListen.add(auditUser);
                                break;
                            }
                            case 2: {
                                mListLeaved.add(auditUser);
                                break;
                            }
                            case 0: {
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

        registerReceiver(deleteReceiver, new IntentFilter(DELETE));
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
        for (AuditUser auditUser : mListListen) {
            mTableListen.addView(ItemTableRowAudit_.build(mContext, auditUser, this));
        }
        for (AuditUser auditUser : mListLeaved) {
            mTableLeaved.addView(ItemTableRowAudit_.build(mContext, auditUser, this));
        }
    }

    String fileName = "";

    @Click(R.id.out)
    void click() {
        FilePicker filePicker = new FilePicker(this, FilePicker.DIRECTORY);
        filePicker.setTitleText("请选择导出文件夹位置");
        filePicker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(final String currentPath) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
                if (time > 0){
                    fileName = sdf.format(new Date(time));
                } else {
                    fileName = sdf.format(new Date());
                }
                fileName += ".xlsx";
                OAService.passExcel(mid, new FileCallBack(currentPath, fileName) {
                    @Override
                    public void syncSaveToSQL(File file) {

                    }

                    @Override
                    public void onError(Call call, Exception e, int ID) {
                        showToast("导出列表失败");
                    }

                    @Override
                    public void onResponse(File response, int ID) {
                        showToast("导出成功,文件保存在"+currentPath+"/"+fileName, Toast.LENGTH_LONG);
                        CallOtherOpenFile.openFile(mContext,response);
                    }
                });
            }
        });
        filePicker.show();
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
