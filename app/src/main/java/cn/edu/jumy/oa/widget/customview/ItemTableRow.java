package cn.edu.jumy.oa.widget.customview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;

/**
 * Created by Jumy on 16/7/5 17:28.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.item_tablerow)
public class ItemTableRow extends TableRow {
    @ViewById
    TextView text1;//name
    @ViewById
    TextView text2;//职务
    @ViewById
    TextView text3;//SEX
    @ViewById
    TextView text4;//tel

    private AuditUser user;


    public ItemTableRow(Context context) {
        super(context);
    }

    public ItemTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRow(Context context, AuditUser node) {
        super(context);
        if (node == null) {
            node = new AuditUser("", "");
        }
        this.user = node;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @AfterViews
    void bindData() {
        text1.setText(TextUtils.isEmpty(user.name) ? "" : user.name);
        text3.setText(TextUtils.isEmpty(user.sex) ? "男" : user.sex);
        text2.setText(TextUtils.isEmpty(user.post) ? "" : user.post);
        text4.setText(TextUtils.isEmpty(user.phone) ? "" : user.phone);
    }


    private void init() {
        setDividerDrawable(getResources().getDrawable(R.drawable.shape_divider_white));
        setShowDividers(SHOW_DIVIDER_MIDDLE);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View itemView, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int oldWidth = oldRight - oldLeft;
                int oldHeight = oldBottom - oldTop;
                int newWidth = right - left;
                int newHeight = bottom - top;
                boolean sizeChanged = (newWidth != oldWidth) || (newHeight != oldHeight);
                if (itemView.getLayoutParams() != null && sizeChanged) {
                    int m = MeasureUtil.dp2px(MyApplication.getContext(), 1.0f);
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) itemView.getLayoutParams();
                    layoutParams.setMargins(0, 0, 0, m);
                    itemView.setLayoutParams(layoutParams);
                }
            }
        });
    }
}
