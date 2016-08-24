package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.Relay2Response;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/8/24 13:00.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class RelayCallback2 extends Callback<Relay2Response>{

    @Override
    public Relay2Response parseNetworkResponse(Response response, int ID) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
        if (baseResponse.code == 0) {
            return gson.fromJson(data, Relay2Response.class);
        } else {
            return new Relay2Response(baseResponse);
        }
    }

    @Override
    public void onError(Call call, Exception e, int ID) {
        Toast.makeText(MyApplication.getContext(),"服务器错误,获取审批意见失败",Toast.LENGTH_SHORT).show();
    }
}
