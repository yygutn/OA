package cn.edu.jumy.oa.bean;

import java.util.List;

/**
 * Created by Jumy on 16/7/7 16:27.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AccountResult extends BaseResponse{
    public List<Account> data;

    public AccountResult(List<Account> data) {
        this.data = data;
    }

    public AccountResult(String msg, int coe, List<Account> data) {
        super(msg, coe);
        this.data = data;
    }
}
