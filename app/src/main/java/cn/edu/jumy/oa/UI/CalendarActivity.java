package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hyphenate.chatuidemo.DemoApplication;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.bean.Alarm;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.widget.datepicker.calendar.bizs.decors.DPDecor;
import cn.edu.jumy.oa.widget.datepicker.calendar.cons.DPMode;
import cn.edu.jumy.oa.widget.datepicker.calendar.utils.MeasureUtil;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.MonthView;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.WeekView;
import cn.edu.jumy.oa.widget.datepicker.view.ContentItemViewAbs;
import cn.qqtheme.framework.picker.DatePicker;
import cn.qqtheme.framework.picker.TimePicker;

@EActivity(R.layout.activity_calendar)
@OptionsMenu(R.menu.add)
public class CalendarActivity extends BaseActivity implements MonthView.OnDateChangeListener, MonthView.OnDatePickedListener {

    @ViewById(R.id.month_calendar)
    MonthView monthView;
    @ViewById(R.id.week_calendar)
    WeekView weekView;
    @ViewById(R.id.toolbar)
    Toolbar toolbar;
    @ViewById(R.id.content_layout)
    LinearLayout contentLayout;

    @StringRes(R.string.message)
    String mNotification;
    @StringRes(R.string.content1)
    String content1;
    @StringRes(R.string.content2)
    String content2;
    @StringRes(R.string.content3)
    String content3;

    //会议日期列表
    List<String> mMeetingList = new ArrayList<>();
    List<String> mMeetingListCopy = new ArrayList<>();

    ArrayList<Meet> mList = new ArrayList<>();

    Calendar now = Calendar.getInstance();

    @Extra("date")
    long mTimeStamp;

    @AfterExtras
    void ex(){
        getList();
    }

    @AfterViews
    void start() {
        now.setTime(new Date());
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        toolbar.setSubtitle(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1));

        monthView.setDPMode(DPMode.SINGLE);
        if (mTimeStamp > 100){
            now.setTimeInMillis(mTimeStamp);
        }
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(this);
        monthView.setOnDatePickedListener(this);
        //设置有背景标识物的日期 传入需要背景的日期列表
        monthView.mCManager.setDecorBG(mMeetingList);
        monthView.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(Color.RED);
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
            }
        });
        //end

        weekView.setDPMode(DPMode.SINGLE);
        weekView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        weekView.setFestivalDisplay(true);
        weekView.setTodayDisplay(true);
        weekView.setOnDatePickedListener(this);
        initNotification();

    }
    private void initMeetingList() {
        for (Meet meet : mList){
            now.setTimeInMillis(meet.meetTime);
            int y = now.get(Calendar.YEAR);
            int m = now.get(Calendar.MONTH);
            int d = now.get(Calendar.DAY_OF_MONTH);
            String time = y+"-"+m+"-"+d;
            String timeCopy = y+"-"+(m>9?m:("0"+m))+"-"+(d>9?d:("0"+d));
            mMeetingList.add(time);
            mMeetingListCopy.add(timeCopy);
        }
    }

    /**
     * 初始化当天有会议的情况
     */
    private void initNotification() {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = format1.format(new Date());
        addCard(date);
    }

    private void addCard(String date) {
        int len = mList.size();
        for (int i = 0; i < mMeetingListCopy.size(); i++) {
            if (mMeetingListCopy.get(i).equals(date)) {
                Meet node = mList.get(i);
                String message = "";
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                message += "发文单位: " + node.sendDepartmentInfo + "\n";
                message += "发文时间: " + sdf.format(new Date(node.createTime)) + "\n";
                message += "承办单位: " + node.meetCompanyName + "\n";
                message += "会议时间: " + sdf.format(new Date(node.meetTime)) + "\n";
                message += "会议地点: " + node.addr + "\n";
                ContentItemViewAbs cia = new ContentItemViewAbs(this, "会议", "", message);
                contentLayout.addView(cia);
            }
        }
    }

    @Override
    public void onDateChange(int year, int month) {
        ActionBar toolbar = getSupportActionBar();
        if (null != toolbar)
            toolbar.setSubtitle(year + "." + month);
    }

    @Override
    public void onDatePicked(String date) {
        try {
            contentLayout.removeAllViews();
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
            Date chooseDate = format1.parse(date);
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf2.format(chooseDate);
            addCard(now);
        } catch (Exception e) {
            showDebugException(e);
        }
    }
    private void getList(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) - 1900;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if ((month < 8 && (month & 1) == 1) || (month >= 8 && (month & 1) == 0)) {
            if (day == 31) {
                day--;
            }
            if (month == 2 && day > 28) {
                day = 28;
            }
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
        } else {
            month--;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);

        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "20");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("meetCompany", "");
        params.put("signStatus", "");
        params.put("passStatus", "");

        OAService.meetReceive(params, new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response.code == 1 || response.data == null) {
                    showToast("获取会议列表失败");
                    return;
                }
                mList = response.data.pageObject;
                initMeetingList();
            }
        });
    }




    @OptionsItem(R.id.action_add_alarm)
    void add_alarm() {
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePicker picker = new cn.qqtheme.framework.picker.DatePicker(this, cn.qqtheme.framework.picker.DatePicker.YEAR_MONTH_DAY);
        picker.setRange(2016, 2050);//年份范围
        picker.setLabel("年", "月", "日");
        picker.setSelectedItem(year, month + 1, day);
        picker.setOnDatePickListener(new cn.qqtheme.framework.picker.DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                getTime(Integer.valueOf(year), Integer.valueOf(month), Integer.valueOf(day));
            }
        });
        picker.show();
    }

    private void getTime(final int year, final int month, final int day) {
        TimePicker timePicker = new TimePicker(this, TimePicker.HOUR_OF_DAY);
        timePicker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                addAlarm(year, month, day, Integer.valueOf(hour), Integer.valueOf(minute));
            }
        });
        timePicker.show();
    }

    private void addAlarm(final int year, final int monthOfYear, final int dayOfMonth, final int hour, final int minute) {
        // 添加日程提醒到数据库存储起来
        final AppCompatEditText edit = new AppCompatEditText(this);
        int margin = MeasureUtil.dp2px(MyApplication.getContext(), 15);
        edit.setPadding(margin, margin, margin, margin);
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).
                setTitle(year + "年" + monthOfYear + "月" + dayOfMonth + "日  " + hour + ":" + minute)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = edit.getText().toString();
                        Date date = new Date(year-1900, monthOfYear-1, dayOfMonth, hour, minute, 0);
                        Alarm alarm = new Alarm(str, date, DemoApplication.currentUserName);
                        boolean flag = alarm.save();
                        String message = flag ? "创建日程提醒成功" : "创建日程提醒失败,请重新创建";
                        showToast(message);
                    }
                }).setNegativeButton("取消", null)
                .setView(edit)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

}
