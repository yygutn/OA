package cn.edu.jumy.oa.API;

import cn.edu.jumy.oa.bean.User;
import retrofit2.Call;
import retrofit2.http.POST;

/**
 * Created by Jumy on 16/6/14 15:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public interface Login {
    @POST("login")
    Call<User> login();
}
