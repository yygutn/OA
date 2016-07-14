package cn.edu.jumy.oa.UI.TaskItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.ListDropDownAdapter;
import cn.edu.jumy.oa.adapter.MultiDropDownAdapter;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.Response.AccountResult;
import cn.edu.jumy.oa.safe.PasswordUtil;
import cn.edu.jumy.oa.server.UploadServer;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollGridView;
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
    protected DropDownMenu mDropDownMenu1;
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

    List<Account> accountList;

    String receiveUnits = "";//公文接收单位
    String level = "0"; //等级

    private Object str;

    private MultiDropDownAdapter mUnitAdapter;
    private ListDropDownAdapter mLevelAdapter;

    private ArrayList<String> mUnits = new ArrayList<>(Arrays.asList(new String[]{"省委办公厅", "省信访局", "省档案局", "省委机要局", "省人大常委办公厅"}));
    private ArrayList<String> mLevel = new ArrayList<>(Arrays.asList(new String[]{"请选择", "特急", "加急", "平急", "特提"}));
    private String headers[] = {"请选择"};

    private List<View> popupView1 = new ArrayList<>();
    private List<View> popupView2 = new ArrayList<>();


    BroadcastReceiver uploadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == UploadServer.UPLOAD_BR_RESULT) {
                String path = intent.getStringExtra(UploadServer.EXTRA_PATH);
                if (!mFilePath.contains(path)) {
                    mFilePath.add(path);
                }
            }
            if (intent.getAction() == UploadServer.UPLOAD_BR_RESULT_DELETE){
                String path = intent.getStringExtra(UploadServer.EXTRA_PATH);
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

            OAService.getOrganizationData(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    showDebugException(e);
                    showToast("网络异常，获取可发送单位失败");
                    mUnits.clear();
                    mUnits.add("请选择");
                }

                @Override
                public void onResponse(String response, int id) {
                    try {

                        Gson gson = new Gson();
                        AccountResult account = gson.fromJson(response, AccountResult.class);
                        if (account.code != 0) {
                            showToast("网络异常，获取可发送单位失败");
                            mUnits.clear();
                            mUnits.add("请选择");
                        } else {
                            mUnits.clear();
                            accountList = account.data;
                            for (Account data : accountList) {
                                mUnits.add(data.name);
                            }
                        }
                        initUnitView();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            initLevelView();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadServer.UPLOAD_BR_RESULT);
        filter.addAction(UploadServer.UPLOAD_BR_RESULT_DELETE);
        registerReceiver(uploadBroadcastReceiver, filter);

    }

    @Click({R.id.submit, R.id.addUpload})
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
            default:
                break;
        }
    }

    AlertDialog alertDialog;

    private void confirmSubmit() {
        alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("确认发送")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String docNo = mEt1.getText().toString();
                        String docTitle = mEt2.getText().toString();
                        String docMain = mEt3.getText().toString();
                        if (receiveUnits.equals("请选择")) {
                            showToast("请选择发文单位");
                            return;
                        }
                        if (TextUtils.isEmpty(docTitle)) {
                            showToast("发文标题不能为空");
                            return;
                        }
                        alertDialog.setTitle("发送中，请稍后...");

                        long time = new Date().getTime();
                        final String base = EMClient.getInstance().getCurrentUser() + "_" + time;
                        String zip = PasswordUtil.simpleEncpyt(base);

                        dealZipFile();

                        Map<String, String> params = new HashMap<>();
                        params.put("department", receiveUnits);
                        params.put("level", level);
                        params.put("docNo", docNo);
                        params.put("docTitle", docTitle);
                        params.put("docSummary", docMain);
                        OAService.docSend(params, fileMap, new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                showDebugException(e);
                                showToast("网络异常,发送失败");
                            }

                            @Override
                            public void onBefore(Request request, int id) {
                                super.onBefore(request, id);
                            }

                            @Override
                            public void inProgress(float progress, long total, int id) {
                                super.inProgress(progress, total, id);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (response.contains("0")) {
                                    showToast("发送成功");
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

    private void dealZipFile() {
        fileMap = new HashMap<>();
        for (String path : mFilePath) {
            File file = new File(path);
            fileMap.put(file.getName(), file);
        }
    }

    private void initUnitView() {
        final View view = getLayoutInflater().inflate(R.layout.layout_custom_release, null);
        NoScrollGridView gridView = (NoScrollGridView) view.findViewById(R.id.gridView);
        mUnitAdapter = new MultiDropDownAdapter(mContext, mUnits);
        gridView.setAdapter(mUnitAdapter);

        TextView textView = (TextView) view.findViewById(R.id.ok);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu1.setTabText(mUnitAdapter.getNeedString());
                getUnits(mUnitAdapter.checkedList);
                mDropDownMenu1.closeMenu();
            }
        });

        popupView1.add(view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUnitAdapter.setCheckItem(position);
            }
        });

        mDropDownMenu1.setDropDownMenu(Arrays.asList(headers), popupView1, null);

    }

    private void getUnits(List<Integer> checkedList) {
        receiveUnits = "";
        if (accountList == null || accountList.size() <= 0) {
            receiveUnits = "请选择";
            return;
        }
        for (int i = 0; i < checkedList.size(); i++) {
            if (i == 0) {
                receiveUnits += accountList.get(checkedList.get(0)).id;
            } else {
                receiveUnits += "," + accountList.get(checkedList.get(i)).id;
            }
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
        if (mDropDownMenu1.isShowing()) {
            mDropDownMenu1.closeMenu();
        } else if (mDropDownMenu2.isShowing()) {
            mDropDownMenu2.closeMenu();
        } else {
            super.onBackPressed();
        }
    }
}
