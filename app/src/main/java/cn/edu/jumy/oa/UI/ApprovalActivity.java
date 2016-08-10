package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.ApprovalFqActivity_;
import cn.edu.jumy.oa.UI.TaskItem.ApprovalSpActivity_;
import cn.edu.jumy.oa.widget.dragrecyclerview.entity.Item;

/**
 * Created by Jumy on 16/5/23 09:38.
 * 审核页面
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_approval)
public class ApprovalActivity extends BaseActivity {
    private List<Item> results = new ArrayList<Item>();

    @ViewById(R.id.toolbar)
    Toolbar mToolBar;

    @ViewById(R.id.approval_mine)
    LinearLayout mAppMine;

    @ViewById(R.id.approval_wait)
    LinearLayout mAppWait;

    @ViewById(R.id.approval_sick_leave_visible)
    LinearLayout mAppVisible;

    @AfterViews
    void start() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Click({R.id.approval_sick_leave, R.id.approval_sick_leave_invisible, R.id.approval_wait, R.id.approval_mine})
    void click(View view) {
        switch (view.getId()) {
            case R.id.approval_mine: {
                ApprovalFqActivity_.intent(mContext).start();
                break;
            }
            case R.id.approval_wait: {
                ApprovalSpActivity_.intent(mContext).start();
                break;
            }
            case R.id.approval_sick_leave: {
                break;
            }
            case R.id.approval_sick_leave_invisible: {
                mAppVisible.setVisibility(View.GONE);
                break;
            }
            default:
                break;
        }
    }
}
