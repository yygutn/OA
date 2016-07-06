package cn.edu.jumy.oa.API;

import org.json.JSONObject;

import java.util.List;

import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.bean.BaseResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Jumy on 16/7/6 16:31.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public interface GetSendUnit {
    @POST("getOrganizationData")
    @FormUrlEncoded
    Call<String> getUnits(@Field("value") String value);
}
