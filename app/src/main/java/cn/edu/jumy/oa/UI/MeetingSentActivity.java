package cn.edu.jumy.oa.UI;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Jumy on 16/6/7 13:59.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity
public class MeetingSentActivity extends BaseWebActivity{
    @Override
    protected void setBaseUrl() {
        super.baseUrl = "file:///android_asset/h5/h5_meet.html";
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToolbar.setTitle("已发布会议");
    }
}
