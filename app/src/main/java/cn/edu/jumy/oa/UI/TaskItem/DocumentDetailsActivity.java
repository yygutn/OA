package cn.edu.jumy.oa.UI.TaskItem;

import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/20 16:53.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_details)
public class DocumentDetailsActivity extends BaseActivity{
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.document_details_level)
    protected AppCompatTextView mDocumentDetailsLevel;
    @ViewById(R.id.document_details_title)
    protected AppCompatTextView mDocumentDetailsTitle;
    @ViewById(R.id.document_details_user)
    protected AppCompatTextView mDocumentDetailsUser;
    @ViewById(R.id.document_details_number)
    protected AppCompatTextView mDocumentDetailsNumber;
    @ViewById(R.id.document_details_subtitle)
    protected AppCompatTextView mDocumentDetailsSubtitle;
    @ViewById(R.id.document_details_content_head)
    protected AppCompatTextView mDocumentDetailsContentHead;
    @ViewById(R.id.document_details_content)
    protected AppCompatTextView mDocumentDetailsContent;
    @ViewById(R.id.document_details_time)
    protected AppCompatTextView mDocumentDetailsTime;
    @ViewById(R.id.document_details_other)
    protected AppCompatTextView mDocumentDetailsOther;

    @ViewById(R.id.document_details_download)
    protected AppCompatTextView mDocumentDetailsDownload;

    protected Node mNode;

    @AfterViews
    void start(){
        mNode = (Node) getIntent().getSerializableExtra("details");
        if (mNode == null){
            backToPreActivity();
        }
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        setUpViews();
    }

    private void setUpViews() {
        if (TextUtils.isEmpty(mNode.getTitle())){
            return;
        }
        mDocumentDetailsLevel.setText(mNode.getLevel());
        mDocumentDetailsTitle.setText(mNode.getTitle());
        mDocumentDetailsUser.setText(mNode.getIssuer());
        mDocumentDetailsNumber.setText(mNode.getDocumentNumber());
        mDocumentDetailsSubtitle.setText(mNode.getSubtitle());
        mDocumentDetailsContentHead.setText(mNode.getContentHead());
        mDocumentDetailsTime.setText(mNode.getDispatchTime());
        mDocumentDetailsOther.setText(mNode.getOther());
        String message = "";
        switch (mNode.getType()){
            case 0:{//会议，特殊处理
                message = mNode.getContent();
                mDocumentDetailsContent.setText(mNode.getContent());
                mDocumentDetailsContent.setGravity(Gravity.CENTER_VERTICAL);
                mDocumentDetailsContent.setPadding(45,0,0,0);
                break;
            }
            case 1:
            case 2:{
                message = "\t\t\t\t\t" + mNode.getContent();
                mDocumentDetailsContent.setText(mNode.getContent());
            }
            default:break;
        }

    }

    @Click(R.id.document_details_download)
    void click(){
        // TODO: 16/6/20 下载文件
    }
}
