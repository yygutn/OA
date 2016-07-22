package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.OrganizationOftenResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/22 12:59.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class OrganizationOftenCallback extends Callback<OrganizationOftenResponse>{
    @Override
    public OrganizationOftenResponse parseNetworkResponse(Response response, int id) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
        if (baseResponse.code == 0){
            return gson.fromJson(data,OrganizationOftenResponse.class);
        } else {
            return new OrganizationOftenResponse(baseResponse.msg,baseResponse.code);
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        Toast.makeText(MyApplication.getContext(),"当前网络不可用,获取常用组织列表失败",Toast.LENGTH_SHORT).show();
    }
}
