package cn.edu.jumy.oa.widget.customview;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.lang.ref.WeakReference;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.UI.SignUpAddActivity_;
import cn.edu.jumy.oa.UI.SignUpDetailsActivity;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.bean.Sign;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/5 17:28.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.item_tablerow)
public class ItemTableRow extends TableRow implements View.OnClickListener {
    @ViewById
    TextView text1;//name
    @ViewById
    TextView text2;//职务
    @ViewById
    TextView text3;//审批状态
    @ViewById
    TextView text4;//审批反馈
    @ViewById
    TextView text5;//操作-重新报名
    @ViewById
    TextView text6;//操作-修改
    @ViewById
    TextView text7;//操作-删除

    private AuditUser user;
    private WeakReference<BaseActivity> mActivityRef;


    public ItemTableRow(Context context) {
        super(context);
    }

    public ItemTableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRow(Context context, AuditUser node, BaseActivity activity) {
        super(context);
        if (node == null) {
            node = new AuditUser("", "");
        }
        this.mActivityRef = new WeakReference<BaseActivity>(activity);
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
        text2.setText(TextUtils.isEmpty(user.post) ? "" : user.post);
        switch (user.passStatus){
            case 0:{//审批通过
                text3.setText("审批通过");
                text5.setVisibility(GONE);
                text6.setVisibility(GONE);
                text7.setVisibility(GONE);
                break;
            }
            case 1:{//审批未通过
                text3.setText("审批未通过");
                text6.setVisibility(GONE);
                text7.setVisibility(GONE);
                break;
            }
            case 2:{//正在审批
                text3.setText("正在审批");
                text5.setVisibility(GONE);
                text6.setVisibility(GONE);
                text7.setVisibility(GONE);
                break;
            }
            case 3:{//未审批
                text3.setText("未审批");
                text5.setVisibility(GONE);
                break;
            }
            default:break;
        }
        text4.setText(TextUtils.isEmpty(user.passRemark) ? "" : user.passRemark);
        setButtonClickListener();
    }

    private void setButtonClickListener() {
        text5.setOnClickListener(this);
        text6.setOnClickListener(this);
        text7.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String title = "";
        switch (v.getId()){
            case R.id.text5:{//重新报名
                title = "重新报名";
                skip2edit(title);
                this.user.passStatus = 3;
                break;
            }
            case R.id.text6:{//修改
                title = "修改";
                skip2edit(title);
                break;
            }
            case R.id.text7:{//删除
                OAService.delMEntry(user.id, new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int ID) {
                        mActivityRef.get().showToast("删除失败,请重新尝试");
                    }

                    @Override
                    public void onResponse(String response, int ID) {
                        Intent data = new Intent(SignUpDetailsActivity.DELETE);
                        mActivityRef.get().getApplicationContext().sendBroadcast(data);
                    }
                });
                break;
            }
            default:break;
        }
    }

    private void skip2edit(String title) {
        Sign node = new Sign(user);
        node.pid = ((SignUpDetailsActivity)mActivityRef.get()).tid;
        SignUpAddActivity_.intent(mActivityRef.get()).extra("old",node).extra("fromItem",true).extra("title",title).startForResult(2048);
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
