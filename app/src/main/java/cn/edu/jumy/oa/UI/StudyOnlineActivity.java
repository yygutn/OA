package cn.edu.jumy.oa.UI;

import android.content.pm.ActivityInfo;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Stack;

import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/5/27 22:12.
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
@EActivity
public class StudyOnlineActivity extends BaseWebActivity{

    WebViewClient client;
    WebChromeClient webChromeClient;
    Stack<String> stack = new Stack<>();
    @AfterViews
    void start(){
        mToolbar.setTitle("在线学习");
        getWebSettings();
        mWebView.loadUrl(baseUrl);
        preUrl = nowUrl;
        nowUrl = baseUrl;
        push(baseUrl);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    @Override
    protected void initViews() {
    }

    private void getWebSettings() {
        WebSettings ws = mWebView.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加
        webChromeClient = new WebChromeClient();
        mWebView.setWebChromeClient(webChromeClient);


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
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.loadUrl("about:blank");
        mWebView.stopLoading();
        mWebView.setWebChromeClient(null);
        mWebView.setWebViewClient(null);
        mWebView.destroy();
        mWebView = null;
    }

    @Override
    protected void setBaseUrl() {
        baseUrl = "file:///android_asset/h5/h5_study.html";
    }
}
