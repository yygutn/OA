package cn.edu.jumy.oa.UI.TaskItem;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
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

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
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
import cn.edu.jumy.oa.UI.OrgRelaySelectActivity_;
import cn.edu.jumy.oa.UI.SignUpMultiAbleActivity_;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.bean.Annex;
import cn.edu.jumy.oa.bean.Attachment;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
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
    @ViewById(R.id.document_details_content_meet)
    protected AppCompatTextView mDocumentDetailsContent_meet;
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

    //附件列表
    ArrayList<Attachment> mList;

    public static ClipboardManager clipboardManager;

    @AfterExtras
    void getList() {
        Map<String, String> params = new HashMap<>();
        params.put("pid", TextUtils.isEmpty(mNode.id) ? "" : mNode.id);
        OAService.getAttachmentList(params, new AttachListCallBack() {
            @Override
            public void onResponse(AttachResponse response, int id) {
                mList = response.data;
                if (mList == null || mList.size() <= 0) {
                    doSign();
                }
            }
        });
    }

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
        setUpViews();
    }

    private void doSign() {
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
                level = "等级:特急";
                break;
            }
            case 2: {
                level = "等级:加急";
                break;
            }
            case 3: {
                level = "等级:平急";
                break;
            }
            case 4: {
                level = "等级:特提";
                break;
            }
            default:
                break;
        }
        mDocumentDetailsLevel.setText(level);
        if (!TextUtils.isEmpty(mNode.title)) {
            mDocumentDetailsTitle.setText(mNode.title);
        } else {
            mDocumentDetailsTitle.setVisibility(View.GONE);
        }
        mDocumentDetailsContent_meet.setVisibility(View.VISIBLE);
        mDocumentDetailsContent_meet.setText(CardGenerator.getContentString(mNode));

        if (!TextUtils.isEmpty(mNode.contactName) && !TextUtils.isEmpty(mNode.contactPhone)) {
            mDocumentDetailsOther.setText("联系人: " + mNode.contactName + "\n" + "联系电话: " + mNode.contactPhone);
        } else {
            mDocumentDetailsOther.setVisibility(View.GONE);
        }

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
        if (mList == null || mList.size() <= 0) {
            showToast("无附件");
            return;
        }
        ArrayList<String> item_list = new ArrayList<String>();
        for (Attachment attachment : mList) {
            item_list.add(attachment.getFileName());
        }
        final String[] items = item_list.toArray(new String[item_list.size()]);
        SpannableString spannableString = new SpannableString("附件查看\n请点击文件名查看附件内容\n查看后保存至文件柜");
        RelativeSizeSpan sizeSpan = new RelativeSizeSpan(0.9f);
        RelativeSizeSpan sizeSpan0 = new RelativeSizeSpan(1.0f);
        spannableString.setSpan(sizeSpan0,0,3,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan,4,spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        final TextView title = new TextView(mContext);
        title.setTextColor(Color.BLACK);
        title.setText(spannableString);
        int padding = MeasureUtil.dp2px(mContext,16);
        title.setPadding(padding,padding,padding,0);
        alertDialog = new AlertDialog.Builder(mContext)
                .setCustomTitle(title)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        try {
                            doSign();
                            String id = mList.get(which).getId();
                            id = TextUtils.isEmpty(id) ? "" : id;
                            String filepath = mContext.getExternalCacheDir().getAbsolutePath();
                            final String filename = mList.get(which).getFileName();
                            //检索数据库，获取文件，否则下载
                            final String newFileName = getRealFileName(filename);

                            final Attachment attachment = mList.get(which);
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
        if (fromSP && mNode.isPass == 1) {
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
        params.put("signnum", mNode.signNum + "");

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
