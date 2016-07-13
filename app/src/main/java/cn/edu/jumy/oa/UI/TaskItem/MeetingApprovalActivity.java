package cn.edu.jumy.oa.UI.TaskItem;

import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/20 13:57.
 *
 * @会议审核 会议审核和检索功能
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class MeetingApprovalActivity extends BaseSearchRefreshActivity {

    MeetingCardAdapter adapter;
    ArrayList<Meet> mList = new ArrayList<>();

    @Override
    protected void setTile() {
        mTitleBar.setTitle("会议审核");
    }

    @Override
    protected void initData() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) - 1900;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if ((month < 8 && (month & 1) == 1) || (month >= 8 && (month & 1) == 0)) {
            if (day == 31) {
                day--;
            }
            if (month == 2 && day > 28) {
                day = 28;
            }
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
        } else {
            month--;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");
        params.put("passStatus", "");

        OAService.meetCompany(params, new MeetCallback() {

            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.data != null && response.code == 0) {
                    mList = response.data.pageObject;
                    adapter.setList(response.data.pageObject);
                }
            }
        });
    }

    @Override
    protected void initListView() {
        adapter = new MeetingCardAdapter(mContext,R.layout.item_card_notification,new ArrayList(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onTextSubmit(String str) {
        super.onTextSubmit(str);
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void doLoadMore() {

    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        MeetAuditActivity_.intent(mContext).extra("audit",(Meet)o).start();
    }
}
