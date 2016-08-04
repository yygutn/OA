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
import cn.edu.jumy.oa.UI.SignUpAddActivity_;
import cn.edu.jumy.oa.UI.SignUpDetailsActivity;
import cn.edu.jumy.oa.UI.TaskItem.MeetAuditActivity;
import cn.edu.jumy.oa.bean.AuditUser;
import cn.edu.jumy.oa.bean.Sign;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
import okhttp3.Call;

/**
 * Created by Jumy on 16/7/5 17:28.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.widget_item_table_row_audit)
public class ItemTableRowAudit extends TableRow implements View.OnClickListener {
    @ViewById
    TextView text1;//单位
    @ViewById
    TextView text2;//姓名
    @ViewById
    TextView text3;//审批状态
    @ViewById
    TextView text4;//操作-通过
    @ViewById
    TextView text5;//操作-退回

    private AuditUser user;
    private WeakReference<BaseActivity> mActivityRef;


    public ItemTableRowAudit(Context context) {
        super(context);
    }

    public ItemTableRowAudit(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemTableRowAudit(Context context, AuditUser node, BaseActivity activity) {
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
        text1.setText(TextUtils.isEmpty(user.organame) ? "" : user.organame);
        text2.setText(TextUtils.isEmpty(user.name) ? "" : user.name);
        switch (user.passStatus) {
            case 0: {//审批通过
                text3.setText("审批通过");
                text4.setVisibility(GONE);
                text5.setVisibility(GONE);
                break;
            }
            case 1: {//审批未通过
                text3.setText("审批未通过");
                text4.setVisibility(GONE);
                text5.setVisibility(GONE);
                break;
            }
            case 2: {//正在审批
                text3.setText("正在审批");
                break;
            }
            case 3: {//未审批
                text3.setText("未审批");
                break;
            }
            default:
                break;
        }
        setButtonClickListener();
    }

    private void setButtonClickListener() {
        text4.setOnClickListener(this);
        text5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text4: {//操作-通过
                skip2edit(true);
                break;
            }
            case R.id.text5: {//操作-退回
                skip2edit(false);
                break;
            }
            default:
                break;
        }
    }

    private void skip2edit(final boolean pass) {
        final EditText editText = new EditText(mActivityRef.get());
        final String title = pass ? "通过意见":"退回意见";
        AlertDialog alertDialog = new AlertDialog.Builder(mActivityRef.get())
                .setTitle(title)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String passRemark = editText.getText().toString();
                        OAService.meetUserPass(user.id, String.valueOf(pass), passRemark, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                Toast.makeText(mActivityRef.get(), "当前网络不可用,审核报名失败", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                                if (baseResponse.code == 0) {
                                    //审核通过/退回
                                    Intent data = new Intent(MeetAuditActivity.DELETE);
                                    mActivityRef.get().sendBroadcast(data);
                                }
                            }
                        });

                    }
                })
                .setNegativeButton("取消", null)
                .setView(editText)
                .create();
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
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
