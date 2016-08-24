package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

/**
 * Created by Jumy on 16/8/24 14:03.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class StringListResponse extends BaseResponse{
    public ArrayList<String> data;
    public StringListResponse(BaseResponse baseResponse){
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
        this.data = null;
    }
}
