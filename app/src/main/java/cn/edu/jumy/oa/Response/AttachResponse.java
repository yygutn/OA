package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Attachment;

/**
 * Created by Jumy on 16/7/12 16:51.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class AttachResponse extends BaseResponse {
    public ArrayList<Attachment> data;

    public AttachResponse(String msg, int coe, ArrayList<Attachment> data) {
        this.msg = msg;
        this.code = coe;
        this.data = data;
    }
}
