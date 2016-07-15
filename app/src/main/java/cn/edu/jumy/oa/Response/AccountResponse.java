package cn.edu.jumy.oa.Response;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.bean.Account;

/**
 * Created by Jumy on 16/7/7 16:27.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AccountResponse extends BaseResponse{
    public ArrayList<Account> data;

    public AccountResponse(ArrayList<Account> data) {
        this.data = data;
    }

    public AccountResponse(String msg, int coe, ArrayList<Account> data) {
        super(msg, coe);
        this.data = data;
    }
}
