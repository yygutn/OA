package cn.edu.jumy.oa.UI.TaskItem;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.BroadCastReceiver.UploadBroadcastReceiver;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.DepartmentSelectActivity_;
import cn.edu.jumy.oa.Utils.OpenApp;
import cn.edu.jumy.oa.adapter.ListDropDownAdapter;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.safe.PasswordUtil;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollListView;
import cn.edu.jumy.oa.widget.customview.UploadItem;
import cn.edu.jumy.oa.widget.customview.UploadItem_;
import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by Jumy on 16/6/23 11:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_release)
public class DocumentReleaseActivity extends BaseActivity {
    @ViewById(R.id.dropDownMenu_1)
    protected AppCompatTextView mDropDownMenu1;
    @ViewById(R.id.dropDownMenu_2)
    protected DropDownMenu mDropDownMenu2;
    @ViewById(R.id.et_1)
    protected AppCompatEditText mEt1;
    @ViewById(R.id.et_2)
    protected AppCompatEditText mEt2;
    @ViewById(R.id.et_3)
    protected AppCompatEditText mEt3;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById
    TextView submit;

    List<String> mFilePath = new ArrayList<>();
    Map<String, File> fileMap;
    @ViewById(R.id.uploadView)
    LinearLayout uploadView;
    int index = 0;

    ArrayList<Account> accountList;

    String receiveUnitsID = "";//公文接收单位
    String receiveUnitsStr = "";//公文接收单位
    String level = "0"; //等级

    private Object str;

    private ListDropDownAdapter mLevelAdapter;

    private ArrayList<String> mLevel = new ArrayList<>(Arrays.asList(new String[]{"请选择", "特急", "加急", "平急", "特提"}));
    private String headers[] = {"请选择"};

    private List<View> popupView2 = new ArrayList<>();


    BroadcastReceiver uploadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UploadBroadcastReceiver.UPLOAD_BR_RESULT)) {
                String path = intent.getStringExtra(UploadBroadcastReceiver.EXTRA_PATH);
                if (!mFilePath.contains(path)) {
                    mFilePath.add(path);
                }
            }
            if (intent.getAction().equals(UploadBroadcastReceiver.UPLOAD_BR_RESULT_DELETE)) {
                String path = intent.getStringExtra(UploadBroadcastReceiver.EXTRA_PATH);
                if (mFilePath.contains(path)) {
                    mFilePath.remove(path);
                }
            }
        }
    };

    @AfterViews
    void start() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            initLevelView();
        } catch (Exception e) {
            showDebugException(e);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadBroadcastReceiver.UPLOAD_BR_RESULT);
        filter.addAction(UploadBroadcastReceiver.UPLOAD_BR_RESULT_DELETE);
        registerReceiver(uploadBroadcastReceiver, filter);

    }

    @Click({R.id.submit, R.id.addUpload, R.id.dropDownMenu_1, R.id.addUpload_2})
    void clickSubmit(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                confirmSubmit();
                break;
            }
            case R.id.addUpload: {
                UploadItem item = UploadItem_.build(mContext, this);
                uploadView.addView(item, index++);
                break;
            }
            case R.id.dropDownMenu_1: {
                DepartmentSelectActivity_.intent(mContext).startForResult(REQUEST_CODE);
                break;
            }
            case R.id.addUpload_2: {
                OpenApp.doStartApplicationWithPackageName(OpenApp.OFFICE_LENS, mContext, "请先安装Office Lens");
                break;
            }
            default:
                break;
        }
    }

    AlertDialog alertDialog;
    ProgressDialog progressDialog;

    private void confirmSubmit() {
        alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("确认发送")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String docNo = mEt1.getText().toString();
                        String docTitle = mEt2.getText().toString();
                        String docMain = mEt3.getText().toString();
                        if (TextUtils.isEmpty(receiveUnitsID)) {
                            showToast("请选择接收单位");
                            return;
                        }
                        if (TextUtils.isEmpty(docTitle)) {
                            showToast("发文标题不能为空");
                            return;
                        }
                        progressDialog = new ProgressDialog(mContext);
                        progressDialog.setMessage("发送中...");
                        progressDialog.setTitle("公文发布");
                        progressDialog.show();

                        long time = new Date().getTime();
                        final String base = EMClient.getInstance().getCurrentUser() + "_" + time;
                        String zip = PasswordUtil.simpleEncpyt(base);

                        dealZipFile();

                        Map<String, String> params = new HashMap<>();
                        params.put("department", receiveUnitsID);
                        params.put("level", level);
                        params.put("docNo", docNo);
                        params.put("docTitle", docTitle);
                        params.put("docSummary", docMain);
                        OAService.docSend(params, fileMap, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                progressDialog.dismiss();
                                showDebugException(e);
                                showToast("网络异常,发送失败");
                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                progressDialog.dismiss();
                                if (response.contains("0")) {
                                    afterSent();
                                } else {
                                    JSONObject object = new Gson().fromJson(response, JSONObject.class);
                                    try {
                                        if (TextUtils.isEmpty(object.get("data").toString())) {
                                            showToast("服务器异常,发送失败");
                                        } else {
                                            showToast(object.get("data").toString());
                                        }
                                    } catch (JSONException e) {
                                        showToast("服务器异常,发送失败");
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void afterSent() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("发送成功")
                .setMessage("是否继续发布公文")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //continue
                        clearContent();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        AlertDialog dialog = new AlertDialog.Builder(mContext)
                                .setMessage("是否进入已发布公文界面")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        backToPreActivity();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //ok
                                        SentDocumentActivity_.intent(mContext).start();
                                        AppManager.getInstance().finishCurActivity();
                                    }
                                })
                                .create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void clearContent() {
        mDropDownMenu1.setText("");
        mDropDownMenu2.setTabText(mLevel.get(0));
        mEt1.setText("");
        mEt2.setText("");
        mEt3.setText("");
    }

    private void dealZipFile() {
        fileMap = new HashMap<>();
        for (String path : mFilePath) {
            File file = new File(path);
            fileMap.put(file.getName(), file);
        }
    }

    private void initLevelView() {
        final NoScrollListView listView = new NoScrollListView(mContext);
        listView.setDividerHeight(0);
        mLevelAdapter = new ListDropDownAdapter(mContext, mLevel);
        listView.setAdapter(mLevelAdapter);

        popupView2.add(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLevelAdapter.setCheckItem(position);
                mDropDownMenu2.setTabText(mLevel.get(position));
                level = position + "";
                mDropDownMenu2.setTabTextColor(pressed);
                mDropDownMenu2.closeMenu();
            }
        });

        mDropDownMenu2.setDropDownMenu(Arrays.asList(headers), popupView2, null);
    }

    @ColorRes(R.color.pressed)
    int pressed;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu2.isShowing()) {
            mDropDownMenu2.closeMenu();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            super.onBackPressed();
        }
    }

    protected static final int REQUEST_CODE = 0;

    @OnActivityResult(REQUEST_CODE)
    void onResult(int resultCode, Intent data) {
        if (resultCode == 0) {
            receiveUnitsID = data.getStringExtra("ids");
            receiveUnitsStr = data.getStringExtra("str");
            mDropDownMenu1.setText(receiveUnitsStr);
        } else if (resultCode == 1) {
            //没有选择接收单位
            receiveUnitsStr = receiveUnitsID = "";
        }
    }
}
