package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.adapter.MultiSignListAdapter;
import cn.edu.jumy.oa.bean.Sign;
import okhttp3.Call;

/**
 * 报名
 * Created by Jumy on 16/7/1 11:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_multi)
@OptionsMenu(R.menu.add_submit)
public class SignUpMultiAbleActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.recView)
    protected PullToRefreshRecyclerView mListView;
    MultiSignListAdapter adapter;

    ArrayList<Sign> mList = new ArrayList<>();


    @Extra("pid")
    String pid = "";

    @Extra("tid")
    String tid = "";

    @AfterExtras
    void go() {
        if (TextUtils.isEmpty(pid)) {
            return;
        }
        //获取个人报名信息,,,waiting
    }

    /**
     * 初始化--控件绑定之后
     */
    @AfterViews
    void start() {
        setSupportActionBar(mTitleBar);
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });

        mListView.setLayoutManager(new LinearLayoutManager(this));

        initAdapter();
    }

    private void initAdapter() {
        adapter = new MultiSignListAdapter(mContext, R.layout.item_notify_notification, mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Sign node = mList.get(position);
                SignUpAddActivity_.intent(mContext).extra("tid", tid).extra("pid", pid).extra("old", node).startForResult(32);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }

    @OptionsItem(R.id.action_add)
    void skipToAddNewOne() {
        SignUpAddActivity_.intent(mContext).extra("tid", tid).extra("pid", pid).startForResult(16);
    }


    AlertDialog alertDialog;

    /**
     * 上传（个人/单位）会议报名信息
     */
    @OptionsItem(R.id.action_submit)
    void submit() {
        alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("确认报名")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String data = new Gson().toJson(mList);
                        OAService.insertListMEntry(data, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int ID) {
                                showToast("报名失败");
                            }

                            @Override
                            public void onResponse(String response, int ID) {
                                Gson gson = new Gson();
                                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                                if (baseResponse.code == 0) {
                                    showToast("报名成功");
                                } else {
                                    showToast("报名失败");
                                }
                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 新添加的数据
     *
     * @param resultCode 返回码
     * @param data       数据
     */
    @OnActivityResult(16)
    void onResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Sign sign = data.getExtras().getParcelable("node");
            if (sign != null) {
                boolean flag = true;
                for (Sign node : mList) {
                    if (node.name.equals(sign.name)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    mList.add(sign);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 修改的报名信息
     *
     * @param resultCode 返回码
     * @param data       数据
     */
    @OnActivityResult(32)
    void onResultFix(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            int position = data.getIntExtra("position", -1);
            Sign sign = data.getExtras().getParcelable("node");
            if (sign != null) {
                boolean flag = true;
                for (Sign node : mList) {
                    if (node.name.equals(sign.name)) {
                        flag = false;
                        break;
                    }
                }
                if (flag && position >= 0) {
                    mList.set(position, sign);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
