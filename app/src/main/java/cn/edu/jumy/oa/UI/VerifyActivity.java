package cn.edu.jumy.oa.UI;

/**
 * Created by Jumy on 16/5/27 21:44.
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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chatui.ui.LoginActivity;
import com.hyphenate.chatui.ui.SplashActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import cn.edu.jumy.jumyframework.StatusBarCompat;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DocumentCabinetActivity_;

/**
 * 校验指纹
 */
@EActivity(R.layout.activity_verify)
public class VerifyActivity extends AppCompatActivity {

    @ViewById(R.id.img_verify)
    ImageView imageView;

    @ViewById(R.id.login_other)
    TextView textView;

    private int LOGIN_RESULT_CODE = 100;

    Context mContext;

    String file = "";

    @ColorRes(R.color.pressed)
    int pressed;

    @AfterViews
    void start() {
        StatusBarCompat.compat(this, pressed);
        mContext = this;
        Glide.with(mContext).load(R.drawable.fingerprint).into(imageView);
        Bundle bundle = getIntent().getBundleExtra("file");
        if (bundle != null) {
            file = bundle.getString("file", "");
            if (!TextUtils.isEmpty(file) && file.contains("file")) {
                textView.setVisibility(View.GONE);
            }
        }
    }

    @Click(R.id.login_other)
    void click() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_RESULT_CODE);
        finish();
    }

    @LongClick(R.id.img_verify)
    void longClick() {
        Glide.with(mContext).load(R.drawable.fingerprint_pass).into(imageView);
        if (!TextUtils.isEmpty(file) && file.contains("file")) {
            DocumentCabinetActivity_.intent(mContext).start();
        } else {
            startActivity(new Intent(mContext, SplashActivity.class));
        }
        imageView = null;
        finish();
    }

}
