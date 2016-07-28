package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.DocCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.DocResponse;
import cn.edu.jumy.oa.adapter.SentDocAdapter;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.customview.SimpleDividerItemDecoration;

/**
 * Created by Jumy on 16/7/12 20:25.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class SentDocumentActivity extends BaseSearchRefreshActivity {

    private ArrayList<Doc> mList = new ArrayList<>();
    SentDocAdapter adapter;

    @Override
    protected void setTile() {
        mTitleBar.setTitle("已发送公文");
    }

    int index = 0;
    static final int basePages = 50;

    @Override
    protected void initData() {
        final Map<String, String> params = getParams(index);

        OAService.docUser(params, new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList = response.data.pageObject;
                    adapter.setList(response.data.pageObject);
                    mListView.setLoadMoreCount(20);
                }
            }
        });
    }

    @NonNull
    private Map<String, String> getParams(int Index) {
        final Map<String, String> params = new HashMap<>();
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("signStatus", "");
        return params;
    }

    @Override
    protected void initListView() {
        adapter = new SentDocAdapter(mContext, R.layout.item_sent_xx, new ArrayList<>(mList));
        mListView.setAdapter(adapter);
        mListView.getRecyclerView().addItemDecoration(new SimpleDividerItemDecoration(mContext));
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = new Node((Doc) o);
        DetailsActivity_.intent(mContext).extra("details", node).start();
    }

    @Override
    protected void doRefresh() {
        OAService.docUser(getParams(1), new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    int size = response.data.pageObject.size();
                    Doc node = mList.get(0);
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
        OAService.docUser(getParams(index), new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList.addAll(response.data.pageObject);
                    adapter.setList(new ArrayList<>(mList));
                    mListView.setLoadMoreCount(index++ * basePages);
                }
            }
        });
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)) {
            showToast("请输入有效关键字");
            return;
        }
        ArrayList<Doc> list = new ArrayList<>();
        for (Doc doc : mList) {
            if (doc.docTitle.contains(str)) {
                list.add(doc);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mList));
    }
}
