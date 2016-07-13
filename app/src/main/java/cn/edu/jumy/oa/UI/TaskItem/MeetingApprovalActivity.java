package cn.edu.jumy.oa.UI.TaskItem;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Utils.CardGenerator;
import okhttp3.Call;

/**
 * Created by Jumy on 16/6/20 13:57.
 * @会议审核 会议审核和检索功能
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class MeetingApprovalActivity extends BaseSearchRefreshActivity{

    @Override
    protected void setTile() {
        mTitleBar.setTitle("会议审核");
    }

    @Override
    protected void initData() {
        Resources resources = getResources();
        String [] test1 = resources.getStringArray(R.array.test1);
        String [] test2 = resources.getStringArray(R.array.test2);
        String [] test3 = resources.getStringArray(R.array.test3);
        String [] test4 = resources.getStringArray(R.array.test4);
        String [] test5 = resources.getStringArray(R.array.test5);
        String [] test6 = resources.getStringArray(R.array.test6);

        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test1),0));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test2),0));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test3),0));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test4),0));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test5),0));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test6),0));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        year = date.getYear();
        month = date.getMonth() - 1;
        day = date.getDay();
        if (month < 0) {
            year--;
            month = 11;
            day = 31;
        } else {
            day = month == 1 ? 28 : 30;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);
        Map<String,String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");
        params.put("passStatus", "");

        OAService.meetCompany(params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showDebugException(e);
            }

            @Override
            public void onResponse(String response, int id) {
                showDebugLogd(response);
            }
        });
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
        MeetAuditActivity_.intent(mContext).start();
    }
}
