package cn.edu.jumy.oa.widget.datepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
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
        View view = inflate(context, R.layout.content_list_item_abs, null);
        addView(view);
    }

    public ContentItemViewAbs(Context context, int defStyleAttr,String title, String subtitle, String content){
        super(context,null,defStyleAttr);
        View cardView =  inflate(context,R.layout.content_list_item_abs,null);
        ((TextView)cardView.findViewById(R.id.title)).setText(title);
        ((TextView)cardView.findViewById(R.id.subtitle)).setText(subtitle);
        ((TextView)cardView.findViewById(R.id.content)).setText(content);
        addView(cardView);
    }
}
