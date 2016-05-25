package cn.edu.jumy.oa.bean;

import com.dexafree.materialList.card.Card;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.HomeActivity;
import cn.edu.jumy.oa.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.fragment.NotifyFragment;

/**
 * Created by Jumy on 16/5/25 15:34.
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
@EBean
public class NotifyFragmentBackgroundTask {


    private ArrayList<CardData> items;
    @RootContext
    HomeActivity fragment;

    @Background
    public void initData() {
        /////////初始化数据，如果缓存中有就使用缓存中的
//        items = (ArrayList<CardData>) ACache.get(fragment.getActivity()).getAsObject("notify_items");
        List<Card> cards = new ArrayList<>();
        if (items != null){
            for (CardData item : items){
//                cards.add(fragment.getNotificationCard(item.getTitle(),item.getSubTitle(),item.getMessage()));
            }
//            fragment.isCache = true;
        }
        updateUI(items);
    }

    @UiThread
    public void updateUI(ArrayList<CardData> items) {
    }
}
