package cn.edu.jumy.oa.CallBack;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.NotifyResponse;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/22 17:19.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class NotifyCallback extends Callback<NotifyResponse>{
    @Override
    public NotifyResponse parseNetworkResponse(Response response, int id) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
        if (baseResponse.code == 0){
            return gson.fromJson(data,NotifyResponse.class);
        } else {
            return new NotifyResponse(baseResponse);
        }
    }
}
