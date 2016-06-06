package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.listeners.RecyclerItemClickListener;
import com.dexafree.materialList.view.MaterialListView;
import com.squareup.picasso.Picasso;
import com.tencent.qcloud.tlslibrary.activity.BaseFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.SignUpActivity_;
import cn.edu.jumy.oa.bean.CardData;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.widget.utils.CardGenerater;
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
        fillArray();
//        getCardDataList();

        // Set the dismiss listener
        //cn:滑动删除监听
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
                switch ((int) card.getTag()) {
                    case 0: {//meeting
                        SignUpActivity_.intent(getActivity()).start();
                        break;
                    }
                    case 1: {//notice
                        break;
                    }
                    case 2: {//document
                        break;
                    }
                }
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
            String message = generateNotifyString(0,new SparseArray<String>());
            cards.add(getCard(0, "会议(点击查看详情)", message));
            cardDataList.add(new CardData("通知", "会议", message));
        }
        mListView.getAdapter().addAll(cards);
    }

    private String generateNotifyString(int tag, SparseArray<String> list) {
        String message = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = sdf.format(new Date());
        if (tag == 0) {
            message += "发文时间: " + list.get(0, time) + "\n";
            message += "承办单位: " + list.get(1, "省办公厅") + "\n";
            message += "会议时间: " + list.get(2, time) + "\n";
            message += "会议地点: " + list.get(3, "省办公厅11楼" + 110 + new Random().nextInt(9)) + "\n";
//            message += "会议名称: " + list.get(4, new Random().nextBoolean() ? "关于召开省委城市工作会议的通知" : "召开传达中央文件精神会议") + "\n";
        } else if (tag == 1) {

        } else if (tag == 2) {

        }
        return message;
    }

    public Card getCard(int tag, String subtitle, String message) {
        boolean flag = new Random().nextBoolean();
        subtitle = (new Random().nextBoolean() ? "关于召开省委城市工作会议的通知" : "召开传达中央文件精神会议");
        subtitle = "(会议)"+subtitle;
        final CardProvider provider = new Card.Builder(mContext)
                .setTag(tag)
//                .setDismissible()//添加之后，滑动删除
                .withProvider(new CardProvider())
                .setLayout(R.layout.item_card_notification)
                .setDescription(message)
                .setDescriptionColor(Color.WHITE)
                .setSubtitle(subtitle)
                .setSubtitleColor(Color.WHITE)
                .setBackgroundColor(getResources().getColor(R.color.pressed));
        return provider.endConfig().build();
    }

    @ColorRes(R.color.colorPrimary)
    int blue;

    private void getCardDataList() {
        cardDataList = (ArrayList<CardData>) ACache.get(getActivity()).getAsObject("notify_items");
        if (cardDataList != null) {
            cards.clear();
            for (CardData item : cardDataList) {
                cards.add(getCard(item.getTAG(), item.getSubTitle(), item.getMessage()));
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