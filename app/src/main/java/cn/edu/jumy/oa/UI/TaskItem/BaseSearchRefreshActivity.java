package cn.edu.jumy.oa.UI.TaskItem;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DrawableRes;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.BaseSearchRefreshAdapter;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/27 16:14.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class BaseSearchRefreshActivity extends BaseActivity implements OnItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        PullToRefreshRecyclerView.PagingableListener{
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.search_view)
    MaterialSearchView mSearchView;
    @ViewById(R.id.recView)
    protected PullToRefreshRecyclerView mListView;
    protected BaseSearchRefreshAdapter adapter;
    ImageView mEmptyImageView;

    int lastVisiblePosition = 0;

    Handler handler = new Handler();

    protected ArrayList<Node> mList = new ArrayList<>();

    @AfterViews
    void start(){
        try {

            mListView.setLoadmoreString("加载中...");

            mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);
            mListView.setEmptyView(mEmptyImageView);
            mListView.setSwipeEnable(true);
            mListView.setLayoutManager(new LinearLayoutManager(this));
            mListView.setOnRefreshListener(this);
            mListView.setPagingableListener(this);
            mListView.setLoadMoreCount(20);

            mListView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
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
            initData();
            initListView();
            initSearchView();
            setSupportActionBar(mTitleBar);
        } catch (Exception e) {
            e.printStackTrace();
        }


        setTile();
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    protected void initData() {
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
    }

    protected void setTile(){};
    /**
     * 更新卡片
     */
    protected void initListView() {
        adapter = new BaseSearchRefreshAdapter(mContext, R.layout.item_card_notification, new ArrayList<>(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        showDebugLogw("点击"+position);
        DetailsActivity_.intent(mContext).extra("details",mList.get(position)).start();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }
    private void initSearchView() {
        try {
            mSearchView.setVoiceSearch(false);
            mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    onTextSubmit(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    onTextChange(newText);
                    return false;
                }
            });
            mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {

                }

                @Override
                public void onSearchViewClosed() {
                    onSearchClose();
                }
            });
            mSearchView.setBackgroundColor(pressed);
            mSearchView.setTextColor(Color.WHITE);
            mSearchView.setBackIcon(back);
            mSearchView.setCloseIcon(clear);
            mSearchView.setHintTextColor(Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void onTextChange(String text) {

    }

    @DrawableRes(R.drawable.ic_arrow_back_white)
    Drawable back;
    @DrawableRes(R.drawable.ic_clear_white)
    Drawable clear;
    @ColorRes(R.color.pressed)
    int pressed;
    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
    protected void onTextSubmit(String str){
        if (str == null){
            return;
        }
        List<Node> list = new ArrayList<>();
        list.add(mList.get(0));
        adapter.setList(list);
    }
    protected void onSearchClose(){
        adapter.setList(new ArrayList<>(mList));
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doRefresh();
                mListView.setOnRefreshComplete();
            }
        }, 2500);
    }
    protected void doRefresh(){};
    protected void doLoadMore(){};
    @Override
    public void onLoadMoreItems() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //coding something before finish loading
                doLoadMore();
                mListView.setOnLoadMoreComplete();
            }
        }, 2500);
    }
}
