package cn.edu.jumy.oa.CallBack;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.Response.AccountResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/15 11:14.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class AccountCallback extends Callback<AccountResponse>{
    @Override
    public AccountResponse parseNetworkResponse(Response response, int id) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
        if (baseResponse.code == 0){
            return gson.fromJson(data,AccountResponse.class);
        } else {
            return new AccountResponse("",1,null);
        }
    }
}
