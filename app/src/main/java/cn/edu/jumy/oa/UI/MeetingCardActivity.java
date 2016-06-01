package cn.edu.jumy.oa.UI;

import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.CardData;
import cn.edu.jumy.oa.widget.utils.CardGenerater;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;


/**
 * @会议卡片列表，所有的会议卡片都在这儿
 * Created by Jumy on 16/6/1 13:18.
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
@EActivity(R.layout.activity_meeting_card)
public class MeetingCardActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @ViewById(R.id.toolbar)
    Toolbar mToolBar;
    @ViewById(R.id.meeting_listView)
    MaterialListView mListView;
    @ViewById(R.id.imageView)
    ImageView mEmptyView;
    @ViewById(R.id.swipe_container)
    SwipeRefreshLayout mSwipeLayout;

    private List<Card> cards = new ArrayList<>();
    private List<CardData> cardDataList = new ArrayList<>();

    @AfterViews
    void start(){
        mContext = this;

        // Bind the MaterialListView to a variable
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        mEmptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(mEmptyView);
        Picasso.with(mContext)
                .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                .resize(100, 100)
                .centerInside()
                .into(mEmptyView);

        //set list data
        updateList();
        // Set the dismiss listener
        mListView.setOnDismissCallback(new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull Card card, int position) {
                // Show a toast
                Toast.makeText(mContext, "You have dismissed a " + card.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        // Add the ItemTouchListener
        mListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull Card card, int position) {
                Log.d("CARD_TYPE", "" + card.getTag());
                // TODO: 16/6/1 修改为跳转到详情报名页面
//                CalendarActivity_.intent(mContext).start();
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });

        mSwipeLayout.setOnRefreshListener(this);
        // 设置下拉圆圈上的颜色，蓝色、绿色、橙色、红色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeLayout.setDistanceToTriggerSync(400);// 设置手指在屏幕下拉多少距离会触发下拉刷新
//        mSwipeLayout.setProgressBackgroundColor(Color.WHITE); // 设定下拉圆圈的背景
        mSwipeLayout.setSize(SwipeRefreshLayout.LARGE); // 设置圆圈的大小
    }


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //更新数据，停止刷新
                updateList();
                mSwipeLayout.setRefreshing(false);
            }
        },2500);
    }

    /**
     * 更新会议卡片
     */
    private void updateList() {
        fillArray();
    }

    private void fillArray() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        cardDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String time = sdf.format(new Date());
            time = time + "在" + new Random().nextInt(1000) + "开会"+"\n";
            time +=  "承办单位:省办公厅"+"\n";
            time +=  new Random().nextBoolean()?"会议名称:关于召开省委城市工作会议的通知":"会议名称:召开传达中央文件精神会议";
            cards.add(CardGenerater.getNotificationCard(mContext,"通知", "会议(点击查看详情)", time));
            cardDataList.add(new CardData("通知", "会议", time));
        }
        mListView.getAdapter().addAll(cards);
    }
}
