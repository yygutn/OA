package cn.edu.jumy.oa.UI;

import cn.edu.jumy.jumyframework.BaseActivity;

import org.androidannotations.annotations.EActivity;

/**
 * Created by Jumy on 16/6/2 10:57.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity
public class FileSentActivity extends BaseWebActivity{

    @Override
    protected void setBaseUrl() {
        super.baseUrl = "file:///android_asset/h5/h5_doc.html";
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToolbar.setTitle("已发送公文");
    }
}
