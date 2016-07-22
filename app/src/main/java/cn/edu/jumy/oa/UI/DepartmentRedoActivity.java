package cn.edu.jumy.oa.UI;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.OrganizationOftenCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.OrganizationOftenResponse;
import cn.edu.jumy.oa.adapter.DepartmentListAdapter;
import cn.edu.jumy.oa.bean.OrganizationOften;

/**
 * Created by Jumy on 16/7/15 09:35.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_list_department)
public class DepartmentRedoActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    Toolbar mTitleBar;

    @ViewById(R.id.department_listView)
    RecyclerView mListView;

    DepartmentListAdapter adapter;

    ArrayList<OrganizationOften> mList = new ArrayList<>();

    @AfterExtras
    void getData() {
        OAService.getMagroupAll(new OrganizationOftenCallback() {
            @Override
            public void onResponse(OrganizationOftenResponse response, int id) {
                if (response.code == 0) {
                    mList = response.data;
                    adapter.setList(mList);
                } else {
                    showToast("获取常用单位列表失败" + response.msg);
                }
            }
        });
    }


    @AfterViews
    void go() {
        setSupportActionBar(mTitleBar);
        setTitle("修改常用单位");
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //init ListView && adapter
        initAdapter();
        mListView.setAdapter(adapter);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
    }

    private void initAdapter() {
        //bind data
        adapter = new DepartmentListAdapter(mContext, R.layout.item_notify_notification, mList);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                DepartmentOftenUseFixActivity_.intent(mContext).extra("org", ((OrganizationOften) o)).start();
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
    }
}
