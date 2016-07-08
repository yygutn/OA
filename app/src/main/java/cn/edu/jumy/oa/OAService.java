package cn.edu.jumy.oa;

import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Date;
import java.util.Map;

import cn.edu.jumy.oa.safe.PasswordUtil;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/8 17:40.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OAService {

    public static String zip = "";
    public static String base = "";

    /**
     * 获取服务器时间
     * @param callback 回调
     */
    private static void getTime(Callback callback){
        OkHttpUtils.post()
                .url(MyApplication.API_URL+"getTime")
                .build()
                .execute(callback);
    }

    /**
     * 公文接收
     * @param params  参数
     * @param callback 回调
     */
    public static void docReceive(final Map<String,String> params, final Callback callback){
        getTime(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call,e,id);
            }

            @Override
            public void onResponse(String response, int id) {
                base = EMClient.getInstance().getCurrentUser() + "_" + response;
                zip = PasswordUtil.simpleEncpyt(base);

                OkHttpUtils.post()
                        .url(MyApplication.API_URL+"docReceive")
                        .addParams("value",zip)
                        .params(params)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 公文发送
     * @param params  参数
     * @param fileMap 附件表
     * @param callback 回调
     */
    public static void docSend(final Map<String,String> params, final Map<String,File> fileMap, final Callback callback){
        getTime(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call,e,id);
            }

            @Override
            public void onResponse(String response, int id) {
                base = EMClient.getInstance().getCurrentUser() + "_" + response;
                zip = PasswordUtil.simpleEncpyt(base);

                OkHttpUtils.post()
                        .url(MyApplication.API_URL+"docReceive")
                        .addParams("value",zip)
                        .params(params)
                        .files("upfile",fileMap)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 获取当前用户可以发送公文的组织单位
     * @param callback
     */
    public static void getOrganizationData(final Callback callback){
        getTime(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call,e,id);
            }

            @Override
            public void onResponse(String response, int id) {
                base = EMClient.getInstance().getCurrentUser() + "_" + response;
                zip = PasswordUtil.simpleEncpyt(base);

                OkHttpUtils.post()
                        .url(MyApplication.API_URL + "getOrganizationData")
                        .addParams("value", zip)
                        .build()
                        .execute(callback);
            }
        });
    }

    public static void getAttachmentList(){

    }
    public static void downAttachment(){

    }

}
