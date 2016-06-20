package cn.edu.jumy.oa.UI.web;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Jumy on 16/6/2 11:41.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity
public class SignUpActivity extends BaseWebActivity{

    @Override
    protected void setBaseUrl() {
        super.baseUrl = "file:///android_asset/h5/h5_bao.html";
    }

    @Override
    protected void initViews() {
        super.initViews();
        super.mToolbar.setTitle("报名详情");
    }
}
