package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.OrgRelay;

/**
 * Created by Jumy on 16/7/7 16:27.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OrgRelayResponse extends BaseResponse {
    public ArrayList<OrgRelay> data;

    public OrgRelayResponse(ArrayList<OrgRelay> data) {
        this.data = data;
    }

    public OrgRelayResponse(String msg, int coe, ArrayList<OrgRelay> data) {
        this.code = coe;
        this.msg = msg;
        this.data = data;
    }
}
