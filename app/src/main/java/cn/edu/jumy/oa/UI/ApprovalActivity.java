package cn.edu.jumy.oa.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.dragrecyclerview.adapter.RecyclerAdapter;
import cn.edu.jumy.oa.dragrecyclerview.common.DividerGridItemDecoration;
import cn.edu.jumy.oa.dragrecyclerview.entity.Item;
import cn.edu.jumy.oa.dragrecyclerview.helper.MyItemTouchCallback;
import cn.edu.jumy.oa.dragrecyclerview.helper.OnRecyclerItemClickListener;
import cn.edu.jumy.oa.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.dragrecyclerview.utils.VibratorUtil;

/**
 * Created by Jumy on 16/5/23 09:38.
 * 审核页面
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
@EActivity(R.layout.activity_approval)
public class ApprovalActivity extends BaseActivity implements MyItemTouchCallback.OnDragListener {
    private List<Item> results = new ArrayList<Item>();

    private Context mContext;

    @ViewById(R.id.toolbar)
    Toolbar mToolBar;

    @ViewById(R.id.approval_mine)
    TextView mAppMine;

    @ViewById(R.id.approval_wait)
    TextView mAppWait;

    @ViewById(R.id.approval_sick_leave_visible)
    LinearLayout mAppVisible;

    @ViewById(R.id.recView)
    RecyclerView mListView;
    private ItemTouchHelper itemTouchHelper;

    @AfterViews
    void start() {
        AppManager.getInstance().addActivity(this);
        mContext = this;
        ////////////////////////////////////////////////////////
        /////////初始化数据，如果缓存中有就使用缓存中的
        ArrayList<Item> items = (ArrayList<Item>) ACache.get(mContext).getAsObject("approval_items");
        if (items != null && items.size() > 0) {
            results.addAll(items);
            showDebugLoge("from local cache");
        } else {
            showDebugLoge("from new data");
            results.add(new Item(0, "请假", R.drawable.ask_for_leave));
            results.add(new Item(1, "报销", R.drawable.purchase));
            results.add(new Item(2, "物品领用", R.drawable.recipients));
            results.add(new Item(3, "通用审批", R.drawable.approval_normal));
            results.add(new Item(4, "付款", R.drawable.payment));
            results.add(new Item(5, "采购", R.drawable.write_off));
        }
//        results.remove(results.size() - 1);
//        results.add(new Item(results.size(), "更多", R.drawable.takeout_ic_more));
        ////////////////////////////////////////////////////////
        initRecyclerViews();
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Click({R.id.approval_sick_leave, R.id.approval_sick_leave_invisible, R.id.approval_wait, R.id.approval_mine})
    void click(View view) {
        switch (view.getId()) {
            case R.id.approval_mine: {
                showToast("我发起的");
                break;
            }
            case R.id.approval_wait: {
                showToast("待我审批");
                break;
            }
            case R.id.approval_sick_leave: {
                break;
            }
            case R.id.approval_sick_leave_invisible: {
                mAppVisible.setVisibility(View.GONE);
                break;
            }
            default:
                break;
        }
    }

    private void initRecyclerViews() {
        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        mListView = (RecyclerView) findViewById(R.id.recView);
        mListView.setHasFixedSize(true);
        mListView.setAdapter(adapter);
        mListView.setLayoutManager(new GridLayoutManager(mContext, 4));
        mListView.addItemDecoration(new DividerGridItemDecoration(mContext));

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(mListView);

        mListView.addOnItemTouchListener(new OnRecyclerItemClickListener(mListView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != results.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(mContext, 70);   //震动70ms
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Bundle bundle;
                Item item = results.get(vh.getLayoutPosition());
                Toast.makeText(mContext, item.getId() + " " + item.getName(), Toast.LENGTH_SHORT).show();
                switch (item.getId()) {
                    case 0: {
                        bundle = new Bundle();
                        bundle.putString("title", "请假");
                        bundle.putString("img", "file:///android_asset/img/img_2226.png");
                        TempActivity_.intent(mContext).extra("temp", bundle).start();
                        break;
                    }
                    case 1: {
                        bundle = new Bundle();
                        bundle.putString("title", "报销");
                        bundle.putString("img", "file:///android_asset/img/img_2227.png");
                        TempActivity_.intent(mContext).extra("temp", bundle).start();
                        break;
                    }
                    case 2: {
                        bundle = new Bundle();
                        bundle.putString("title", "物品领用");
                        bundle.putString("img", "file:///android_asset/img/img_2228.png");
                        TempActivity_.intent(mContext).extra("temp", bundle).start();
                        break;
                    }
                    case 4: {
                        bundle = new Bundle();
                        bundle.putString("title", "付款");
                        bundle.putString("img", "file:///android_asset/img/img_2230.png");
                        TempActivity_.intent(mContext).extra("temp", bundle).start();
                        break;
                    }
                    case 5: {
                        bundle = new Bundle();
                        bundle.putString("title", "采购");
                        bundle.putString("img", "file:///android_asset/img/img_2229.png");
                        TempActivity_.intent(mContext).extra("temp", bundle).start();
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
        ACache.get(mContext).put("approval_items", (ArrayList<Item>) results);
    }
}
