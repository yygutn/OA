package cn.edu.jumy.oa.widget.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import cn.edu.jumy.oa.R;

/**
 * 设置等页面条状控制或显示信息的控件
 */
public class LineControllerView extends LinearLayout {

    private String name;
    private boolean isBottom;
    private String content;
    private boolean canNav;
    private boolean isSwitch;

    public LineControllerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.widget_line_controller, this);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LineControllerView, 0, 0);
        try {
            name = ta.getString(R.styleable.LineControllerView_name);
            content = ta.getString(R.styleable.LineControllerView_content);
            isBottom = ta.getBoolean(R.styleable.LineControllerView_isBottom, false);
            canNav = ta.getBoolean(R.styleable.LineControllerView_canNav,false);
            isSwitch = ta.getBoolean(R.styleable.LineControllerView_isSwitch,false);
            setUpView();
        } finally {
            ta.recycle();
        }
    }


    private void setUpView(){
        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(name);
        TextView tvContent = (TextView) findViewById(R.id.content);
        tvContent.setText(getShortenStr(content));
        View bottomLine = findViewById(R.id.bottomLine);
        bottomLine.setVisibility(isBottom ? VISIBLE : GONE);
        ImageView navArrow = (ImageView) findViewById(R.id.rightArrow);
        navArrow.setVisibility(canNav ? VISIBLE : GONE);
        LinearLayout contentPanel = (LinearLayout) findViewById(R.id.contentText);
        contentPanel.setVisibility(isSwitch ? GONE : VISIBLE);

    }


    /**
     * 设置文字内容
     *
     * @param content 内容
     */
    public void setContent(String content){
        this.content = content;
        TextView tvContent = (TextView) findViewById(R.id.content);
        tvContent.setText(getShortenStr(content));
    }


    /**
     * 获取内容
     *
     */
    public String getContent(){
        TextView tvContent = (TextView) findViewById(R.id.content);
        return tvContent.getText().toString();
    }


    /**
     * 设置是否可以跳转
     *
     * @param canNav 是否可以跳转
     */
    public void setCanNav(boolean canNav){
        this.canNav = canNav;
        ImageView navArrow = (ImageView) findViewById(R.id.rightArrow);
        navArrow.setVisibility(canNav ? VISIBLE : GONE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        TextView textView = (TextView) findViewById(R.id.name);
        textView.setText(name);
    }

    private String getShortenStr(String str){
        if (str == null) return "";
        if (str.length()>23){
            return str.substring(0,23)+"...";
        }
        return str;
    }
}
