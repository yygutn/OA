package cn.edu.jumy.oa.UI.TaskItem;

import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.DocCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.DocResponse;
import cn.edu.jumy.oa.adapter.SentDocAdapter;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.widget.customview.SimpleDividerItemDecoration;

/**
 * Created by Jumy on 16/7/12 20:25.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class SentDocumentActivity extends BaseSearchRefreshActivity {

    private ArrayList<Doc> mListDoc = new ArrayList<>();
    SentDocAdapter documentAdapter;
    @Override
    protected void setTile() {
        mTitleBar.setTitle("已发送公文");
    }

    @Override
    protected void initData() {
        mList = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        year = date.getYear();
        month = date.getMonth() - 1;
        day = date.getDay();
        if ((month < 8 && (month & 1) == 1) || (month >= 8 && (month & 1) == 0)) {
            if (day == 31) {
                day--;
            }
            if (month == 2 && day > 28) {
                day = 28;
            }
            if (month == 0) {
                month = 11;
            } else {
                month--;
            }
        } else {
            month--;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);

        final Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");

        OAService.docUser(params, new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null){
                    mListDoc = response.data.pageObject;
                    documentAdapter.setList(response.data.pageObject);
                }
            }
        });
    }

    @Override
    protected void initListView() {
        documentAdapter = new SentDocAdapter(mContext, R.layout.item_sent_xx, new ArrayList<>(mListDoc));
        mListView.setAdapter(documentAdapter);
        mListView.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }

    @Override
    protected void onTextSubmit(String str) {
        super.onTextSubmit(str);
    }
}
