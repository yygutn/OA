package cn.edu.jumy.oa.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.web.FromMeActivity_;
import cn.edu.jumy.oa.UI.web.WaitingForApprovalActivity_;
import cn.edu.jumy.oa.widget.dragrecyclerview.adapter.RecyclerAdapter;
import cn.edu.jumy.oa.widget.dragrecyclerview.common.DividerGridItemDecoration;
import cn.edu.jumy.oa.widget.dragrecyclerview.entity.Item;
import cn.edu.jumy.oa.widget.dragrecyclerview.helper.MyItemTouchCallback;
import cn.edu.jumy.oa.widget.dragrecyclerview.helper.OnRecyclerItemClickListener;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.VibratorUtil;

/**
 * Created by Jumy on 16/5/23 09:38.
 * 审核页面
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
@EActivity(R.layout.activity_approval)
public class ApprovalActivity extends BaseActivity{
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
                showToast("我发起的");
                FromMeActivity_.intent(this).start();
                break;
            }
            case R.id.approval_wait: {
                showToast("待我审批");
                WaitingForApprovalActivity_.intent(this).start();
                break;
            }
            case R.id.approval_sick_leave: {
                showToast("销假");
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
