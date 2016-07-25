package cn.edu.jumy.oa.UI.TaskItem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
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
import org.androidannotations.annotations.PageScrollStateChanged;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.androidannotations.annotations.res.DrawableRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.BroadCastReceiver.DocumentBroadcastReceiver;
import cn.edu.jumy.oa.BroadCastReceiver.MeetBroadcastReceiver;
import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.fragment.AlreadyApprovalFragment;
import cn.edu.jumy.oa.fragment.AlreadyApprovalFragment_;
import cn.edu.jumy.oa.fragment.BaseSearchRefreshFragment;
import cn.edu.jumy.oa.fragment.DocumentAllFragment_;
import cn.edu.jumy.oa.fragment.DocumentReadFragment_;
import cn.edu.jumy.oa.fragment.DocumentUnreadFragment_;
import cn.edu.jumy.oa.fragment.WaitingForApprovalFragment;
import cn.edu.jumy.oa.fragment.WaitingForApprovalFragment_;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/24  下午5:41
 */
@EActivity(R.layout.activity_shen_pi)
public class ApprovalSpActivity extends BaseActivity {

    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;

    protected Indicator indicator;
    @ViewById(R.id.document_viewPager)
    protected ViewPager viewPager;

    @ViewById(R.id.search_view)
    MaterialSearchView mSearchView;

    private IndicatorViewPager indicatorViewPager;

    MyAdapter adapter;

    @AfterExtras
    void extras(){
        //getData from internet

        OAService.meetReceive(getParams(), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response.code == 1 || response.data == null) {
                    showToast("获取会议列表失败");
                    return;
                }
                ArrayList<Meet> list = response.data.pageObject;
                //未审批-list
                ArrayList<Meet> list0 = new ArrayList<>();
                //已审批-list
                ArrayList<Meet> list1 = new ArrayList<>();

                for (Meet meet : list){
                    if (meet.passStatus == 3){
                        list0.add(meet);
                    } else if (meet.passStatus != 2){
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
            }
        });
    }

    private Map<String,String> getParams() {
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
        return params;
    }


    @AfterViews
    void start() {
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
//            indicatorViewPager.setCurrentItem(0, false);

        initSearchView();
        setSupportActionBar(mToolBar);

        mToolBar.setTitle("由我审批");
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
        private String[] tabNames = {"待审批", "已审批"};
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
                    return new WaitingForApprovalFragment_();
                }
                case 1: {
                    return new AlreadyApprovalFragment_();
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
}
