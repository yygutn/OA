package cn.edu.jumy.oa.UI.TaskItem;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hyphenate.chatuidemo.DemoHelper;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.squareup.picasso.Picasso;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.DocumentAdapter;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;

/**
 * Created by Jumy on 16/6/20 13:57.
 * @文件柜 文件列表和检索功能
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class DocumentCabinetActivity extends BaseActivity implements OnItemClickListener {
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.recView)
    protected PullToRefreshRecyclerView mListView;

    ImageView mEmptyImageView;

    int lastVisiblePosition = 0;

    Handler handler = new Handler();

    private ArrayList<Node> mList = new ArrayList<>();

    @AfterViews
    void start(){
        try {
//            LoadMoreView loadMoreView = new LoadMoreView(this, mListView.getRecyclerView());
//            loadMoreView.setLoadmoreString("加载中...");
//            loadMoreView.setLoadMorePadding(100);
//            mListView.setLoadMoreFooter(loadMoreView);

            mListView.setLoadmoreString("加载中...");

            mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);
            Picasso.with(mContext)
                    .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                    .resize(200, 200)
                    .centerInside()
                    .into(mEmptyImageView);
            mListView.setEmptyView(mEmptyImageView);

            mListView.setLayoutManager(new LinearLayoutManager(this));

            //这句话是为了，第一次进入页面的时候显示加载进度条
            mListView.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                            .getDisplayMetrics()));

            //set list data
            updateList();
        } catch (Exception e) {
            e.printStackTrace();
        }



        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
    }
    /**
     * 更新卡片
     */
    private void updateList() {
        DocumentAdapter adapter = new DocumentAdapter(mContext, R.layout.item_document_cabinet, mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        showDebugLogw("点击"+position);
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ACache.get(MyApplication.getContext()).put(DemoHelper.getInstance().getCurrentUsernName(), mList);
    }
}
