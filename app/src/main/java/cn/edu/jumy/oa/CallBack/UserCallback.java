package cn.edu.jumy.oa.CallBack;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.UserResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/26 14:47.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class UserCallback extends Callback<UserResponse>{
    @Override
    public UserResponse parseNetworkResponse(Response response, int ID) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
        if (baseResponse.code == 0){
            return gson.fromJson(data,UserResponse.class);
        } else {
            return new UserResponse(baseResponse);
        }
    }

    @Override
    public void onError(Call call, Exception e, int ID) {
        BaseActivity.showDebugException(e);
    }
}
