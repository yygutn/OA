package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import org.androidannotations.annotations.EFragment;

import cn.edu.jumy.oa.BroadCastReceiver.DocumentBroadcastReceiver;
import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/6/22 10:17.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_document_all)
public class DocumentAllFragment extends BaseSearchRefreshFragment {


    @Override
    protected void initList() {

        documentBroadcastReceiver = new DocumentBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if (getType() == 2) {
                    // xxx: 16/7/8 赋值操作
                    mList = getDocList();
                    updateListView();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DocumentBroadcastReceiver.DOC);
        mContext.registerReceiver(documentBroadcastReceiver, filter);
    }

    @Override
    protected void updateListView() {
        super.updateListView();
        if (mList == null || mList.size() == 0) {
            // TODO: 16/7/12 无公文交互提示
        }
    }
}