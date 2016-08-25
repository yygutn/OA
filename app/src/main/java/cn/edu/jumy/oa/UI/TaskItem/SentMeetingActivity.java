package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.adapter.SentMeetAdapter;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/7/12 20:25.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class SentMeetingActivity extends BaseSearchRefreshActivity {

    private ArrayList<Meet> mList = new ArrayList<>();
    SentMeetAdapter adapter;

    @Override
    protected void setTile() {
        mTitleBar.setTitle("已发布会议");
    }

    int index = 1;
    static final int basePages = 50;

    @Override
    protected void initData() {
        OAService.meetUser(getParams(index), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList = response.data.pageObject;
                    adapter.setList(response.data.pageObject);
                    mListView.setLoadMoreCount(index++ * basePages);
                }
            }
        });
    }

    @NonNull
    private Map<String, String> getParams(int Index) {

        final Map<String, String> params = new ArrayMap<>();
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("signStatus", "");
        params.put("passStatus", "");
        params.put("meetCompany", "");
        return params;
    }

    @Override
    protected void initListView() {
        adapter = new SentMeetAdapter(mContext, R.layout.item_notify_notification, new ArrayList<>(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = new Node((Meet) o);
        SignDetailsActivity.flag = false;
        DetailsActivity_.intent(mContext).extra("details", node).extra("from_sent_meet", true).start();
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)) {
            showToast("请输入有效关键字");
            return;
        }
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet meet : mList) {
            if (meet.docTitle.contains(str)) {
                list.add(meet);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void doRefresh() {
        OAService.meetUser(getParams(1), new MeetCallback() {
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
                    mListView.setLoadMoreCount((index - 1) * basePages + position);
                    adapter.setList(new ArrayList(mList));
                }
            }
        });
    }

    @Override
    protected void doLoadMore() {
        OAService.meetUser(getParams(index), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList.addAll(response.data.pageObject);
                    adapter.setList(new ArrayList<>(mList));
                    mListView.setLoadMoreCount(index++ * basePages);
                }
            }
        });
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mList));
    }
}
