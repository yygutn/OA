package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.Relay2Response;
import cn.edu.jumy.oa.Response.StringListResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/8/24 13:00.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class StringCallback2 extends Callback<StringListResponse>{

    @Override
    public StringListResponse parseNetworkResponse(Response response, int ID) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
        if (baseResponse.code == 0) {
            return gson.fromJson(data, StringListResponse.class);
        } else {
            return new StringListResponse(baseResponse);
        }
    }

    @Override
    public void onError(Call call, Exception e, int ID) {

    }
}
