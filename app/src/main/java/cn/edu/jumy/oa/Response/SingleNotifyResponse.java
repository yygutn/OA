package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Notify;

/**
 * Created by Jumy on 16/7/22 17:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class SingleNotifyResponse extends BaseResponse {
    public Notify data;

    public SingleNotifyResponse(BaseResponse baseResponse) {
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
    }
}
