package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.widget.customview.ItemTableRow;
import cn.edu.jumy.oa.widget.customview.ItemTableRow_;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;

/**
 * Created by Jumy on 16/7/5 16:46.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up_details)
public class SignUpDetailsActivity extends BaseActivity {
    @ViewById(R.id.tool_bar)
    Toolbar mToolBar;
    @ViewById(R.id.sign_details_table_signed)
    TableLayout mTableSigned;
    @ViewById(R.id.sign_details_table_unsigned)
    TableLayout mTableUnsigned;

    @AfterViews
    void go() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTableSigned.addView(ItemTableRow_.build(mContext));
//        mTableUnsigned.addView(ItemTableRow_.build(mContext));
//        mTableUnsigned.addView(ItemTableRow_.build(mContext));
//        mTableUnsigned.addView(ItemTableRow_.build(mContext));
    }
}
