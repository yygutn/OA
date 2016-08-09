package cn.edu.jumy.oa.UI.TaskItem;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.FileObserver;
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
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.BroadCastReceiver.UploadBroadcastReceiver;
import cn.edu.jumy.oa.CallBack.UserCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.UserResponse;
import cn.edu.jumy.oa.UI.DepartmentSelectActivity_;
import cn.edu.jumy.oa.Utils.OpenApp;
import cn.edu.jumy.oa.adapter.ListDropDownAdapter;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollListView;
import cn.edu.jumy.oa.widget.customview.UploadItem;
import cn.edu.jumy.oa.widget.customview.UploadItem_;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;
import okhttp3.Call;

/**
 * Created by Jumy on 16/6/27 14:47.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_meeting_send)
public class SendMeetingActivity extends BaseActivity {
    @ViewById(R.id.toolbar)
    protected Toolbar mToolbar;
    @ViewById(R.id.Undertaking_Unit)
    protected AppCompatTextView mDropDownMenuUnit;
    @ViewById(R.id.dropDownMenu_1)
    protected AppCompatTextView mDropDownMenu1;
    @ViewById(R.id.dropDownMenu_2)
    protected DropDownMenu mDropDownMenu2;
    @ViewById(R.id.meetingNumber)
    protected AppCompatEditText mMeetingNumber;
    @ViewById(R.id.meeting_title)
    protected AppCompatEditText mMeetingTitle;
    @ViewById(R.id.meeting_name)
    protected AppCompatEditText mMeetingName;
    @ViewById(R.id.meeting_details)
    protected AppCompatEditText mMeetingDetails;
    @ViewById(R.id.meeting_time)
    protected AppCompatTextView mMeetingTime;
    @ViewById(R.id.meeting_people)
    protected AppCompatEditText mMeetingPeople;
    @ViewById(R.id.meeting_phone)
    protected AppCompatEditText mMeetingPhone;
    @ViewById(R.id.meeting_loc)
    protected AppCompatEditText mMeetingLoc;
    @ViewById(R.id.addUpload)
    protected AppCompatTextView mAddUpload;
    @ViewById(R.id.uploadView)
    protected LinearLayout mUploadView;
    @ViewById(R.id.submit)
    protected TextView mSubmit;

    List<String> mFilePath = new ArrayList<>();
    @ViewById(R.id.uploadView)
    LinearLayout uploadView;
    int index = 0;

    String receiveUnits = "";//接收单位
    String receiveUnitsID = "";//接收单位ID
    String UndertakingUnits = "";//承办单位
    String UndertakingUnitsID = "";//承办单位ID
    String level = "0"; //等级
    Map<String, File> fileMap;

    private ListDropDownAdapter mLevelAdapter;

    private ArrayList<String> mLevel = new ArrayList<>(Arrays.asList(new String[]{"请选择", "特急", "加急", "平急", "特提"}));
    private String headers[] = {"请选择"};

    private List<View> popupView2 = new ArrayList<>();


    BroadcastReceiver uploadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == UploadBroadcastReceiver.UPLOAD_BR_RESULT) {
                String path = intent.getStringExtra(UploadBroadcastReceiver.EXTRA_PATH);
                if (!mFilePath.contains(path)) {
                    mFilePath.add(path);
                }
            }
            if (intent.getAction() == UploadBroadcastReceiver.UPLOAD_BR_RESULT_DELETE) {
                String path = intent.getStringExtra(UploadBroadcastReceiver.EXTRA_PATH);
                if (mFilePath.contains(path)) {
                    mFilePath.remove(path);
                }
            }
        }
    };

    String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Office Lens";

    Map<String, Boolean> picMap = new HashMap<>();//扫描件
    FileObserver listener = new FileObserver(dir, FileObserver.CREATE) {
        @Override
        public void onEvent(int event, String path) {
            picMap.put(path, true);
        }
    };

    private void dealZipFile() {
        fileMap = new HashMap<>();
        for (String path : mFilePath) {
            File file = new File(path);
            fileMap.put(file.getName(), file);
        }
    }

    @AfterViews
    void start() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        initLevelView();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadBroadcastReceiver.UPLOAD_BR_RESULT);
        filter.addAction(UploadBroadcastReceiver.UPLOAD_BR_RESULT_DELETE);
        registerReceiver(uploadBroadcastReceiver, filter);
        File dirs = new File(dir);
        if (!dirs.exists()) {
            dirs.mkdir();
        }
        getMyInfo();
    }

    private void getMyInfo() {
        OAService.getMyUser(new UserCallback() {
            @Override
            public void onResponse(UserResponse response, int ID) {
                String orgname[] = response.data.orgname.split(",");
                UndertakingUnits = "";
                int len = orgname.length;
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        UndertakingUnits = orgname[i];
                    } else {
                        UndertakingUnits += "/" + orgname[i];
                    }
                }

                UndertakingUnitsID = response.data.oid;
                if (!TextUtils.isEmpty(UndertakingUnitsID)) {
                    mDropDownMenuUnit.setText(UndertakingUnits);
                }
                if (TextUtils.isEmpty(UndertakingUnitsID)){
                    getMyInfo();
                }
            }
        });
    }

    @Click({R.id.submit, R.id.addUpload, R.id.meeting_time, R.id.addUpload_2, R.id.Undertaking_Unit, R.id.dropDownMenu_1})
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
            case R.id.meeting_time: {
                int year, month, day;
                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH_DAY);
                picker.setRange(2016, 2050);//年份范围
                picker.setLabel("年", "月", "日");
                picker.setSelectedItem(year, month + 1, day);
                picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                    @Override
                    public void onDatePicked(String year, String month, String day) {
                        getTime(year, month, day);
                    }
                });
                picker.show();
                break;
            }
            case R.id.addUpload_2: {
                Intent app = OpenApp.getApplicationWithPackageName(OpenApp.OFFICE_LENS, mContext, "请先安装Office Lens");
                if (app != null) {
                    listener.startWatching();
                    startActivityForResult(app, 110);
                }
                break;
            }
            case R.id.Undertaking_Unit: {
                DepartmentSelectActivity_.intent(mContext).extra("requestCode", 0).startForResult(0);
                break;
            }
            case R.id.dropDownMenu_1: {
                DepartmentSelectActivity_.intent(mContext).startForResult(1);
                break;
            }
            default:
                break;
        }
    }

    private void getTime(final String year, final String month, final String day) {
        TimePicker timePicker = new TimePicker(this, TimePicker.HOUR_OF_DAY);
        timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                mMeetingTime.setText(year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00");
            }
        });
        timePicker.show();
    }

    private void confirmSubmit() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).
                setTitle("确认发送")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doSending();
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    ProgressDialog progressDialog;

    private void doSending() {


        if (TextUtils.isEmpty(UndertakingUnitsID)) {
            showToast("承办单位不能为空");
            return;
        }
        if (TextUtils.isEmpty(receiveUnitsID)) {
            showToast("接收单位不能为空");
            return;
        }
        String docNo = mMeetingNumber.getText().toString();
        String docTitle = mMeetingTitle.getText().toString();
        if (TextUtils.isEmpty(docTitle)) {
            showToast("发文标题不能为空");
            return;
        }
        String name = mMeetingName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("会议名称不能为空");
            return;
        }
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("发送中...");
        progressDialog.setTitle("会议发布");
        progressDialog.show();
        String docSummary = mMeetingDetails.getText().toString();
        String contactName = mMeetingPeople.getText().toString();
        String contactPhone = mMeetingPhone.getText().toString();
        String location = mMeetingLoc.getText().toString();
        String time = mMeetingTime.getText().toString();
        Map<String, String> params = new HashMap<>();
        params.put("department", receiveUnitsID);
        params.put("meetCompany", UndertakingUnitsID);
        params.put("level", level);
        params.put("docNo", docNo);
        params.put("docTitle", docTitle);
        params.put("docSummary", docSummary);
        params.put("contactName", contactName);
        params.put("contactPhone", contactPhone);
        params.put("addr", location);
        params.put("name", name);
        params.put("meetTimeString", time);
        dealZipFile();
        OAService.meetSend(params, fileMap, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                progressDialog.dismiss();
                showDebugException(e);
                showToast("服务器错误,发送失败");
            }

            @Override
            public void onResponse(String response, int id) {
                progressDialog.dismiss();
                Gson gson = new Gson();
                BaseResponse baseResponse = gson.fromJson(response, BaseResponse.class);
                if (baseResponse.code == 0) {
                    afterSent();
                } else {
                    showToast("发送失败" + (TextUtils.isEmpty(baseResponse.msg) ? "" : ("," + baseResponse.msg)));
                }
            }
        });
    }

    private void afterSent() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("发送成功")
                .setMessage("是否继续发布会议")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //continue
                        clearContent();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //ok
                        SentMeetingActivity_.intent(mContext).start();
                        AppManager.getInstance().finishCurActivity();
                    }
                })
                .create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void clearContent() {
        mDropDownMenu1.setText("");
        mDropDownMenuUnit.setText("");
        mDropDownMenu2.setTabText(mLevel.get(0));
        mMeetingNumber.setText("");
        mMeetingTitle.setText("");
        mMeetingName.setText("");
        mMeetingDetails.setText("");
        mMeetingTime.setText("");
        mMeetingPeople.setText("");
        mMeetingPhone.setText("");
        mMeetingLoc.setText("");
        mUploadView.removeAllViews();
        mFilePath.clear();
        index = 0;
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

    /**
     * 承办单位
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(0)
    void result1(int resultCode, Intent data) {
        if (resultCode == 0) {
            UndertakingUnitsID = data.getStringExtra("ids");
            UndertakingUnits = data.getStringExtra("str");
            mDropDownMenuUnit.setText(UndertakingUnits);
        } else if (resultCode == 1) {
            //没有选择接收单位
            UndertakingUnits = UndertakingUnitsID = "";
        }
    }

    /**
     * 接收单位
     *
     * @param resultCode
     * @param data
     */
    @OnActivityResult(1)
    void result2(int resultCode, Intent data) {
        if (resultCode == 0) {
            receiveUnitsID = data.getStringExtra("ids");
            receiveUnits = data.getStringExtra("str");
            mDropDownMenu1.setText(receiveUnits);
        } else if (resultCode == 1) {
            //没有选择接收单位
            receiveUnits = receiveUnitsID = "";
        }
    }

    /**
     * 扫描文件，图片附件在这里添加到页面显示
     */
    @OnActivityResult(110)
    void onPic() {
        listener.stopWatching();
        for (String name : picMap.keySet()) {
            File file = new File(dir, name);
            if (file != null && file.exists()) {
                UploadItem item = UploadItem_.build(mContext, this);
                item.setPathForShow(name);
                uploadView.addView(item, index++);
                mFilePath.add(file.getAbsolutePath());
            }
        }
        picMap.clear();
    }
}
