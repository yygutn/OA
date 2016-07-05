package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.squareup.picasso.Picasso;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Card;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.widget.utils.CardGenerator;

/**
 * Created by Jumy on 16/5/19 12:07.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
@EFragment(R.layout.fragment_notify)
public class NotifyFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, PullToRefreshRecyclerView.PagingableListener, OnItemClickListener {
    private Context mContext;
    @ViewById(R.id.notify_listView)
    PullToRefreshRecyclerView mListView;

    ImageView mEmptyImageView;
    private List<Node> cardList = new ArrayList<>();
    public boolean isCache = false;

    Handler handler = new Handler();

    int lastVisiblePosition = 0;
    @AfterViews
    void start() {
        mContext = getActivity();

        mListView.setLoadmoreString("加载中...");

        mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);
        Picasso.with(mContext)
                .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                .resize(200, 200)
                .centerInside()
                .into(mEmptyImageView);
        mListView.setEmptyView(mEmptyImageView);

        mListView.setSwipeEnable(true);

        mListView.setLayoutManager(new LinearLayoutManager(mContext));

        mListView.setOnRefreshListener(this);
        mListView.setPagingableListener(this);

        //这句话是为了，第一次进入页面的时候显示加载进度条
        mListView.setProgressViewOffset(false, 0, (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
                        .getDisplayMetrics()));
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

        // Fill the array withProvider mock content
        fillArray();
    }


    private void fillArray() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SparseArray<String> array;
        cardList = new ArrayList<>();
        String title = "";
        for (int i = 0; i < 10; i++) {
            array = new SparseArray<String>();
            int tag = i%3;
            switch (tag){
                case 0:{
                    title = "(会议)召开传达中央文件精神会议";
                    break;
                }
                case 1:{
                    title = "(公文)省人大常委会公告(第25号)";
                    break;
                }
                case 2:{
                    title = "(公告)浙江省人民政府关于建立江山仙霞岭省级自然保护区的批复";
                    break;
                }
            }
            String message = CardGenerator.generateNotifyString(tag, array);
            cardList.add(new Node(title, message,tag));
        }
        MeetingCardAdapter adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification, cardList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onDestroy() {
        //存入缓存
        ACache.get(getActivity()).put("notify_items", (ArrayList<Node>) cardList);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.setOnRefreshComplete();
            }
        }, 2500);
    }

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

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = (Node) o;
        DetailsActivity_.intent(mContext).extra("details",node).start();
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }
}