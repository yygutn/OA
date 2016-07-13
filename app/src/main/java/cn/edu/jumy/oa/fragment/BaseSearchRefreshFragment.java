package cn.edu.jumy.oa.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.BroadCastReceiver.DocumentBroadcastReceiver;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.adapter.DocumentAdapter;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;

/**
 * Created by Jumy on 16/6/22 18:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_document_all)
public class BaseSearchRefreshFragment extends BaseFragment implements OnItemClickListener {


    DocumentBroadcastReceiver documentBroadcastReceiver;

    @ViewById(R.id.listview)
    protected PullToRefreshRecyclerView mListView;

    protected ImageView mEmptyImageView;

    protected ArrayList<Doc> mList = new ArrayList<>();
    protected DocumentAdapter adapter;

    @AfterViews
    protected void start() {
        mContext = getActivity();
        try {
            mListView.setLoadmoreString("加载中...");

            mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);
            mListView.setEmptyView(mEmptyImageView);

            mListView.setLayoutManager(new LinearLayoutManager(mContext));

            //这句话是为了，第一次进入页面的时候显示加载进度条
            mListView.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                            .getDisplayMetrics()));

            //set list data
            initList();
            updateListView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void initList() {
    }


    /**
     * 更新卡片
     */
    protected void updateListView() {
        adapter = new DocumentAdapter(mContext, R.layout.item_card_notification, new ArrayList<>(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        showDebugLogw("点击" + position);
        Node node = new Node((Doc) o);
        DetailsActivity_.intent(mContext).extra("details", node).start();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (documentBroadcastReceiver != null) {
                mContext.unregisterReceiver(documentBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onTextChanged(CharSequence message) {
        if (message == null) {
            return;
        }
        List<Doc> list = new ArrayList<>();
        // TODO: 16/6/22  根据搜索提供的字符串，处理&&筛选出所需的数据
        for (Doc document : mList) {
            if (document.docTitle.contains(message) || document.docSummary.contains(message) || document.docNo.contains(message)) {
                list.add(document);
            }
        }
        adapter.setList(list);
    }


    public void onSearchCancel() {
        adapter.setList(new ArrayList<>(mList));
    }
}