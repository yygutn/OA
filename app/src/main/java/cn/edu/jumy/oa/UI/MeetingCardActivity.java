package cn.edu.jumy.oa.UI;

import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;
import com.zhy.http.okhttp.callback.Callback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.bean.Node;
import okhttp3.Call;
import okhttp3.Response;


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
    MeetingCardAdapter adapter;

    ImageView mEmptyImageView;

    private List<Meet> mList = new ArrayList<>();

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
            mListView.setLoadmoreString("加载中...");

            mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);
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
            adapter = new MeetingCardAdapter(mContext,R.layout.item_card_notification,mList);
            mListView.setAdapter(adapter);
            adapter.setOnItemClickListener(this);

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) - 1900;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if ((month < 8 && (month & 1) == 1) || (month >= 8 && (month & 1) == 0)) {
            if (day == 31) {
                day--;
            }
            if (month == 2 && day > 28) {
                day = 28;
            }
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
        } else {
            month--;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("meetCompany", "");
        params.put("signStatus", "");
        params.put("passStatus", "");

        OAService.meetReceive(params, new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response.code == 1 || response.data == null) {
                    showToast("获取会议列表失败");
                    adapter.setList(new ArrayList<Meet>());
                    return;
                }
                adapter.setList(response.data.pageObject);
            }
        });


    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = new Node((Meet) o);
        DetailsActivity_.intent(mContext).extra("details",node).start();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    abstract class MeetCallback extends Callback<MeetResponse> {
        @Override
        public MeetResponse parseNetworkResponse(Response response, int id) throws Exception {
            try {
                String data = response.body().string();
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
                if (baseResponse.code == 0) {
                    return gson.fromJson(data, MeetResponse.class);
                } else if (baseResponse.code == 1) {
                    return new MeetResponse(baseResponse);
                }
            } catch (Exception e) {
                onError(null, e, 0);
            }
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showDebugException(e);
            showToast("获取会议列表失败");
        }
    }
}
