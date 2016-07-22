package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.OrganizationOften;

/**
 * Created by Jumy on 16/7/22 12:57.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OrganizationOftenResponse extends BaseResponse{
    public ArrayList<OrganizationOften> data;
    public  OrganizationOftenResponse(String msg,int code){
        this.msg = msg;
        this.code = code;
        this.data = null;
    }
}
