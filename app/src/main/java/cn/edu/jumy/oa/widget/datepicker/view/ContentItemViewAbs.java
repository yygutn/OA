package cn.edu.jumy.oa.widget.datepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.jumy.oa.R;

public class ContentItemViewAbs extends LinearLayout {

    public ContentItemViewAbs(Context context) {
        this(context, null);
    }

    public ContentItemViewAbs(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentItemViewAbs(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ContentItemViewAbs(Context context, String title, String subtitle, String content) {
        super(context, null, 0);
        View view = LayoutInflater.from(context).inflate(R.layout.content_list_item_abs,null);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setViews(view,title,subtitle,content);
        addView(view);
    }

    private void setViews(View view, String title, String subTitle, String content) {
        ((TextView)view.findViewById(R.id.title)).setText(title);
        ((TextView)view.findViewById(R.id.subtitle)).setText(subTitle);
        ((TextView)view.findViewById(R.id.content)).setText(content);
    }
}
