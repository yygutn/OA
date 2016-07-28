package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.text.TextUtils;
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

    int index = 1;
    static final int basePages = 50;

    @Override
    protected void setTile() {
        mTitleBar.setTitle("会议审核");
    }

    @Override
    protected void initData() {
        Map<String, String> params = getParams(index);

        OAService.meetCompany(params, new MeetCallback() {

            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.data != null && response.code == 0) {
                    mList = response.data.pageObject;
                    adapter.setList(new ArrayList<>(mList));
                    mListView.setLoadMoreCount(basePages * index);
                    index++;
                }
            }
        });
    }

    @NonNull
    private Map<String, String> getParams(int Index) {
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
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");
        params.put("passStatus", "");
        return params;
    }

    @Override
    protected void initListView() {
        adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification, new ArrayList(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)) {
            showToast("请输入有效关键字");
            return;
        }
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet meet : mList) {
            if (meet.docTitle.contains(str) || meet.sendDepartmentInfo.contains(str) || meet.department.contains(str) || meet.addr.contains(str)) {
                list.add(meet);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void doRefresh() {
        OAService.meetCompany(getParams(1), new MeetCallback() {

            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    int size = response.data.pageObject.size();
                    Meet node = mList.get(0);
                    int position = 0;
                    for (int i = size - 1; i >= 0; i--) {
                        if (response.data.pageObject.get(i).id.equals(node.id)) {
                            position = i;
                            break;
                        }
                    }
                    for (int i = position - 1; i >= 0; i--) {
                        mList.add(0, response.data.pageObject.get(i));
                    }
                    mListView.setLoadMoreCount((index-1)*basePages+position);
                    adapter.setList(new ArrayList(mList));
                }
            }
        });
    }

    @Override
    protected void doLoadMore() {
        OAService.meetCompany(getParams(index), new MeetCallback() {

            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.data != null && response.code == 0) {
                    mList.addAll(response.data.pageObject);
                    adapter.setList(new ArrayList<>(mList));
                    mListView.setLoadMoreCount(basePages * index);
                    index++;
                }
            }
        });
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mList));
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        MeetAuditActivity_.intent(mContext).extra("mid", ((Meet) o).id).start();
    }
}
