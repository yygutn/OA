package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.EFragment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.edu.jumy.oa.BroadCastReceiver.DocumentBroadcastReceiver;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.DocResponse;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.safe.PasswordUtil;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/6/22 10:17.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_document_all)
public class DocumentAllFragment extends BaseSearchRefreshFragment {


    @Override
    protected void initList() {
        Resources resources = getResources();
        String [] test1 = resources.getStringArray(R.array.test1);
        String [] test2 = resources.getStringArray(R.array.test2);
        String [] test3 = resources.getStringArray(R.array.test3);
        String [] test4 = resources.getStringArray(R.array.test4);
        String [] test5 = resources.getStringArray(R.array.test5);
        String [] test6 = resources.getStringArray(R.array.test6);

        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test1),1));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test2),1));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test3),1));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test4),1));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test5),1));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test6),1));

        documentBroadcastReceiver = new DocumentBroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                super.onReceive(context, intent);
                if (getType() == 2) {
                    // TODO: 16/7/8 赋值操作
                    // mList = getDocList();
                }
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(DocumentBroadcastReceiver.DOC);
        mContext.registerReceiver(documentBroadcastReceiver, filter);
    }
}