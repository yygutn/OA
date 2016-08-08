package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.OrgRelayResponse;
import cn.edu.jumy.oa.adapter.DepartmentAdapter;
import cn.edu.jumy.oa.adapter.OrgRelayAdapter;
import cn.edu.jumy.oa.bean.OrgRelay;
import cn.edu.jumy.oa.widget.IndexableStickyListView.IndexableStickyListView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/15 09:35.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_pick_department)
@OptionsMenu(R.menu.org_relay_submit)
public class OrgRelaySelectActivity extends BaseActivity {
    @ViewById(R.id.searchview)
    SearchView mSearchView;
    @ViewById(R.id.indexListView)
    IndexableStickyListView mIndexListView;

    OrgRelayAdapter adapter;
    @ViewById(R.id.title_bar)
    Toolbar mTitleBar;

    @Extra("type")
    int type = -1;

    @Extra("id")
    String id = "";

    @AfterExtras
    void identify() {
        if (type == -1 || TextUtils.isEmpty(id)) {
            backToPreActivity();
        }
    }


    ArrayList<OrgRelay> mList = new ArrayList<>();

    private void getMainList() {

        OAService.getOrganizationTreeData(new OrgCallback() {
            @Override
            public void onResponse(OrgRelayResponse response, int ID) {
                if (response.code == 1) {
                    showToast(response.msg);
                    return;
                }
                mList.clear();
                for (OrgRelay orgRelay : response.data) {
                    DFS(orgRelay);
                }
                //end of format List
                updateView();
            }
        });
    }

    @AfterViews
    void go() {
        getMainList();
        setSupportActionBar(mTitleBar);
        mTitleBar.setTitle("请选择转发单位");
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //init ListView && adapter
        adapter = new OrgRelayAdapter(mContext);
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
    }

    private void updateView() {
        //bind data
        mIndexListView.bindDatas(mList);
    }

    /**
     * 确认选择单位
     */
    @OptionsItem(R.id.action_submit)
    void submit() {
        String ids = "";
        Map<String, Integer> idMap = new HashMap<>();

        for (OrgRelay account : mList) {
            if (account.checked) {
                idMap.put(account.id, 0);
            }
        }
        for (String id : idMap.keySet()) {
            if (TextUtils.isEmpty(ids)) {
                ids = id;
            } else {
                ids += "," + id;
            }
        }
        final String IDS = ids;
        final EditText editText = new EditText(mContext);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("转发意见")
                .setView(editText)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String message = editText.getText().toString();


                        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                                .setTitle("确认转发")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendRelay(message, IDS);
                                    }
                                })
                                .setNegativeButton("否", null)
                                .create();
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);

                    }
                })
                .setNegativeButton("取消", null)
                .create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }

    /**
     * 转发
     *
     * @param message 转发意见
     * @param ids     ID
     */
    private void sendRelay(String message, String ids) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);//公文或者会议的id
        params.put("type", 1 - type + "");//转发的类型（0：代表公文  ，1：代表会议）
        params.put("organid", ids);//你选择的所有机构的id的集合，比如"1,2,3,4,5"这样的字符串
        OAService.Relay(params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int ID) {
                showToast("转发失败");
            }

            @Override
            public void onResponse(String response, int ID) {
                try {
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                    showToast(baseResponse.msg);
                    if (baseResponse.code == 0) {
                        backToPreActivity();
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void DFS(OrgRelay temp) {
        mList.add(temp);
        if (temp.children != null && temp.children.size() >= 0) {
            for (OrgRelay relay : temp.children) {
                relay.name = temp.name + "/" + relay.name;
                DFS(relay);
            }
        }
    }

    private abstract class OrgCallback extends Callback<OrgRelayResponse> {
        @Override
        public OrgRelayResponse parseNetworkResponse(Response response, int ID) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, OrgRelayResponse.class);
            } else {
                return new OrgRelayResponse(baseResponse.msg, baseResponse.code, null);
            }
        }

        @Override
        public void onError(Call call, Exception e, int ID) {
            showToast("获取可转发组织失败");
        }
    }
}
