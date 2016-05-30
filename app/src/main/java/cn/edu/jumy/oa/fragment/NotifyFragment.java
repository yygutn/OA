package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tlslibrary.BuildConfig;
import com.tencent.qcloud.tlslibrary.activity.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.CalendarActivity_;
import cn.edu.jumy.oa.bean.CardData;
import cn.edu.jumy.oa.dragrecyclerview.utils.ACache;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

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
public class NotifyFragment extends BaseFragment {
    private Context mContext;
    @ViewById(R.id.notify_listView)
    MaterialListView mListView;

    private List<Card> cards = new ArrayList<>();
    private List<CardData> cardDataList = new ArrayList<>();
    public boolean isCache = false;


    @AfterViews
    void start() {
        mContext = getActivity();

        // Bind the MaterialListView to a variable
        mListView.setItemAnimator(new SlideInLeftAnimator());
        mListView.getItemAnimator().setAddDuration(300);
        mListView.getItemAnimator().setRemoveDuration(300);

        final ImageView emptyView = (ImageView) getActivity().findViewById(R.id.imageView);
        emptyView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mListView.setEmptyView(emptyView);
        Picasso.with(mContext)
                .load("https://www.skyverge.com/wp-content/uploads/2012/05/github-logo.png")
                .resize(100, 100)
                .centerInside()
                .into(emptyView);

        // Fill the array withProvider mock content
//        fillArray();
        getCardDataList();

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
                CalendarActivity_.intent(getActivity()).start();
            }

            @Override
            public void onItemLongClick(@NonNull Card card, int position) {
                Log.d("LONG_CLICK", "" + card.getTag());
            }
        });
    }

    public void updateUI(List<Card> items) {
        if (items != null && items.size() > 0) {
            showDebugLoge("from local cache");
            mListView.getAdapter().addAll(items);
        } else {
            showDebugLoge("from new data");
            fillArray();
        }
    }


    private void fillArray() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        cardDataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String time = sdf.format(new Date());
            time = time + "在" + new Random().nextInt(1000) + "开会"+"\n";
            time +=  "承办单位:省办公厅"+"\n";
            time +=  new Random().nextBoolean()?"会议名称:关于召开省委城市工作会议的通知":"会议名称:召开传达中央文件精神会议";
            cards.add(getNotificationCard("通知", "会议(点击查看详情)", time));
            cardDataList.add(new CardData("通知", "会议", time));
        }
        mListView.getAdapter().addAll(cards);
    }

    public Card getNotificationCard(String title, String subtitle, String message) {
        boolean flag = new Random().nextBoolean();
        final CardProvider provider = new Card.Builder(mContext)
                .setTag("WELCOME_CARD")
                .setDismissible()
                .withProvider(new CardProvider())
                .setLayout(R.layout.item_card_notification)
                .setTitle(title)
                .setTitleColor(flag?Color.WHITE:Color.BLACK)
                .setDescription(message)
                .setDescriptionColor(flag?Color.WHITE:Color.BLACK)
                .setSubtitle(subtitle)
                .setSubtitleColor(flag?Color.WHITE:Color.BLACK)
                .setBackgroundColor(flag?getResources().getColor(R.color.pressed):Color.WHITE)
                .addAction(R.id.ok_button, new TextViewAction(mContext)
                        .setText(flag?"未读":"已读")
                        .setTextColor(flag?Color.WHITE:Color.WHITE)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                try {
                                    showDebugLoge("点击");
                                    TextViewAction action = (TextViewAction) card.getProvider().getAction(R.id.ok_button);
                                    action.setText("已读");
                                    action.setTextColor(Color.BLACK);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }));

        return provider.endConfig().build();
    }
    @ColorRes(R.color.colorPrimary)
    int blue;

    void getCardDataList() {
        cardDataList = (ArrayList<CardData>) ACache.get(getActivity()).getAsObject("notify_items");
        if (cardDataList != null) {
            cards.clear();
            for (CardData item : cardDataList) {
                cards.add(getNotificationCard(item.getTitle(), item.getSubTitle(), item.getMessage()));
            }
            isCache = true;
        }
        updateUI(cards);
    }

    @Override
    public void onDestroy() {
        //存入缓存
        ACache.get(getActivity()).put("notify_items", (ArrayList<CardData>) cardDataList);
        super.onDestroy();
    }

}