package cn.edu.jumy.oa.widget.customview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
import cn.edu.jumy.oa.UI.TaskItem.MeetAuditActivity;
import cn.edu.jumy.oa.UI.TaskItem.SignDetailsActivity;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/5 17:28.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.widget_item_table_row_sign_details)
public class ItemTableRowSignDetails extends TableRow{
    @ViewById
    TextView text1;//单位
    @ViewById
    TextView text2;//姓名
    @ViewById
    TextView text3;//操作-催收

    private AuditUser user;
    private WeakReference<BaseActivity> mActivityRef;


    public ItemTableRowSignDetails(Context context) {
        super(context);
    }

    public ItemTableRowSignDetails(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRowSignDetails(Context context, AuditUser node, BaseActivity activity) {
        super(context);
        if (node == null) {
            node = new AuditUser("", "");
        }
        this.user = node;
        this.mActivityRef = new WeakReference<BaseActivity>(activity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @AfterViews
    void bindData() {
        text1.setText(TextUtils.isEmpty(user.organame) ? "" : user.organame);
        text2.setText(TextUtils.isEmpty(user.uname) ? "" : user.uname);
        if (user.signStatus == 0){
            text3.setText("");
        } else {
            text3.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    skip2remote();
                }
            });
        }
    }

    private void skip2remote() {
        if (SignDetailsActivity.flag){
            //公文
            OAService.docUrge(user.oid, user.did, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(mActivityRef.get(),"网络异常,催收失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                    if (baseResponse.code == 0){
                        text3.setText("已催收");
                        text3.setClickable(false);
                        mActivityRef.get().sendBroadcast(new Intent(SignDetailsActivity.UPDATE));
                    } else {
                        Toast.makeText(mActivityRef.get(),"催收失败,"+baseResponse.msg,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            //会议
            OAService.meetUrge(user.oid, user.did, new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toast.makeText(mActivityRef.get(),"网络异常,催收失败",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(String response, int id) {
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(response,BaseResponse.class);
                    if (baseResponse.code == 0){
                        text3.setText("已催收");
                        text3.setClickable(false);
                    } else {
                        Toast.makeText(mActivityRef.get(),"催收失败,"+baseResponse.msg,Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
