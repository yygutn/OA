package cn.edu.jumy.oa.UI.TaskItem;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AttachResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.UI.SignUpActivity_;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.bean.Annex;
import cn.edu.jumy.oa.bean.Attachment;
import cn.edu.jumy.oa.bean.Node;
import okhttp3.Call;
import okhttp3.Response;

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

    @Extra("details")
    protected Node mNode = new Node();


    ClipboardManager clipboardManager;

    @AfterViews
    void start() {
        clipboardManager = (ClipboardManager) getSystemService(mContext.CLIPBOARD_SERVICE);
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            setUpViews();
        } catch (Exception e) {
            showDebugException(e);
        }

        if (mNode.getType() == 1) {
            DocSignBackground();
            setResult(1025);
        } else if (mNode.getType() == 0) {
            MeetSign();
        }
    }

    private void MeetSign() {
        if (TextUtils.isEmpty(mNode.tid) || mNode.signStatus == 0) {
            return;
        }
        OAService.meetSign(mNode.tid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showToast("当前网络不可用,签收会议失败");
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
                    showToast("签收会议成功");
                } else if (baseResponse.code == 1) {
                    showToast("签收会议失败");
                }
            }
        });
    }

    private void DocSignBackground() {
        if (TextUtils.isEmpty(mNode.tid) || mNode.signStatus == 0) {
            return;
        }
        OAService.docSign(mNode.tid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showDebugException(e);
                showToast("当前网络不可用,签收公文失败");
            }

            @Override
            public void onResponse(String response, int id) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
                    showToast("签收公文成功");
                } else {
                    if (!TextUtils.isEmpty(baseResponse.msg)) {
                        showToast(baseResponse.msg);
                    } else {
                        showToast("当前网络不可用,签收公文失败");
                    }
                }
            }
        });
    }


    private void setUpViews() {
        //标题处理
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
        //文字信息处理
        // TODO: 16/7/12 根据LEVEL的等级ID设置不同的等级显示
        String level = "";
        switch (mNode.getLevel()) {
            case 0: {
                level = "特提";
                break;
            }
            case 1: {
                level = "特急";
                break;
            }
            case 2: {
                level = "加急";
                break;
            }
            case 3: {
                level = "平急";
                break;
            }
            case 4: {
                level = "特提";
                break;
            }
            default:
                break;
        }
        try {
            mDocumentDetailsLevel.setText(level);
            mDocumentDetailsTitle.setText(mNode.getTitle());
            mDocumentDetailsNumber.setText(mNode.getDocumentNumber());
            mDocumentDetailsContentHead.setText(mNode.getContentHead());
            mDocumentDetailsTime.setText(mNode.getDispatchTime());
            mDocumentDetailsOther.setText(mNode.getOther());

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
            showDebugException(e);
        }

    }

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
                doFileDownload();
                break;
            }
            case R.id.document_details_sign_up: {// TODO: 16/7/5 报名
                SignUpActivity_.intent(mContext).extra("pid",mNode.getID()).start();
                break;
            }
            default:
                break;
        }
    }

    AlertDialog alertDialog;

    private void doFileDownload() {
        Map<String, String> params = new HashMap<>();
        params.put("pid", TextUtils.isEmpty(mNode.getID()) ? "" : mNode.getID());
        OAService.getAttachmentList(params, new AttachListCallBack() {
            @Override
            public void onResponse(AttachResponse response, int id) {
                final ArrayList<Attachment> list = response.data;
                if (list == null || list.size() == 0 || response.code == 1) {
                    showToast("无附件");
                    return;
                }
                ArrayList<String> item_list = new ArrayList<String>();
                for (Attachment attachment : list) {
                    item_list.add(attachment.getFileName());
                }
                final String[] items = item_list.toArray(new String[item_list.size()]);
                alertDialog = new AlertDialog.Builder(mContext)
                        .setTitle("附件下载 " +
                                "\n(点击下载,长按可复制文件名)")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                try {
                                    String id = list.get(which).getId();
                                    id = TextUtils.isEmpty(id) ? "" : id;
                                    String filepath = mContext.getExternalCacheDir().getAbsolutePath();
                                    String filename = list.get(which).getFileName();
                                    File file = new File(filepath, filename);
                                    if (file.exists()) {
                                        CallOtherOpenFile.openFile(mContext, file);
                                        file.deleteOnExit();
                                    } else {
                                        OAService.downloadAttachment(id, new FileCallBack(filepath, filename) {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                showToast("下载附件失败");
                                            }

                                            @Override
                                            public void onResponse(File file, int id) {
                                                CallOtherOpenFile.openFile(mContext, file);
                                                file.deleteOnExit();
                                            }

                                            @Override
                                            public void syncSaveToSQL(File file) {
                                                Attachment attachment = list.get(which);
                                                ArrayList<Annex> annexArrayList = (ArrayList<Annex>) DataSupport.where("IDS = ?", attachment.getId()).find(Annex.class);
                                                Annex annex;
                                                if (annexArrayList != null && annexArrayList.size() > 0) {
                                                    annex = annexArrayList.get(0);
                                                } else {
                                                    annex = null;
                                                }
                                                if (annex == null || TextUtils.isEmpty(annex.getID())) {
                                                    annex = new Annex(attachment, file);
                                                } else {
                                                    annex.setAnnex(attachment, file);
                                                }
                                                if (annex.save()){
                                                    showDebugLoge(annex.getFileName()+"：保存成功");
                                                }

                                            }
                                        });
                                        alertDialog.cancel();
                                    }
                                } catch (Exception e) {
                                    showDebugException(e);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                alertDialog.getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                        clipboardManager.setText(items[position]);
                        showToast("标题已复制到剪贴板");
                        return true;
                    }
                });
                alertDialog.show();
                alertDialog.setCanceledOnTouchOutside(true);
            }
        });

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

    abstract class AttachListCallBack extends Callback<AttachResponse> {
        @Override
        public AttachResponse parseNetworkResponse(Response response, int id) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, AttachResponse.class);
            } else if (baseResponse.code == 1) {
                return new AttachResponse(baseResponse.msg, baseResponse.code, null);
            }
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            showToast("获取附件列表失败");
        }
    }

}
