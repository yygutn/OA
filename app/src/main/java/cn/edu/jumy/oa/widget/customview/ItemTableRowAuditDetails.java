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

import java.lang.ref.WeakReference;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Relay;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;

/**
 * Created by Jumy on 16/7/5 17:28.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.widget_item_table_row_audit_details)
public class ItemTableRowAuditDetails extends TableRow {
    @ViewById
    TextView text1;//单位
    @ViewById
    TextView text2;//转发审批意见

    private Relay user;
    private WeakReference<BaseActivity> mActivityRef;


    public ItemTableRowAuditDetails(Context context) {
        super(context);
    }

    public ItemTableRowAuditDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRowAuditDetails(Context context, Relay node, BaseActivity activity) {
        super(context);
        if (node == null) {
            node = new Relay();
        }
        this.mActivityRef = new WeakReference<>(activity);
        this.user = node;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @AfterViews
    void bindData() {
        text1.setText(TextUtils.isEmpty(user.uname) ? "" : user.uname);
        text2.setText(TextUtils.isEmpty(user.passRemark) ? "" : user.passRemark);
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
