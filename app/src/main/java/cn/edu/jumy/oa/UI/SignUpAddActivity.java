package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
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

import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AccountResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.bean.Sign;
import okhttp3.Call;
import okhttp3.Request;

/**
 * 报名
 * Created by Jumy on 16/7/1 11:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up)
public class SignUpAddActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;
    @ViewById(R.id.sign_up_name)
    protected AppCompatEditText mSignUpName;
    @ViewById(R.id.sign_up_position)
    protected AppCompatEditText mSignUpPosition;
    @ViewById(R.id.sign_up_phone)
    protected AppCompatEditText mSignUpPhone;
    @ViewById(R.id.sign_up_join_button)
    protected CheckBox mSignUpJoinButton;
    @ViewById(R.id.sign_up_listen_button)
    protected CheckBox mSignUpListenButton;
    @ViewById(R.id.sign_up_leave_button)
    protected CheckBox mSignUpLeaveButton;
    @ViewById(R.id.sign_up_leave)
    protected AppCompatEditText mSignUpLeave;

    //基本信息
    String name, position, tel, remark;
    int status = -1;

    @Extra("pid")
    String pid = "";

    String id = "";

    @Extra("tid")
    String tid = "";

    @Extra("position")
    int _position = -1;

    @Extra("old")
    Sign node = null;

    @Extra("title")
    String title = "";

    @Extra("fromItem")
    boolean flag = false;

    String editType = "add";

    /**
     * 初始化--控件绑定之后
     */
    @AfterViews
    void start() {
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });

        if (node != null) {
            //来自修改信息界面
            editType = "edit";
            mSignUpName.setText(node.name);
            mSignUpPosition.setText(node.post);
            mSignUpPhone.setText(node.phone);
            mSignUpLeave.setText(node.remark);
            status = node.type;
            clickCheckBox(status);
        }
        if (flag && !TextUtils.isEmpty(title)){
            mToolBar.setTitle(title);
        }
    }

    private void clickCheckBox(int type) {
        switch (type) {
            case 0: {
                mSignUpJoinButton.setChecked(true);
                break;
            }
            case 1: {
                mSignUpListenButton.setChecked(true);
                break;
            }
            case 2: {
                mSignUpLeaveButton.setChecked(true);
                break;
            }

        }
    }

    /**
     * 三个会议状态的点击事件
     *
     * @param view
     */
    @Click({R.id.sign_up_ll_join, R.id.sign_up_ll_leave, R.id.sign_up_ll_listen})
    void click(View view) {
        switch (view.getId()) {
            case R.id.sign_up_ll_join: {//参会
                resetButton();
                status = 0;
                clickCheckBox(0);
                break;
            }
            case R.id.sign_up_ll_leave: {//请假
                resetButton();
                status = 2;
                clickCheckBox(2);
                break;
            }
            case R.id.sign_up_ll_listen: {//听会
                resetButton();
                status = 1;
                clickCheckBox(1);
                break;
            }
            default:
                break;
        }
    }

    AlertDialog alertDialog;

    /**
     * 上传（个人/单位）会议报名信息
     */
    @Click(R.id.submit)
    void submit() {
        clearFocus();
        alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("确认报名")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dealPreWork();
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void dealPreWork() {
        name = mSignUpName.getText().toString();
        position = mSignUpPosition.getText().toString();
        tel = mSignUpPhone.getText().toString();
        remark = mSignUpLeave.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position) || status == -1) {
            showToast("报名信息不完善,请再次确认");
            return;
        }


        if (node == null) {
            node = new Sign();
        }
        node.pid = tid;
        node.name = name;
        node.phone = tel;
        node.post = position;
        node.type = status;
        node.remark = remark;

        if (flag){
            Map<String,String> params = new HashMap<>();
            params.put("editType","edit");
            params.put("pid",node.pid);
            params.put("name",name);
            params.put("id",node.id);
            params.put("post",position);
            params.put("type",status+"");
            params.put("sex","");
            params.put("phone",tel);
            params.put("remark",remark);
            OAService.updateMEntry(params, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int ID) {
                    showToast("报名失败");
                }

                @Override
                public void onResponse(String response, int ID) {
                    BaseResponse baseResponse = new Gson().fromJson(response,BaseResponse.class);
                    if (baseResponse.code == 0){
                        showToast("报名成功");
                    } else {
                        showToast("报名失败"+(TextUtils.isEmpty(baseResponse.msg)?"":(","+baseResponse.msg)));
                    }
                }
            });
            backToPreActivity();
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("node", node);
        intent.putExtra("position", _position);
        setResult(RESULT_OK, intent);

        backToPreActivity();
    }


    /**
     * 重置按钮和文本焦点
     */
    private void resetButton() {
        mSignUpLeaveButton.setChecked(false);
        mSignUpJoinButton.setChecked(false);
        mSignUpListenButton.setChecked(false);
        mSignUpLeave.clearFocus();
    }

    /**
     * 清除焦点
     */
    private void clearFocus() {
        mSignUpName.clearFocus();
        mSignUpPosition.clearFocus();
        mSignUpPhone.clearFocus();
        mSignUpLeave.clearFocus();
    }
}
