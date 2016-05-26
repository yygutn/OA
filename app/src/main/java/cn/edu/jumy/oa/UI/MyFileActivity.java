package cn.edu.jumy.oa.UI;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/5/26 11:26.
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
@EActivity(R.layout.activity_webview)
public class MyFileActivity extends BaseActivity{
    @ViewById(R.id.webView)
    WebView mWebView;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    String baseUrl = "file:///android_asset/h5/h5_myDoc.html";
    String preUrl = "";
    String nowUrl = "";

    WebViewClient client;

    @AfterViews
    void start(){
        AppManager.getInstance().addActivity(this);
        mToolbar.setTitle("我的文件");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(client = new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                showDebugLoge("333:"+url);
                view.loadUrl(url);
                preUrl = nowUrl;
                nowUrl = url;
                showDebugLoge("preUrl="+preUrl+"\n"+"nowUrl="+nowUrl+"\n"+"baseUrl="+baseUrl);
                return true;
            }
        });
        mWebView.loadUrl(baseUrl);
        preUrl = nowUrl;
        nowUrl = baseUrl;
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDebugLoge("preUrl="+preUrl+"\n"+"nowUrl="+nowUrl+"\n"+"baseUrl="+baseUrl);
                if (baseUrl.contains(mWebView.getUrl()) || nowUrl.isEmpty()) {
                    onBackPressed();
                } else {
                    client.shouldOverrideUrlLoading(mWebView,preUrl);
                }
            }
        });
    }
}
