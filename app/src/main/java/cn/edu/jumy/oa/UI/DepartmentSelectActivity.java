package cn.edu.jumy.oa.UI;

import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.OrganizationOftenCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AccountResponse;
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
@EActivity(R.layout.activity_pick_department)
@OptionsMenu(R.menu.confirm)
public class DepartmentSelectActivity extends BaseActivity {
    @ViewById(R.id.searchview)
    SearchView mSearchView;
    @ViewById(R.id.indexListView)
    IndexableStickyListView mIndexListView;

    DepartmentAdapter adapter;
    @ViewById(R.id.title_bar)
    Toolbar mTitleBar;

    //初始化数据
    ArrayList<Account> mDepartments = new ArrayList<>();


    ArrayList<OrganizationOften> mList = new ArrayList<>();

    ArrayList<IndexHeaderEntity<Account>> mHeaderList = new ArrayList<>();
    ArrayList<String> mHeaderTitleList = new ArrayList<>();

    private void getMainList() {
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
                        mDepartments.clear();
                        for (Account temp : account.data) {
                            DFS(temp);
                        }
                        updateView();
                    }
                } catch (Exception e) {
                    showDebugException(e);
                }
            }
        });
    }

    private void DFS(Account temp) {
        mDepartments.add(temp);
        if (temp.organizationList != null && temp.organizationList.size() >= 0) {
            for (Account account : temp.organizationList) {
                account.name = temp.name + "/" + account.name;
                DFS(account);
            }
        }
    }

    private void getHeader() {
        mList.clear();
        mHeaderList.clear();
        mHeaderTitleList.clear();
        OAService.getMagroupAll(new OrganizationOftenCallback() {
            @Override
            public void onResponse(OrganizationOftenResponse response, int id) {
                if (response.code == 0) {
                    mList = response.data;
                    initOftenData();
                } else {
                    showToast("获取常用单位列表失败" + response.msg);
                }
            }
        });
    }

    private void initOftenData() {
        int len;
        String[] ids;
        String[] names;
        ArrayList<Account> temp;
        mHeaderList.clear();
        try {
            for (OrganizationOften often : mList) {
                temp = new ArrayList<>();
                if (TextUtils.isEmpty(often.value)) {
                    often.value = "";
                }
                if (TextUtils.isEmpty(often.departmentName)) {
                    often.departmentName = "";
                }
                ids = often.value.split(",");
                names = often.departmentName.split(",");
                len = ids.length;
                for (int i = 0; i < len; i++) {
                    temp.add(new Account(ids[i], names[i]));
                }

                IndexHeaderEntity<Account> hotHeader = new IndexHeaderEntity<>();
                if (TextUtils.isEmpty(often.name)) {
                    often.name = "#";
                }
                hotHeader.setHeaderTitle(often.name);
                hotHeader.setIndex(often.name.charAt(0) + " ");
                hotHeader.setHeaderList(temp);
                mHeaderList.add(hotHeader);
                mHeaderTitleList.add(often.name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateView();
    }

    @AfterViews
    void go() {
        getHeader();
        getMainList();
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

        mIndexListView.setOnItemTitleClickListener(new IndexableStickyListView.OnItemTitleClickListener() {
            @Override
            public void onItemClick(View v, String title) {
                for (IndexHeaderEntity<Account> headerEntity : mHeaderList) {
                    if (headerEntity.getHeaderTitle().equals(title)) {
                        for (Account account : headerEntity.getHeaderList()) {
                            account.checked = !(account.checked);
                        }
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
            }
        });
    }

    private void updateView() {
        try {
            if (!mHeaderList.isEmpty()) {
                //bind data
                mIndexListView.bindDatas(mDepartments, mHeaderList);
            } else {
                //bind data
                mIndexListView.bindDatas(mDepartments);
            }
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    /**
     * 确认选择单位，提交到上一级页面
     */
    @OptionsItem(R.id.action_submit)
    void submit() {
        String str = "";
        String ids = "";
        Map<String, Integer> idMap = new HashMap<>();
        Map<String, Integer> nameMap = new HashMap<>();
        for (Account account : mDepartments) {
            if (account.checked) {
                nameMap.put(account.name, 0);
                idMap.put(account.id, 0);
            }
        }
        for (IndexHeaderEntity<Account> entity : mHeaderList) {
            for (Account account : entity.getHeaderList()) {
                if (account.checked) {
                    idMap.put(account.id, 0);
                    nameMap.put(account.name, 0);
                }
            }
        }
        for (String id : idMap.keySet()) {
            if (TextUtils.isEmpty(ids)) {
                ids = id;
            } else {
                ids += "," + id;
            }
        }
        for (String name : nameMap.keySet()) {
            if (TextUtils.isEmpty(str)) {
                str = name;
            } else if (str.contains(",")) {
                str += "等";
                break;
            } else {
                str += "," + name;
            }
        }
        int requestCode = getIntent().getIntExtra("requestCode", -1);
        if (requestCode == 0 && mDepartments != null && mDepartments.size() > 0) {
            if (ids.contains(",")) {
                showToast("只能选择一个承办单位,请重新选择");
                return;
            }
        }
        Intent data = new Intent();
        if (!TextUtils.isEmpty(ids) && !TextUtils.isEmpty(str)) {
            data.putExtra("str", str);
            data.putExtra("ids", ids);
            setResult(0, data);
        } else {
            setResult(1);
        }
        backToPreActivity();
    }

    /**
     * 添加常用单位
     */
    @OptionsItem(R.id.action_add)
    void addOrganization() {
        DepartmentAddActivity_.intent(mContext).extra("titleList", mHeaderTitleList).startForResult(16);
    }

    /**
     * 修改常用单位
     */
    @OptionsItem(R.id.action_redo)
    void redoOrganization() {
        DepartmentRedoActivity_.intent(mContext).startForResult(16);
    }


    @Override
    public void onBackPressed() {
        setResult(1);
        backToPreActivity();
    }

    @OnActivityResult(16)
    void fix() {
        getHeader();
    }
}
