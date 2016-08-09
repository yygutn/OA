package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Relay;

/**
 * Created by Jumy on 16/8/8 14:38.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class RelayResponse extends BaseResponse{
    public RelayData data;
    public RelayResponse(BaseResponse baseResponse){
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
        this.data = null;
    }

    public class RelayData {

        /**
         * page : 1
         * size : 20
         * totalPage : 4
         * pageObject : null
         * startTime : null
         * endTime : null
         * ids : null
         * userids : null
         * before : null
         */

        public int page;
        public int size;
        public int totalPage;
        public ArrayList<Relay> pageObject;
        public String startTime;
        public String endTime;
        public String ids;
        public String userids;
        public String before;
    }
}
