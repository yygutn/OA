package cn.edu.jumy.oa.Response;

import cn.edu.jumy.oa.bean.User;

/**
 * Created by Jumy on 16/7/26 14:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class UserResponse extends BaseResponse {
    public User data;

    public UserResponse(BaseResponse baseResponse) {
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
        data = null;
    }
}
