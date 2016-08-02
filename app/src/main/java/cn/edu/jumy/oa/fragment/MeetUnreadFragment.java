package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

import cn.edu.jumy.oa.BroadCastReceiver.MeetBroadcastReceiver;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.MeetingCardActivity;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/22 10:17.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_document_all)
public class MeetUnreadFragment extends BaseSearchRefreshFragment implements SwipeRefreshLayout.OnRefreshListener {
    MeetingCardAdapter adapter;
    ArrayList<Meet> mList = new ArrayList<>();

    MeetBroadcastReceiver meetBroadcastReceiver = new MeetBroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (getType() == 1) {
                mList = getMeetList();
                adapter.setList(new ArrayList<>(mList));
            }
        }
    };

    @Override
    protected void initList() {
        mListView.setSwipeEnable(true);
        mListView.setOnRefreshListener(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(MeetBroadcastReceiver.MEET);
        mContext.registerReceiver(meetBroadcastReceiver, filter);
    }

    @Override
    protected void updateListView() {
        adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification, new ArrayList(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onTextChanged(CharSequence message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet meet : mList) {
            if (meet.docTitle.contains(message)) {
                list.add(meet);
            }
        }
        adapter.setList(list);
    }

    @Override
    public void onSearchCancel() {
        adapter.setList(new ArrayList<Meet>(mList));
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        //下一步，进入详情界面...
        Node node = new Node((Meet) o);
        DetailsActivity_.intent(mContext).extra("details", node).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (meetBroadcastReceiver != null) {
                mContext.unregisterReceiver(meetBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Handler mHandler = new Handler();

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((MeetingCardActivity) getActivity()).getData();
                mListView.setOnRefreshComplete();
            }
        }, 1500);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mHandler = null;
    }
}
