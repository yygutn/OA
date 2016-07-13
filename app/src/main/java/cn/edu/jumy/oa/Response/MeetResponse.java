package cn.edu.jumy.oa.Response;

import java.util.ArrayList;

import cn.edu.jumy.oa.bean.Meet;

/**
 * Created by Jumy on 16/7/12 13:51.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class MeetResponse extends BaseResponse{
    public MeetResult data;

    public class MeetResult{
        public int page;
        public int size;
        public int totalPage;
        public ArrayList<Meet> pageObject;

        public String startTime;
        public String endTime;
        public String ids;
        public String userids;
        public int before;
    }

    public MeetResponse(String msg, int coe) {
        this.msg = msg;
        this.code = coe;
        this.data = null;
    }

    public MeetResponse(BaseResponse baseResponse) {
        this.data = null;
        this.code = baseResponse.code;
        this.msg = baseResponse.msg;
    }
}
