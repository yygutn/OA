package cn.edu.jumy.oa.UI.TaskItem;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
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
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.AttachResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.Relay2Response;
import cn.edu.jumy.oa.Response.RelaySingleResponse;
import cn.edu.jumy.oa.UI.OrgRelaySelectActivity_;
import cn.edu.jumy.oa.UI.SignUpMultiAbleActivity_;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.bean.Annex;
import cn.edu.jumy.oa.bean.Attachment;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Relay;
import cn.edu.jumy.oa.widget.customview.ItemTableRowAuditDetails_;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
import okhttp3.Call;
import okhttp3.Request;
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
    @ViewById(R.id.root_ll)
    protected LinearLayout mRootLayout;

    TextView mZFYJ;
    TextView mZFDW;
    TextView mSPYJ;


    @Extra("details")
    protected Node mNode = new Node();
    @Extra("from_sent_meet")
    boolean fromSentMeet = false;
    @Extra("from_SP")
    boolean fromSP = false;
    @Extra("FromFQ")
    boolean fromFQ = false;

    public static boolean FROM_NOTIFY = false;

    TableLayout mTableDone;
    TableLayout mTableUndo;
    ArrayList<Relay> mListUndo;
    ArrayList<Relay> mListDone;

    //附件列表
    ArrayList<Attachment> mList;

    public static ClipboardManager clipboardManager;

    @AfterExtras
    void getList() {
        Map<String, String> params = new ArrayMap<>();
        String id = "";
        if (fromSP) {
            id = mNode.oldid;
        } else {
            id = mNode.id;
        }
        params.put("pid", TextUtils.isEmpty(id) ? "" : id);
        OAService.getAttachmentList(params, new AttachListCallBack() {
            @Override
            public void onResponse(AttachResponse response, int id) {
                mList = response.data;
                if (mList == null || mList.size() <= 0) {
                    if (fromFQ || fromSP) {
                        return;
                    }
                    doSign();
                }
            }
        });
    }

    /**
     * 来自审批||转发
     */
    private void setUpAuditView() {
        if (fromFQ) {//我的发起详情
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_document_details_extra, null);
            mZFYJ = (TextView) view.findViewById(R.id.mZFYJ);
            mZFDW = (TextView) view.findViewById(R.id.mZFDW);
            mTableDone = (TableLayout) view.findViewById(R.id.sign_details_table_done);
            mTableUndo = (TableLayout) view.findViewById(R.id.sign_details_table_undo);
            mRootLayout.addView(view);
            mListUndo = new ArrayList<>();
            mListDone = new ArrayList<>();
            OAService.findapproved(mNode.id, (mNode.type == 1 ? 1 : 2) + "", new RelayCallBack());
            mZFYJ.setText(mNode.relayRemark);
            mZFDW.setText(mNode.dispatchUnit);
        } else if (fromSP) {//我的审批详情
            View view = LayoutInflater.from(mContext).inflate(R.layout.layout_document_details_extra_2, null);
            mZFYJ = (TextView) view.findViewById(R.id.mZFYJ);
            mZFDW = (TextView) view.findViewById(R.id.mZFDW);
            mSPYJ = (TextView) view.findViewById(R.id.mSPYJ);
            mRootLayout.addView(view);
            mZFYJ.setText(mNode.relayRemark);
            mZFDW.setText(mNode.dispatchUnit);
            //获取我的审批意见并显示
            OAService.ApproveGet(mNode.tid, new SingleRelayCallback());
        }
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
        setUpAuditView();
    }

    /**
     * 签收
     */
    private void doSign() {
        if (TextUtils.isEmpty(mNode.tid) || mNode.signStatus == 0) {
            return;
        }
        if (mNode.type == 1 && !fromSP && !fromSentMeet) {
            DocSign();
        } else if (mNode.type == 0 && !fromSP && !fromSentMeet) {
            MeetSign();
        } else if (mNode.type == 2) {
            NoticeSign();
        }
        setResult(1025);
    }

    private void MeetSign() {
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

    private void DocSign() {
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

    private void NoticeSign() {
        OAService.noticeSign(mNode.tid, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int ID) {
                showDebugException(e);
                showToast("服务器错误,公告签收失败");
            }

            @Override
            public void onResponse(String response, int ID) {
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
//                    showToast("签收公告成功");
                    setResult(1025);
                } else {
                    if (!TextUtils.isEmpty(baseResponse.msg)) {
                        showToast(baseResponse.msg);
                        if (baseResponse.msg.contains("已签收")) {
                            setResult(1025);
                        }
                    }
                }
            }
        });
    }

    private void setUpViews() {
        try {
            //催收按钮
            mCuiS.setVisibility(fromSentMeet ? View.VISIBLE : View.GONE);
            //标题处理
            switch (mNode.type) {
                case 0: {
                    mTitleBar.setTitle("会议详情");
                    //                mDocumentDetailsLevel.setVisibility(View.GONE);
                    mDocumentDetailsSignUp.setVisibility(fromSentMeet || fromSP || fromFQ ? View.GONE : View.VISIBLE);
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
            if (fromSP || fromFQ) {
                mTitleBar.setTitle("详情");
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

            mDocumentDetailsContent_meet.setText(CardGenerator.getZFContentString(mNode));

            if (!TextUtils.isEmpty(mNode.contactName) && !TextUtils.isEmpty(mNode.contactPhone)) {
                mDocumentDetailsOther.setText("联系人: " + mNode.contactName + "\n" + "联系电话: " + mNode.contactPhone);
            } else {
                mDocumentDetailsOther.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        spannableString.setSpan(sizeSpan0, 0, 3, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, 4, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        final TextView title = new TextView(mContext);
        title.setTextColor(Color.BLACK);
        title.setText(spannableString);
        int padding = MeasureUtil.dp2px(mContext, 16);
        title.setPadding(padding, padding, padding, 0);
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
                            File temp = Annex.getFileByByte(annex);
                            if (annex != null && temp != null) {
                                CallOtherOpenFile.openFile(mContext, temp);
                                alertDialog.cancel();
                            } else {
                                final ProgressDialog progressDialog = new ProgressDialog(mContext);
                                progressDialog.setMessage("下载中...");
                                progressDialog.setMax(100);
                                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                progressDialog.setIndeterminate(false);
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                    @Override
                                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                        if (keyCode == KeyEvent.KEYCODE_BACK && isDownLoading) {
                                            OkHttpUtils.getInstance().cancelTag(MyApplication.getContext());
                                            isDownLoading = false;
                                            return true;
                                        }
                                        return false;
                                    }
                                });

                                OAService.downloadAttachment(id, new FileCallBack(filepath, filename) {

                                    @Override
                                    public void onBefore(Request request, int ID) {
                                        super.onBefore(request, ID);
                                        progressDialog.show();
                                        isDownLoading = true;
                                    }

                                    @Override
                                    public void onAfter(int ID) {
                                        super.onAfter(ID);
                                        progressDialog.dismiss();
                                        isDownLoading = false;
                                    }

                                    @Override
                                    public void inProgress(float progress, long total, int ID) {
                                        super.inProgress(progress, total, ID);
                                        int percent = (int) (progress * 100);
                                        progressDialog.setProgress(percent);
                                    }

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
                                            tempNode.setByteFile(Annex.File2byte(file));
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

    boolean isDownLoading = false;


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
        } else if (!fromSP && mNode.type != 2 && !fromFQ) {
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
        Map<String, String> params = new ArrayMap<>();
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

    private class RelayCallBack extends Callback<Relay2Response> {

        @Override
        public Relay2Response parseNetworkResponse(Response response, int ID) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, Relay2Response.class);
            } else {
                return new Relay2Response(baseResponse);
            }
        }

        @Override
        public void onError(Call call, Exception e, int ID) {
            showToast("服务器错误,获取审批意见失败");
        }

        @Override
        public void onResponse(Relay2Response response, int ID) {
            try {
                if (response.code == 0) {
                    if (response.data == null) return;
                    mListUndo.clear();
                    mListDone.clear();
                    removeItemViews(mTableDone);
                    removeItemViews(mTableUndo);
                    for (Relay relay : response.data) {
                        if (relay.remark.equals("0")) {
                            mListDone.add(relay);
                        } else {
                            mListUndo.add(relay);
                        }
                    }
                    updateView();
                } else {
                    showToast(response.msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateView() {
        if (mTableDone == null) {
            return;
        }
        for (Relay auditUser : mListDone) {
            mTableDone.addView(ItemTableRowAuditDetails_.build(mContext, auditUser, this));
        }
        if (mTableUndo == null) {
            return;
        }
        int len = mListUndo.size();
        for (int i = 0; i < len; i++) {
            mTableUndo.addView(ItemTableRowAuditDetails_.build(mContext, mListUndo.get(i), this));
        }
        mTableUndo.addView(View.inflate(mContext, R.layout.layout_empty, null));
    }

    private void removeItemViews(TableLayout tableLayout) {
        try {
            tableLayout.removeAllViews();
            tableLayout.addView(LayoutInflater.from(mContext).inflate(R.layout.layout_item_table_row_audit_details, null));
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    private class SingleRelayCallback extends Callback<RelaySingleResponse> {

        @Override
        public RelaySingleResponse parseNetworkResponse(Response response, int ID) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, RelaySingleResponse.class);
            } else {
                return new RelaySingleResponse(baseResponse);
            }
        }

        @Override
        public void onError(Call call, Exception e, int ID) {
            showDebugException(e);
            showToast("网络错误,获取我的审批意见失败");
        }

        @Override
        public void onResponse(RelaySingleResponse response, int ID) {
            if (response.code == 1) {
                showToast(response.msg);
                return;
            }
            if (response.data == null) {
                return;
            }
            mSPYJ.setText(response.data.passRemark);
        }
    }
}
