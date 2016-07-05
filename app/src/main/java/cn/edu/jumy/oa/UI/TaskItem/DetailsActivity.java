package cn.edu.jumy.oa.UI.TaskItem;

import android.annotation.TargetApi;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.SignUpActivity_;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/20 16:53.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_details)
public class DetailsActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.document_details_level)
    protected AppCompatTextView mDocumentDetailsLevel;
    @ViewById(R.id.document_details_title)
    protected AppCompatTextView mDocumentDetailsTitle;
    @ViewById(R.id.document_details_number)
    protected AppCompatTextView mDocumentDetailsNumber;
    @ViewById(R.id.document_details_content_head)
    protected AppCompatTextView mDocumentDetailsContentHead;
    @ViewById(R.id.document_details_content)
    protected AppCompatTextView mDocumentDetailsContent;
    @ViewById(R.id.document_details_content_meet)
    protected AppCompatTextView mDocumentDetailsContent_meet;
    @ViewById(R.id.document_details_time)
    protected AppCompatTextView mDocumentDetailsTime;
    @ViewById(R.id.document_details_other)
    protected AppCompatTextView mDocumentDetailsOther;
    @ViewById(R.id.document_details_download)
    protected AppCompatTextView mDocumentDetailsDownload;
    @ViewById(R.id.document_details_sign_up)
    protected AppCompatTextView mDocumentDetailsSignUp;

    protected Node mNode;

    @AfterViews
    void start() {
        mNode = (Node) getIntent().getSerializableExtra("details");
        if (mNode == null) {
            mNode = new Node();
        }
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            setUpViews();
        } catch (Exception e) {
            Logger.e("Exception: ", e);
        }
    }

    private void setUpViews() {
        switch (mNode.getType()) {
            case 0: {
                mTitleBar.setTitle("会议详情");
                mDocumentDetailsLevel.setVisibility(View.GONE);
                mDocumentDetailsSignUp.setVisibility(View.VISIBLE);
                break;
            }
            case 1: {
                mTitleBar.setTitle("公文详情");
                break;
            }
            case 2: {
                mTitleBar.setTitle("公告详情");
                break;
            }
            default:
                break;
        }
        if (false) {
            try {
                mDocumentDetailsLevel.setText(mNode.getLevel());
                mDocumentDetailsTitle.setText(mNode.getTitle());
                mDocumentDetailsNumber.setText(mNode.getDocumentNumber());
                mDocumentDetailsContentHead.setText(mNode.getContentHead());
                mDocumentDetailsTime.setText(mNode.getDispatchTime());
                mDocumentDetailsOther.setText(mNode.getOther());
            } catch (Exception e) {
                Logger.e("setUpViews: ", e);
            }
        }

        try {
            switch (mNode.getType()) {
                case 0: {//会议，特殊处理
                    mDocumentDetailsContent.setVisibility(View.GONE);
                    mDocumentDetailsContent_meet.setVisibility(View.VISIBLE);
                    mDocumentDetailsContent_meet.setText(mNode.getContent());
                    mDocumentDetailsContent_meet.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());
                    break;
                }
                case 1:
                case 2: {
                    mDocumentDetailsContent.setVisibility(View.VISIBLE);
                    mDocumentDetailsContent_meet.setVisibility(View.GONE);
                    mDocumentDetailsContent.setText(mNode.getContent());
                }
                default:
                    break;
            }
        } catch (Exception e) {
            Logger.e("Exception :", e);
        }

    }

    @StringRes(R.string.content2)
    String strRes;

    private String autoSplitText(final TextView tv, final String indent) {
        final String rawText = tv.getText().toString(); //原始文本
        final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
        final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度
        //将原始文本按行拆分
        String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
        StringBuilder sbNewText = new StringBuilder();

        for (String rawTextLine : rawTextLines) {
            //获取当前子串的悬挂头
            String str = "";
            int position = rawTextLine.indexOf(indent) + indent.length();
            if (position == 0) {
                position = rawTextLine.indexOf("：") + 1;
            }
            str = rawTextLine.substring(0, position);
//            showDebugLogw("subStr:  " + str);
            //将缩进处理成空格
            String indentSpace = "";
            float indentWidth = 0;
            if (!TextUtils.isEmpty(str)) {
                float rawIndentWidth = tvPaint.measureText(str);
                if (rawIndentWidth < tvWidth) {
                    while ((indentWidth = tvPaint.measureText(indentSpace)) < rawIndentWidth) {
                        indentSpace += " ";
                    }
                }
            }
            if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                //如果整行宽度在控件可用宽度之内，就不处理了
                sbNewText.append(rawTextLine);
            } else {
                //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                float lineWidth = 0;
                for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                    char ch = rawTextLine.charAt(cnt);
                    //从手动换行的第二行开始，加上悬挂缩进
                    if (lineWidth < 0.1f && cnt != 0) {
                        sbNewText.append(indentSpace);
                        lineWidth += indentWidth;
                    }
                    lineWidth += tvPaint.measureText(String.valueOf(ch));
                    if (lineWidth <= tvWidth) {
                        sbNewText.append(ch);
                    } else {
                        sbNewText.append("\n");
                        lineWidth = 0;
                        --cnt;
                    }
                }
            }
            sbNewText.append("\n");
        }

        //把结尾多余的\n去掉
        if (!rawText.endsWith("\n")) {
            sbNewText.deleteCharAt(sbNewText.length() - 1);
        }

        return sbNewText.toString();
    }

    @Click({R.id.document_details_download, R.id.document_details_sign_up})
    void click(View view) {
        switch (view.getId()) {
            case R.id.document_details_download: {// TODO: 16/7/5 下载附件
                break;
            }
            case R.id.document_details_sign_up: {// TODO: 16/7/5 报名
                SignUpActivity_.intent(mContext).start();
                break;
            }
            default:
                break;
        }
    }

    private class OnTvGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onGlobalLayout() {
            mDocumentDetailsContent_meet.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            final String newText = autoSplitText(mDocumentDetailsContent_meet, ":");
            if (!TextUtils.isEmpty(newText)) {
                mDocumentDetailsContent_meet.setText(newText);
            }
        }
    }
}
