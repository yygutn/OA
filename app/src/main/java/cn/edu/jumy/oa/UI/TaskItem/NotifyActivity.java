package cn.edu.jumy.oa.UI.TaskItem;

import org.androidannotations.annotations.EActivity;

import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/6/27 16:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class NotifyActivity extends BaseSearchRefreshActivity{
    @Override
    protected void setTile() {
        mTitleBar.setTitle("公告");
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
}
