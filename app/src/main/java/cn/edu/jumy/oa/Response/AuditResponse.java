package cn.edu.jumy.oa.Response;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.bean.AuditUser;

/**
 * Created by Jumy on 16/7/7 16:27.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AuditResponse extends BaseResponse{
    public ArrayList<AuditUser> data;
    public AuditResponse(String msg,int code){
        this.msg = msg;
        this.code = code;
        this.data = null;
    }
}
