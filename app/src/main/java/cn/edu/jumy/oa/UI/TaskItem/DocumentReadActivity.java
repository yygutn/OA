package cn.edu.jumy.oa.UI.TaskItem;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
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

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.fragment.DocumentAllFragment_;
import cn.edu.jumy.oa.fragment.DocumentFragment;
import cn.edu.jumy.oa.fragment.DocumentReadFragment_;
import cn.edu.jumy.oa.fragment.DocumentUnreadFragment_;

/**
 * Created by Jumy on 16/6/22 10:02.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_read)
public class DocumentReadActivity extends BaseActivity {
    private static final String TAG = DocumentReadActivity.class.getSimpleName();
    private LayoutInflater layoutInflater;
    private final Class fragmentArray[] = {DocumentUnreadFragment_.class, DocumentReadFragment_.class, DocumentAllFragment_.class};
    private int mTitleArray[] = {R.string.document_unread,R.string.document_read,R.string.document_all};
    public String mTextViewArray[] = {"unread","read","all"};

    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;

    protected Indicator indicator;
    @ViewById(R.id.document_viewPager)
    protected ViewPager viewPager;

    @ViewById(R.id.search_view)
    MaterialSearchView mSearchView;

    private IndicatorViewPager indicatorViewPager;

    MyAdapter adapter;



    @AfterViews
    void start(){
        mToolBar.setTitle("公文阅读");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            indicator = (Indicator) findViewById(R.id.document_indicator);
            float unSelectSize = 16;
            float selectSize = unSelectSize * 1.2f;

            int selectColor = getResources().getColor(R.color.pressed);
            int unSelectColor = getResources().getColor(R.color.normal);
            indicator.setScrollBar(new ColorBar(getApplicationContext(), getResources().getColor(R.color.pressed), 5));
            indicator.setOnTransitionListener(new OnTransitionTextListener().setSize(selectSize, unSelectSize).setColor(selectColor,unSelectColor));
            viewPager.setOffscreenPageLimit(3);
            adapter = new MyAdapter(getSupportFragmentManager());
            indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
            indicatorViewPager.setAdapter(adapter);
//            indicatorViewPager.setCurrentItem(0, false);

            initSearchView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = { "已签收", "未签收", "全部"};
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
                    convertView = (TextView)inflater.inflate(R.layout.document_tab, container, false);
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
            switch (position){
                case 0:{
                    return new DocumentUnreadFragment_();
                }
                case 1:{
                    return new DocumentReadFragment_();
                }
                case 2:{
                    return new DocumentAllFragment_();
                }
                default:return null;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.document,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }

    private void initSearchView() {
        mSearchView.setVoiceSearch(false);
        final DocumentFragment documentFragment = (DocumentFragment) adapter.getCurrentFragment();
        mSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                documentFragment.onTextChanged(query);
                return false;
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
                documentFragment.onSearchCancel();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mSearchView.isSearchOpen()){
            mSearchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }
}
