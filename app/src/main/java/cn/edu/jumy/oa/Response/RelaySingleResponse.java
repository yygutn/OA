package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Relay;

/**
 * Created by Jumy on 16/8/8 14:38.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class RelaySingleResponse extends BaseResponse{
    public Relay data;
    public RelaySingleResponse(BaseResponse baseResponse){
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
        this.data = null;
    }
}
