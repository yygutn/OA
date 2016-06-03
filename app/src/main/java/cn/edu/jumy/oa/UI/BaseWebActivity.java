package cn.edu.jumy.oa.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.ViewById;
import org.androidannotations.api.view.HasViews;
import org.androidannotations.api.view.OnViewChangedListener;

import java.util.Stack;

import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/6/2 11:42.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class BaseWebActivity extends BaseActivity{
    WebView mWebView;
    Toolbar mToolbar;
    String baseUrl = "file:///android_asset/h5/h5_doc.html";
    String nowUrl = "";
    String preUrl = "";
    Stack<String> stack = new Stack<>();

    WebViewClient client;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mWebView = (WebView) findViewById(R.id.webView);
        setBaseUrl();
        initViews();
    }

    protected void setBaseUrl() {

    }

    protected void initViews() {
        mToolbar.setTitle("已发送公文");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(client = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showDebugLoge("333:"+url);
                view.loadUrl(url);
                preUrl = nowUrl;
                nowUrl = url;
                push(url);
                showDebugLoge("preUrl="+preUrl+"\n"+"nowUrl="+nowUrl+"\n"+"baseUrl="+baseUrl);
                return true;
            }
        });
        mWebView.loadUrl(baseUrl);
        nowUrl = baseUrl;
        preUrl = nowUrl;
        push(baseUrl);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void back() {
        showDebugLoge("preUrl="+preUrl+"\n"+"nowUrl="+nowUrl+"\n"+"baseUrl="+baseUrl);
        if (baseUrl.contains(stack.lastElement()) || nowUrl.isEmpty()) {
            backToPreActivity();
        } else {
            preUrl = stack.pop();
            client.shouldOverrideUrlLoading(mWebView,stack.lastElement());
        }
    }
    private void push(String url){
        if (!stack.contains(url)){
            stack.push(url);
        }
    }
    @Override
    public void onBackPressed() {
        back();
    }


}
