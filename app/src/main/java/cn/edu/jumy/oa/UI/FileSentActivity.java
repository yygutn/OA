package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Stack;

import cn.edu.jumy.oa.R;

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
