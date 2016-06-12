package cn.edu.jumy.oa.UI;

import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.EActivity;

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
@EActivity
public class FromMeActivity extends BaseWebActivity{

    @Override
    protected void setBaseUrl() {
        baseUrl = "file:///android_asset/h5/h5_request.html";
    }

    @Override
    protected void initViews() {
        super.initViews();
        mToolbar.setTitle("由我发起");
    }
}
