package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Relay;

/**
 * Created by Jumy on 16/8/8 14:38.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class Relay2Response extends BaseResponse{
    public ArrayList<Relay> data = new ArrayList<>();
    public Relay2Response(BaseResponse baseResponse){
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
        this.data = null;
    }
}
