package cn.edu.jumy.oa.UI;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Card;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.widget.utils.CardGenerater;


/**
 * @会议卡片列表，所有的会议卡片都在这儿 1
 * Created by Jumy on 16/6/1 13:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_meeting_card)
public class MeetingCardActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener,
        PullToRefreshRecyclerView.PagingableListener, OnItemClickListener {
    @ViewById(R.id.toolbar)
    Toolbar mToolBar;
    @ViewById(R.id.meeting_listView)
    PullToRefreshRecyclerView mListView;

    ImageView mEmptyImageView;

    private List<Card> cardDataList = new ArrayList<>();

    Handler handler = new Handler();

    int lastVisiblePosition = 0;

    @AfterViews
    void start() {
        mContext = this;
        mToolBar.setTitle("会议");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

            mListView.setSwipeEnable(true);

            mListView.setLayoutManager(new LinearLayoutManager(this));

            mListView.setOnRefreshListener(this);
            mListView.setPagingableListener(this);

            //这句话是为了，第一次进入页面的时候显示加载进度条
            mListView.setProgressViewOffset(false, 0, (int) TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                            .getDisplayMetrics()));
            mListView.getRecyclerView().addOnScrollListener(new OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisiblePosition + 1 == mListView.getRecyclerView().getAdapter().getItemCount()) {
                        mListView.onFinishLoading(true, false);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    lastVisiblePosition = ((LinearLayoutManager) mListView.getLayoutManager()).findLastVisibleItemPosition();
                }
            });

            //set list data
            updateList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下拉刷新
     */
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setOnRefreshComplete();
            }
        }, 2500);
    }

    /**
     * 上拉加载
     */
    @Override
    public void onLoadMoreItems() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //coding something before finish loading
                mListView.setOnLoadMoreComplete();
            }
        }, 2500);
    }

    /**
     * 更新会议卡片
     */
    private void updateList() {
        fillArray();
    }

    private void fillArray() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SparseArray<String> array;
        cardDataList = new ArrayList<>();
        String title = "";
        for (int i = 0; i < 10; i++) {
            array = new SparseArray<String>();
            title = "召开传达中央文件精神会议";
            array.put(0, sdf.format(new Date()));
            array.put(1, "省办公厅");
            array.put(2, sdf.format(new Date()));
            String message = CardGenerater.generateNotifyString(0, array);
            cardDataList.add(new Card(title, message, 0));
        }
        MeetingCardAdapter adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification, cardDataList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        SignUpActivity_.intent(mContext).start();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    @Override
    protected void onDestroy() {
        //存入缓存
        ACache.get(mContext).put("meeting_items", (ArrayList<Card>) cardDataList);
        super.onDestroy();
    }
}
