package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.bean.Sign;
import okhttp3.Call;

/**
 * 报名
 * Created by Jumy on 16/7/1 11:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up)
@OptionsMenu(R.menu.sign_up_details)
public class SignUpMultiAbleActivity extends BaseActivity {
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
    @ViewById(R.id.sign_up_man_button)
    CheckBox signUpManButton;
    @ViewById(R.id.sign_up_women_button)
    CheckBox signUpWomenButton;
    int sex = 0;

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
        mToolBar.setTitle("会议报名");
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

    @OptionsItem(R.id.action_details)
    void skipToDetails() {
        SignUpDetailsActivity_.intent(mContext).extra("tid", tid).extra("pid", pid).start();
    }

    @OptionsItem(R.id.action_multi)
    void skipToMulti() {
        SignUpMultiActivity_.intent(mContext).extra("tid", tid).extra("pid", pid).start();
    }

    /**
     * 三个会议状态的点击事件
     *
     * @param view
     */
    @Click({R.id.sign_up_ll_join, R.id.sign_up_ll_leave, R.id.sign_up_ll_listen, R.id.sign_up_ll_man, R.id.sign_up_ll_women})
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
            case R.id.sign_up_ll_women: {
                sex = 1;
                setSex(true, false);
                break;
            }
            case R.id.sign_up_ll_man: {
                sex = 0;
                setSex(false, true);
                break;
            }
            default:
                break;
        }
    }

    private void setSex(boolean checked, boolean checked2) {
        signUpWomenButton.setChecked(checked);
        signUpManButton.setChecked(checked2);
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
        Map<String, String> params = new HashMap<>();
        params.put("editType", editType);
        params.put("pid", tid);
        params.put("name", name);
        params.put("post", position);
        params.put("type", status + "");
        params.put("sex", sex + "");
        params.put("phone", tel);
        params.put("remark", remark);
        OAService.updateMEntry(params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int ID) {
                showToast("报名失败");
            }

            @Override
            public void onResponse(String response, int ID) {
                BaseResponse baseResponse = new Gson().fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
                    showToast("报名成功");
                } else {
                    showToast("报名失败" + (TextUtils.isEmpty(baseResponse.msg) ? "" : ("," + baseResponse.msg)));
                }
            }
        });
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
