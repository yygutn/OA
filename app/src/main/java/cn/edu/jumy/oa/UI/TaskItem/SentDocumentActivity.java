package cn.edu.jumy.oa.UI.TaskItem;

import android.text.TextUtils;

import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    SentDocAdapter adapter;
    @Override
    protected void setTile() {
        mTitleBar.setTitle("已发送公文");
    }

    @Override
    protected void initData() {
        mList = null;

        final Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("signStatus", "");

        OAService.docUser(params, new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null){
                    mListDoc = response.data.pageObject;
                    adapter.setList(response.data.pageObject);
                    mListView.setLoadMoreCount(20);
                }
            }
        });
    }

    @Override
    protected void initListView() {
        adapter = new SentDocAdapter(mContext, R.layout.item_sent_xx, new ArrayList<>(mListDoc));
        mListView.setAdapter(adapter);
        mListView.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(mContext));
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)){
            showToast("请输入有效关键字");
            return;
        }
        ArrayList<Doc> list = new ArrayList<>();
        for (Doc doc : mListDoc){
            if (doc.docTitle.contains(str)){
                list.add(doc);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mListDoc));
    }
}
