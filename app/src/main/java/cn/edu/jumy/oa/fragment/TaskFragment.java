package cn.edu.jumy.oa.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.edu.jumy.oa.UI.ApprovalActivity;
import cn.edu.jumy.oa.UI.ApprovalActivity_;
import cn.edu.jumy.oa.UI.CalendarActivity_;
import cn.edu.jumy.oa.UI.NotifyActivity_;
import cn.edu.jumy.oa.dragrecyclerview.adapter.RecyclerAdapter;
import cn.edu.jumy.oa.dragrecyclerview.common.DividerGridItemDecoration;
import cn.edu.jumy.oa.dragrecyclerview.entity.Item;
import cn.edu.jumy.oa.dragrecyclerview.helper.MyItemTouchCallback;
import cn.edu.jumy.oa.dragrecyclerview.helper.OnRecyclerItemClickListener;
import cn.edu.jumy.oa.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.dragrecyclerview.utils.VibratorUtil;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;

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
public class TaskFragment extends Fragment implements MyItemTouchCallback.OnDragListener {

    private List<Item> results = new ArrayList<Item>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////////////////////////////////////////////////
        /////////初始化数据，如果缓存中有就使用缓存中的
        ArrayList<Item> items = (ArrayList<Item>) ACache.get(getActivity()).getAsObject("items");
        if (items != null && false)
            results.addAll(items);
        else {
            for (int i = 0; i < 1; i++) {
                results.add(new Item(0, "审批", R.drawable.takeout_ic_category_flower));
//                results.add(new Item(i * 7 + 1, "公告", R.drawable.takeout_ic_category_fruit));
                results.add(new Item(1, "签到", R.drawable.takeout_ic_category_medicine));
                results.add(new Item(2, "日志", R.drawable.takeout_ic_category_motorcycle));
                results.add(new Item(3, "邮件", R.drawable.takeout_ic_category_sweet));
                results.add(new Item(4, "会议", R.drawable.takeout_ic_category_store));
                results.add(new Item(5, "收文", R.drawable.takeout_ic_category_public));
                results.add(new Item(6, "发文", R.drawable.takeout_ic_category_medicine));
                results.add(new Item(7, "公文阅读", R.drawable.takeout_ic_category_motorcycle));
            }
        }
//        results.remove(results.size() - 1);
        results.add(new Item(results.size(), "更多", R.drawable.takeout_ic_more));
        ////////////////////////////////////////////////////////
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.fragment_task, null);
    }

    private RecyclerView recyclerView;
    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != results.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Item item = results.get(vh.getLayoutPosition());
                Toast.makeText(getActivity(), item.getId() + " " + item.getName(), Toast.LENGTH_SHORT).show();
                switch (item.getId()) {
                    case 0: {
                        ApprovalActivity_.intent(getActivity()).start();
                        break;
                    }
                    case 4:{
                        CalendarActivity_.intent(getActivity()).start();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onFinishDrag() {
        //存入缓存
        ACache.get(getActivity()).put("items", (ArrayList<Item>) results);
    }
}