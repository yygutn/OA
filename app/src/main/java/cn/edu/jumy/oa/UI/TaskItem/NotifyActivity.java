package cn.edu.jumy.oa.UI.TaskItem;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.platform.comapi.map.A;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.jumy.oa.CallBack.NotifyCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.NotifyResponse;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.adapter.NotifyAdapter;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Notify;
import okhttp3.Call;

/**
 * Created by Jumy on 16/6/27 16:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class NotifyActivity extends BaseSearchRefreshActivity{
    private ArrayList<Notify> mList = new ArrayList<>();
    NotifyAdapter adapter;
    @Override
    protected void setTile() {
        mTitleBar.setTitle("公告");
    }

    @Override
    protected void initData() {
        OAService.getNoticeList(new NotifyCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("当前网络不可用,请稍后再试");
            }

            @Override
            public void onResponse(NotifyResponse response, int id) {
                if (response.code == 0){
                    mList = response.data;
                } else {
                    showToast("获取公告失败");
                }
            }
        });
    }

    @Override
    protected void initListView() {
        adapter = new NotifyAdapter(mContext,R.layout.item_card_notification,mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = new Node(mList.get(position));
        DetailsActivity_.intent(mContext).extra("details",node).start();
    }

    @Override
    protected void onTextSubmit(String str) {
        if (str == null){
            return;
        }
        ArrayList<Notify> list = new ArrayList<>();
        for (Notify notify : mList){
            if (notify.title.contains(str) || notify.summary.contains(str)){
                list.add(notify);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(mList);
    }

    @Override
    protected void doRefresh() {
        initData();
        adapter.setList(mList);
    }

    @Override
    protected void doLoadMore() {
        initData();
        adapter.setList(mList);
    }
}
