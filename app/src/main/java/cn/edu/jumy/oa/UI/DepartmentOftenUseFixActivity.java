package cn.edu.jumy.oa.UI;

import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.OrganizationOftenCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AccountResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.OrganizationOftenResponse;
import cn.edu.jumy.oa.adapter.DepartmentAdapter;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.bean.OrganizationOften;
import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexHeaderEntity;
import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexableStickyListView;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Jumy on 16/7/15 09:35.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_add_department)
@OptionsMenu(R.menu.ok)
public class DepartmentOftenUseFixActivity extends BaseActivity {
    @ViewById(R.id.searchview)
    SearchView mSearchView;
    @ViewById(R.id.indexListView)
    IndexableStickyListView mIndexListView;

    @ViewById(R.id.edit_often)
    EditText mOftenEt;

    DepartmentAdapter adapter;
    @ViewById(R.id.title_bar)
    Toolbar mTitleBar;

    //初始化数据
    ArrayList<Account> mDepartments = new ArrayList<>();

    @Extra("org")
    OrganizationOften mOrg;

    @Extra("titleList")
    ArrayList<String> mTitleList = new ArrayList<>();

    @AfterExtras
    void getData() {
        OAService.getOrganizationData(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showDebugException(e);
                showToast("网络异常，获取可发送单位失败");
            }

            @Override
            public void onBefore(Request request, int id) {
                super.onBefore(request, id);
            }

            @Override
            public void onResponse(String response, int id) {
                try {

                    Gson gson = new Gson();
                    AccountResponse account = gson.fromJson(response, AccountResponse.class);
                    if (account.code != 0) {
                        showToast("获取可发送单位失败");
                    } else {
                        mDepartments = account.data;
                        if (TextUtils.isEmpty(mOrg.value)){
                            mOrg.value = "";
                        }
                        String[] ids = mOrg.value.split(",");
                        int len = ids.length;
                        for (int i = 0; i < len; i++) {
                            for (Account account1 : mDepartments) {
                                if (account1.id.equals(ids[i])) {
                                    account1.checked = true;
                                }
                            }
                        }
                        updateView();
                    }
                } catch (Exception e) {
                    showDebugException(e);
                }
            }
        });
    }

    @AfterViews
    void go() {
        setSupportActionBar(mTitleBar);
        mTitleBar.setTitle("修改常用单位");
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //init ListView && adapter
        adapter = new DepartmentAdapter(mContext);
        mIndexListView.setAdapter(adapter);

        updateView();

        // 搜索
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 委托处理搜索
                mIndexListView.searchTextChange(newText);
                return true;
            }
        });
        mOftenEt.setText(mOrg.name);
        setResult(RESULT_OK);
    }

    private void updateView() {
        //bind data
        mIndexListView.bindDatas(mDepartments);
    }

    /**
     * 确认修改单位，提交到服务器
     */
    @OptionsItem(R.id.action_ok)
    void submit() {
        String name = mOftenEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("常用单位组名不能为空");
            return;
        }
        for (String title : mTitleList) {
            if (title.equals(name) && !title.equals(mOrg.name)){
                showToast("该组名已存在");
            }
        }
        String str = "";
        String ids = "";
        for (Account account : mDepartments) {
            if (account.checked) {
                if (TextUtils.isEmpty(str)) {
                    str = account.name;
                } else {
                    str += "," + account.name;
                }
                if (TextUtils.isEmpty(ids)) {
                    ids = account.id;
                } else {
                    ids += "," + account.id;
                }
            }
        }
        OAService.updateMagroup(mOrg.id, ids, name, 0, new StringCallback() {

            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("服务器错误,修改失败");
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
                    showToast("修改成功");
                    onBackPressed();
                } else {
                    showToast("修改失败:" + baseResponse.msg);
                }
            }
        });
    }
}
