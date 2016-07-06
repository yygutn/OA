package cn.edu.jumy.oa.UI.TaskItem;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;

import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/6/20 13:57.
 * @会议审核 会议审核和检索功能
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class MeetingApprovalActivity extends BaseSearchRefreshActivity{
    @Override
    protected void setTile() {
        mTitleBar.setTitle("会议审核");
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void onTextSubmit(String str) {
        super.onTextSubmit(str);
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void doLoadMore() {

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        MeetAuditActivity_.intent(mContext).start();
    }
}
