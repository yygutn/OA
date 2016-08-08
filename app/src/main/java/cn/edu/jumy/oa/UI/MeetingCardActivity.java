package cn.edu.jumy.oa.UI;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.PageScrollStateChanged;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DrawableRes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.BroadCastReceiver.MeetBroadcastReceiver;
import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.fragment.BaseSearchRefreshFragment;
import cn.edu.jumy.oa.fragment.MeetAllFragment_;
import cn.edu.jumy.oa.fragment.MeetReadFragment_;
import cn.edu.jumy.oa.fragment.MeetUnreadFragment_;
/**
 * @会议卡片列表，所有的会议卡片都在这儿 1
 * Created by Jumy on 16/6/1 13:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_shen_pi)
public class MeetingCardActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;

    protected Indicator indicator;
    @ViewById(R.id.document_viewPager)
    protected ViewPager viewPager;

    @ViewById(R.id.search_view)
    MaterialSearchView mSearchView;

    private IndicatorViewPager indicatorViewPager;

    MyAdapter adapter;

    int index = 1;
    static final int basePages = 100;

    @AfterExtras
    public void getData() {
        OAService.meetReceive(getParams(index), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response.code == 1 || response.data == null) {
                    showToast("获取会议列表失败");
                    return;
                }
                ArrayList<Meet> list = response.data.pageObject;
                //已签收-list
                ArrayList<Meet> list0 = new ArrayList<>();
                //未签收 -list
                ArrayList<Meet> list1 = new ArrayList<>();

                for (Meet meet : list) {
                    if (meet.signStatus == 0) {
                        list0.add(meet);
                    } else if (meet.signStatus == 1) {
                        list1.add(meet);
                    }
                }
                //已签收
                Intent intent = new Intent(MeetBroadcastReceiver.MEET);
                intent.putParcelableArrayListExtra(MeetBroadcastReceiver.MEET_LIST, list0);
                intent.putExtra(MeetBroadcastReceiver.TYPE, 0);
                sendBroadcast(intent);
                //未签收
                intent = new Intent(MeetBroadcastReceiver.MEET);
                intent.putParcelableArrayListExtra(MeetBroadcastReceiver.MEET_LIST, list1);
                intent.putExtra(MeetBroadcastReceiver.TYPE, 1);
                sendBroadcast(intent);
                //全部
                intent = new Intent(MeetBroadcastReceiver.MEET);
                intent.putParcelableArrayListExtra(MeetBroadcastReceiver.MEET_LIST, list);
                intent.putExtra(MeetBroadcastReceiver.TYPE, 2);
                sendBroadcast(intent);
            }
        });
    }

    private Map<String, String> getParams(int Index) {

        Map<String, String> params = new HashMap<>();
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("startTime", "");
        params.put("endTime", "");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("meetCompany", "");
        params.put("signStatus", "");
        params.put("passStatus", "");
        return params;
    }


    @AfterViews
    void newStart() {
        indicator = (Indicator) findViewById(R.id.document_indicator);
        float unSelectSize = 16;
        float selectSize = unSelectSize * 1.2f;

        int selectColor = getResources().getColor(R.color.pressed);
        int unSelectColor = getResources().getColor(R.color.normal);
        indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.pressed), 5));
        indicator.setOnTransitionListener(new OnTransitionTextListener().setSize(selectSize, unSelectSize).setColor(selectColor, unSelectColor));
        viewPager.setOffscreenPageLimit(3);
        adapter = new MyAdapter(getSupportFragmentManager());
        indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
        indicatorViewPager.setAdapter(adapter);

        initSearchView();
        setSupportActionBar(mToolBar);

        mToolBar.setTitle("会议通知");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @PageScrollStateChanged(R.id.document_viewPager)
    void onPageScrollStateChangedNoParam() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        }
    }
    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {"未签收", "已签收", "全部"};
        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
            inflater = LayoutInflater.from(getApplicationContext());
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            try {
                if (convertView == null) {
                    convertView = (TextView) inflater.inflate(R.layout.document_tab, container, false);
                }
                ((TextView) convertView).setText(tabNames[position]);
                return convertView;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position) {
                case 0: {
                    return new MeetUnreadFragment_();
                }
                case 1: {
                    return new MeetReadFragment_();
                }
                case 2: {
                    return new MeetAllFragment_();
                }

                default:
                    return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document, menu);
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
                    BaseSearchRefreshFragment baseSearchRefreshFragment = (BaseSearchRefreshFragment) adapter.getCurrentFragment();
                    showDebugLogw(baseSearchRefreshFragment.getClass().getSimpleName());
                    baseSearchRefreshFragment.onTextChanged(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                @Override
                public void onSearchViewShown() {

                }

                @Override
                public void onSearchViewClosed() {
                    BaseSearchRefreshFragment baseSearchRefreshFragment = (BaseSearchRefreshFragment) adapter.getCurrentFragment();
                    showDebugLogw(baseSearchRefreshFragment.getClass().getSimpleName());
                    baseSearchRefreshFragment.onSearchCancel();
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

    @DrawableRes(R.drawable.ic_arrow_back_white)
    Drawable back;
    @DrawableRes(R.drawable.ic_clear_white)
    Drawable clear;
    @ColorRes(R.color.pressed)
    int pressed;

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()) {
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }

    }

    @OnActivityResult(2048)
    void reSetData(){
        getData();
    }
}
