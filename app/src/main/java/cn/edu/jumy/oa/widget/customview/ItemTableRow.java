package cn.edu.jumy.oa.widget.customview;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.bean.Node;
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
    TextView text2;//SEX
    @ViewById
    TextView text3;//职业
    @ViewById
    TextView text4;//tel


    public ItemTableRow(Context context) {
        super(context);
    }

    public ItemTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRow(Context context, AuditUser node) {
        super(context);
        text1.setText(node.name);
        text2.setText(node.sex);
        text3.setText(node.post);
        text4.setText(node.phone);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }


    private void init() {
        setDividerDrawable(getResources().getDrawable(R.drawable.shape_divider_white));
        setShowDividers(SHOW_DIVIDER_MIDDLE);

        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                int oldWidth = oldRight - oldLeft;
                int oldHeight = oldBottom - oldTop;
                int newWidth = right - left;
                int newHeight = bottom - top;
                boolean sizeChanged = (newWidth != oldWidth) || (newHeight != oldHeight);
                if (v.getLayoutParams() != null && sizeChanged) {
                    int m = MeasureUtil.dp2px(MyApplication.getContext(), 1.0f);
                    MarginLayoutParams marginLayoutParams = new MarginLayoutParams(getLayoutParams());
                    marginLayoutParams.setMargins(0, 0, 0, m);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(marginLayoutParams);
                    layoutParams.gravity = Gravity.TOP | Gravity.LEFT;
                    setLayoutParams(layoutParams);
                    removeOnLayoutChangeListener(this);
                }
            }
        });
    }
}
