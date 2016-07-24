package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.text.TextUtils;

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
import cn.edu.jumy.oa.adapter.SentMeetAdapter;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.widget.customview.SimpleDividerItemDecoration;

/**
 * Created by Jumy on 16/7/12 20:25.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class SentMeetingActivity extends BaseSearchRefreshActivity {

    private ArrayList<Meet> mListMeet = new ArrayList<>();
    SentMeetAdapter adapter;
    @Override
    protected void setTile() {
        mTitleBar.setTitle("已发布会议");
    }

    @Override
    protected void initData() {
        mList = null;
        OAService.meetUser(getParams(), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null){
                    mListMeet = response.data.pageObject;
                    adapter.setList(response.data.pageObject);
                }
            }
        });
    }

    @NonNull
    private Map<String, String> getParams() {
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

        final Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");
        params.put("passStatus", "0");
        params.put("meetCompany", "");
        return params;
    }

    @Override
    protected void initListView() {
        adapter = new SentMeetAdapter(mContext,R.layout.item_sent_xx,new ArrayList<>(mListMeet));
        mListView.setAdapter(adapter);
        mListView.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)){
            showToast("请输入有效关键字");
            return;
        }
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet meet : mListMeet){
            if (meet.docTitle.contains(str)){
                list.add(meet);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mListMeet));
    }
}
