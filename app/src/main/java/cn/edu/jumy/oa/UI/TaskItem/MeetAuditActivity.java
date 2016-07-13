package cn.edu.jumy.oa.UI.TaskItem;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.AuditAdapter;
import cn.edu.jumy.oa.bean.AuditUser;

/**
 * Created by Jumy on 16/7/6 11:37.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * 会议审核详情
 */
@EActivity(R.layout.activity_meet_audit)
public class MeetAuditActivity extends BaseActivity {
    @ViewById(R.id.audit_listView)
    RecyclerView mAuditListView;
    @ViewById(R.id.audit_pass)
    TextView mAuditPass;
    @ViewById(R.id.audit_not_pass)
    TextView mAuditNotPass;
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;

    AuditAdapter adapter;

    List<AuditUser> list = new ArrayList<>();

    @AfterViews
    void go() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        initList();


        adapter = new AuditAdapter(mContext,R.layout.item_audit_meet,list);

        mAuditListView.setLayoutManager(new LinearLayoutManager(mContext));
        mAuditListView.setAdapter(adapter);
    }

    private void initList() {
        list.add(new AuditUser("张三","省人大办公室"));
        list.add(new AuditUser("李四","省委办公厅"));
        list.add(new AuditUser("张三儿","省委办公厅"));

    }

    @Click(R.id.out)
    void click(){
        showToast("导出...");
    }
}
