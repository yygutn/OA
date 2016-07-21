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
import okhttp3.Call;
import okhttp3.Request;

/**
 * 报名
 * Created by Jumy on 16/7/1 11:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up)
@OptionsMenu(R.menu.sign_up_details)
public class SignUpActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;
    @ViewById(R.id.sign_up_name)
    protected AppCompatEditText mSignUpName;
    @ViewById(R.id.sign_up_position)
    protected AppCompatEditText mSignUpPosition;
    @ViewById(R.id.sign_up_phone)
    protected AppCompatEditText mSignUpPhone;
    @ViewById(R.id.dropDownMenu)
    protected AppCompatTextView mUnitTextView;
    @ViewById(R.id.sign_up_join_button)
    protected CheckBox mSignUpJoinButton;
    @ViewById(R.id.sign_up_listen_button)
    protected CheckBox mSignUpListenButton;
    @ViewById(R.id.sign_up_leave_button)
    protected CheckBox mSignUpLeaveButton;
    @ViewById(R.id.sign_up_leave)
    protected AppCompatEditText mSignUpLeave;
    @ViewById(R.id.submit)
    protected TextView mSubmit;
    //所在单位
    String mUnit = "";
    String mUnitID = "";

    //基本信息
    String name, position, tel, remark;
    int status = -1;

    @Extra("pid")
    String pid = "";

    String id = "";

    @Extra("tid")
    String tid = "";

    @AfterExtras
    void go() {
        if (TextUtils.isEmpty(pid)){
            return;
        }
        //获取个人报名信息,,,waiting
    }

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
    }

    @OptionsItem(R.id.action_details)
    void skipToDetails() {
        SignUpDetailsActivity_.intent(mContext).extra("tid",tid).extra("pid",pid).start();
    }

    /**
     * 三个会议状态的点击事件
     *
     * @param view
     */
    @Click({R.id.sign_up_ll_join, R.id.sign_up_ll_leave, R.id.sign_up_ll_listen, R.id.dropDownMenu})
    void click(View view) {
        switch (view.getId()) {
            case R.id.sign_up_ll_join: {//参会
                resetButton();
                mSignUpJoinButton.setChecked(true);
                status = 0;
                break;
            }
            case R.id.sign_up_ll_leave: {//请假
                resetButton();
                mSignUpLeaveButton.setChecked(true);
                status = 2;
                break;
            }
            case R.id.sign_up_ll_listen: {//听会
                resetButton();
                mSignUpListenButton.setChecked(true);
                status = 1;
                break;
            }
            case R.id.dropDownMenu: {
                DepartmentSelectActivity_.intent(mContext).startForResult(0);
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
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("editType", "add");//(add:添加   edit:修改)
                        params.put("id", id);//报名表人员id(修改必填项)
                        params.put("pid", pid);//会议id(添加必填项，修改无效)
                        params.put("name", name);//姓名(添加必填项，修改不必填，下同)
                        params.put("post", position);//职位
                        params.put("sex", "");//性别(0:男  1:女)
                        params.put("phone", tel);//电话(不必填)
                        params.put("type", status+"");//状态(0:参会 1:听会 2:请假)
                        params.put("remark", remark);//备注
                        OAService.updateMEntry(params, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showToast("报名失败");
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                                if (baseResponse.code == 0){
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

    private void dealPreWork() {
        name = mSignUpName.getText().toString();
        position = mSignUpPosition.getText().toString();
        tel = mSignUpPhone.getText().toString();
        remark = mSignUpLeave.getText().toString();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position) || status == -1) {
            showToast("报名信息不完善,请再次确认");
            return;
        }
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

    /**
     * 承办单位
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(0)
    void result1(int resultCode, Intent data) {
        if (resultCode == 0) {
            mUnitID = data.getStringExtra("ids");
            mUnit = data.getStringExtra("str");
            mUnitTextView.setText(mUnit);
        } else if (resultCode == 1) {
            //没有选择接收单位
            mUnit = mUnitID = "";
        }
    }
}
