package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.View;
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
 * Created by Jumy on 16/5/27 22:04.
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
public class FromMeActivity extends BaseActivity{
    @ViewById(R.id.webView)
    WebView mWebView;

    @ViewById(R.id.toolbar)
    Toolbar mToolbar;

    String baseUrl = "file:///android_asset/h5/h5_request.html";
    String preUrl = "";
    String nowUrl = "";

    WebViewClient client;
    Stack<String> stack = new Stack<>();
    @AfterViews
    void start(){
        mToolbar.setTitle("由我发起");
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
    private void back() {
        showDebugLoge("preUrl="+preUrl+"\n"+"nowUrl="+nowUrl+"\n"+"baseUrl="+baseUrl);
        if (baseUrl.contains(mWebView.getUrl()) || nowUrl.isEmpty()) {
            backToPreActivity();
        } else {
            stack.pop();
            client.shouldOverrideUrlLoading(mWebView,stack.lastElement());
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }
    private void push(String url){
        if (!stack.contains(url)){
            stack.push(url);
        }
    }
}
