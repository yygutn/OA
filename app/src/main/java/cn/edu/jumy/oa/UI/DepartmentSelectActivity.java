package cn.edu.jumy.oa.UI;

import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Date;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AccountResponse;
import cn.edu.jumy.oa.adapter.DepartmentAdapter;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import me.yokeyword.indexablelistview.IndexEntity;
import me.yokeyword.indexablelistview.IndexHeaderEntity;
import me.yokeyword.indexablelistview.IndexableStickyListView;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/15 09:35.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_pick_department)
@OptionsMenu(R.menu.confirm)
public class DepartmentSelectActivity extends BaseActivity {
    public static final String DEPARTMENT = "cn.edu.jumy.oa.UI.Department";
    public static final String DEPARTMENT_SELECTED = "cn.edu.jumy.oa.UI.Department.Selected";
    @ViewById(R.id.searchview)
    SearchView mSearchView;
    @ViewById(R.id.indexListView)
    IndexableStickyListView mIndexListView;

    DepartmentAdapter adapter;
    @ViewById(R.id.title_bar)
    Toolbar mTitleBar;

    private ArrayList<Account> mUsedDepartment;
    private ArrayList<Account> mSelectDepartment = new ArrayList<>();

    //初始化数据
    ArrayList<Account> mDepartments = new ArrayList<>();


    @AfterViews
    void go() {
        Extras();
        setSupportActionBar(mTitleBar);
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //init ListView && adapter
        adapter = new DepartmentAdapter(mContext);
        mIndexListView.setAdapter(adapter);

        updateList();

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

        mIndexListView.setOnItemContentClickListener(new IndexableStickyListView.OnItemContentClickListener() {
            @Override
            public void onItemClick(View v, IndexEntity indexEntity) {
                CheckBox radioButton = (CheckBox) v.findViewById(R.id.radioButton);
                boolean check = radioButton.callOnClick();
                Account account = (Account) indexEntity;
                account.checked = check;
                adapter.notifyDataSetChanged();
                if (check) {
                    boolean flag = true;
                    for (Account a : mSelectDepartment) {
                        if (flag) {
                            if (account.id.equals(a.id)) {
                                flag = false;
                                break;
                            }
                        }
                    }
                    if (flag) {
                        mSelectDepartment.add(account);
                    }
                } else {
                    for (Account ac : mSelectDepartment) {
                        if (ac.id.equals(account.id))
                            mSelectDepartment.remove(ac);
                    }
                }
            }
        });

    }

    void Extras() {
//        mUsedDepartment = (ArrayList<Account>) ACache.get(mContext, EMClient.getInstance().getCurrentUser()).getAsObject(DEPARTMENT);
        if (mUsedDepartment == null) {
            mUsedDepartment = new ArrayList<>();
        }


        OAService.getOrganizationData(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showDebugException(e);
                showToast("网络异常，获取可发送单位失败");
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
                        updateList();
                    }
                } catch (Exception e) {
                    showDebugException(e);
                }
            }
        });
    }

    private void updateList() {
        try {
            adapter.getSourceItems();
            if (mUsedDepartment.size() > 0) {
                //添加最近单位Header
                IndexHeaderEntity<Account> hotHeader = new IndexHeaderEntity<>();
                hotHeader.setHeaderTitle("常用单位");
                hotHeader.setIndex("常");
                hotHeader.setHeaderList(mUsedDepartment);
                //bind data
                mIndexListView.bindDatas(mDepartments, hotHeader);
            } else {
                //bind data
                mIndexListView.bindDatas(mDepartments);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 确认选择单位，提交到上一级页面
     */
    @OptionsItem(R.id.action_submit)
    void submit() {
        String str = "";
        String ids = "";
        for (Account account : mDepartments) {
            if (account.checked) {
                if (TextUtils.isEmpty(str)) {
                    str = account.name;
                } else if (str.contains(",")) {
                    str += "等";
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
        Intent data = new Intent();
        if (!TextUtils.isEmpty(ids) && !TextUtils.isEmpty(str)) {
            data.putExtra("str",str);
            data.putExtra("ids",ids);
            setResult(0, data);
//            try {
//                ACache.get(mContext, EMClient.getInstance().getCurrentUser()).clear();
//                mSelectDepartment = (ArrayList<Account>) mSelectDepartment.subList(0, 10);
//                ACache.get(mContext, EMClient.getInstance().getCurrentUser()).put(DEPARTMENT, mSelectDepartment);
//            } catch (Exception e) {
//                showDebugException(e);
//            }
        } else {
            setResult(1);
        }
        backToPreActivity();
    }

    @Override
    public void onBackPressed() {
        setResult(1);
        backToPreActivity();
    }
}
