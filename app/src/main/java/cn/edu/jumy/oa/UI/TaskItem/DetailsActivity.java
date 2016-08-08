package cn.edu.jumy.oa.UI.TaskItem;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AttachResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.OrgRelayResponse;
import cn.edu.jumy.oa.UI.OrgRelaySelectActivity_;
import cn.edu.jumy.oa.UI.SignUpMultiAbleActivity_;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.bean.Annex;
import cn.edu.jumy.oa.bean.Attachment;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Notify;
import cn.edu.jumy.oa.bean.OrgRelay;
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
    @ViewById(R.id.document_details_cui)
    protected AppCompatTextView mCuiS;

    @Extra("details")
    protected Node mNode = new Node();
    @Extra("from_sent_meet")
    boolean fromSentMeet = false;
    @Extra("from_SP")
    boolean fromSP = false;

    ClipboardManager clipboardManager;

    @AfterViews
    void start() {
        setSupportActionBar(mTitleBar);
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

        if (mNode.type == 1 && !fromSP && !fromSentMeet) {
            DocSignBackground();
            setResult(1025);
        } else if (mNode.type == 0 && !fromSP && !fromSentMeet) {
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
                    if (!TextUtils.isEmpty(baseResponse.msg)) {
                        showToast(baseResponse.msg);
                    }
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
        //催收按钮
        mCuiS.setVisibility(fromSentMeet ? View.VISIBLE : View.GONE);
        //标题处理
        switch (mNode.type) {
            case 0: {
                mTitleBar.setTitle("会议详情");
//                mDocumentDetailsLevel.setVisibility(View.GONE);
                mDocumentDetailsSignUp.setVisibility(fromSentMeet || fromSP ? View.GONE : View.VISIBLE);
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
        switch (mNode.level) {
            case 0: {
                mDocumentDetailsLevel.setVisibility(View.GONE);
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
            if (!TextUtils.isEmpty(mNode.title)) {
                mDocumentDetailsTitle.setText(mNode.title);
            } else {
                mDocumentDetailsTitle.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(mNode.documentNumber)) {
                mDocumentDetailsNumber.setText(mNode.documentNumber);
            } else {
                mDocumentDetailsNumber.setVisibility(View.GONE);
            }
            if (TextUtils.isEmpty(mNode.dispatchUnit)) {
                mDocumentDetailsContentHead.setVisibility(View.GONE);
            } else {
                mDocumentDetailsContentHead.setText(mNode.dispatchUnit + ":");
            }

            mDocumentDetailsContent.setVisibility(View.GONE);
            mDocumentDetailsContent_meet.setVisibility(View.VISIBLE);
            mDocumentDetailsContent_meet.setText(CardGenerator.getContentString(mNode));
            mDocumentDetailsContent_meet.getViewTreeObserver().addOnGlobalLayoutListener(new OnTvGlobalLayoutListener());

            mDocumentDetailsTime.setText(mNode.dispatchTime);
            if (!TextUtils.isEmpty(mNode.other)) {
                mDocumentDetailsOther.setText(mNode.other);
            } else {
                mDocumentDetailsOther.setVisibility(View.GONE);
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

    @Click({R.id.document_details_download, R.id.document_details_sign_up, R.id.document_details_cui})
    void click(View view) {
        switch (view.getId()) {
            case R.id.document_details_download: {// TODO: 16/7/5 下载附件
                doFileDownload();
                break;
            }
            case R.id.document_details_sign_up: {// TODO: 16/7/5 报名
                SignUpMultiAbleActivity_.intent(mContext).extra("tid", mNode.tid).extra("pid", mNode.id).start();
                break;
            }
            case R.id.document_details_cui: {
                SignDetailsActivity_.intent(mContext).extra("id", mNode.id).start();
                break;
            }
            default:
                break;
        }
    }

    AlertDialog alertDialog;

    private void doFileDownload() {
        Map<String, String> params = new HashMap<>();
        params.put("pid", TextUtils.isEmpty(mNode.id) ? "" : mNode.id);
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
                        .setTitle("附件查看 " +
                                "\n(请点击文件名查看附件内容," +
                                "\n查看后保存至文件柜)")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, final int which) {
                                try {
                                    String id = list.get(which).getId();
                                    id = TextUtils.isEmpty(id) ? "" : id;
                                    String filepath = mContext.getExternalCacheDir().getAbsolutePath();
                                    final String filename = list.get(which).getFileName();
                                    //检索数据库，获取文件，否则下载
                                    final String newFileName = getRealFileName(filename);

                                    final Attachment attachment = list.get(which);
                                    ArrayList<Annex> annexArrayList = (ArrayList<Annex>) DataSupport
                                            .where("username = ?", EMClient.getInstance().getCurrentUser()).find(Annex.class);

                                    Annex annex = null;

                                    for (Annex node : annexArrayList) {
                                        if (node.getFileName().equals(newFileName) && node.getPid().equals(attachment.getPid())) {
                                            annex = node;
                                        }
                                    }
                                    final Annex tempNode = annex;
                                    if (annex != null && annex.getFile() != null) {
                                        CallOtherOpenFile.openFile(mContext, annex.getFile());
                                        alertDialog.cancel();
                                    } else {
                                        OAService.downloadAttachment(id, new FileCallBack(filepath, filename) {
                                            @Override
                                            public void onError(Call call, Exception e, int id) {
                                                showToast("下载附件失败");
                                            }

                                            @Override
                                            public void onResponse(File file, int id) {
                                                CallOtherOpenFile.openFile(mContext, file);
                                            }

                                            @Override
                                            public void syncSaveToSQL(File file) {
                                                if (tempNode != null) {
                                                    tempNode.setFileName(newFileName);
                                                    tempNode.setFile(file);
                                                    if (tempNode.save()) {
                                                        showDebugLoge(tempNode.getFileName() + "：保存成功");
                                                    }
                                                    return;
                                                }
                                                Annex annex = new Annex(attachment, file);
                                                annex.setFileName(newFileName);
                                                if (annex.save()) {
                                                    showDebugLoge(annex.getFileName() + "：保存成功");
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

    @NonNull
    private String getRealFileName(String filename) {
        String[] str = filename.split("\\.");
        String tempFileName = str[0];
        int length = str.length;
        for (int i = 1; i < length - 1; i++) {
            tempFileName += "." + str[i];
        }
        tempFileName = tempFileName + "_" + mNode.dispatchTime + "." + str[length - 1];
        return tempFileName;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fromSP && mNode.remark.equals("0")) {
            getMenuInflater().inflate(R.menu.details_shenpi, menu);
        } else if (!fromSP && mNode.type != 2) {
            getMenuInflater().inflate(R.menu.details_share, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @OptionsItem(R.id.action_share)
    void share() {
        OrgRelaySelectActivity_.intent(mContext).extra("type", mNode.type).extra("id", mNode.id).start();
    }

    @OptionsItem(R.id.action_shenpi)
    void shenpi() {
        final EditText editText = new EditText(mContext);
        AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("转发审批意见")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        skip2Sp(editText.getText().toString());
                    }
                })
                .setView(editText)
                .create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    /**
     * @param message 转发审批意见
     */
    private void skip2Sp(String message) {
        Map<String, String> params = new HashMap<>();
        params.put("tid", mNode.tid);
        params.put("passRemark", message);
        params.put("signnum", "");

        OAService.RelayPass(params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int ID) {
                showToast("审批失败");
            }

            @Override
            public void onResponse(String response, int ID) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                showToast(baseResponse.msg);
                if (baseResponse.code == 0) {
                    backToPreActivity();
                }
            }
        });
    }
}
