package cn.edu.jumy.oa.Response;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.bean.Doc;

/**
 * Created by Jumy on 16/7/8 14:23.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DocResponse extends BaseResponse{
    public DocResult data;

    /**
     "page": 1,
     "size": 20,
     "totalPage": 0,
     "pageObject": [],
     "startTime": "2011-09-20 08:30:45",
     "endTime": "2016-07-08 08:30:45",
     "ids": null,
     "userids": "1",
     "before": 0
     */

    public DocResponse(DocResult data) {
        this.data = data;
    }

    public DocResponse(String msg, int coe, DocResult data) {
        super(msg, coe);
        this.data = data;
    }

    public class DocResult{
        public int page;
        public int size;
        public int totalPage;
        public ArrayList<Doc> pageObject;
        public String startTime;
        public String endTime;
        public String ids;
        public String userids;
        public int before;
    }
}
