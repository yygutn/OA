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
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chatuidemo.DemoApplication;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Alarm;
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
    @ViewById(R.id.week_text)
    TextView weekTxt;

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

    Calendar now;

    @AfterViews
    void start() {
        now = Calendar.getInstance();
        toolbar.setSubtitle(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1));
        setSupportActionBar(toolbar);

        monthView.setDPMode(DPMode.SINGLE);
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(this);
        monthView.setOnDatePickedListener(this);
        //设置有背景标识物的日期 传入需要背景的日期列表
        initMeettingList();
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                        Date date = new Date(year-1900, monthOfYear, dayOfMonth, hour, minute, 0);
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

    private void initMeettingList() {
        mMeetingList.add("2016-5-31");

        mMeetingListCopy.add("2016-05-31");
    }

    private void initNotification() {
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            String date = format1.format(new Date());
            if (date.contains("2016-05-31")) {
                for (int i = 0; i < new Random().nextInt(10); i++) {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    String time = sdf.format(new Date());
                    time = time + "在" + new Random().nextInt(1000) + "开会";
                    String message = "";
                    if (i != 0) {
                        message = content1;
                    } else if (i == 1) {
                        message = content2;
                    } else {
                        message = content3;
                    }
                    ContentItemViewAbs cia = new ContentItemViewAbs(this, "会议", time, message);
                    contentLayout.addView(cia);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat format2 = new SimpleDateFormat("EEEE");
            Date chooseDate = format1.parse(date);
            weekTxt.setText(format2.format(chooseDate));
            if (date.equals(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1) + "." + now.get(Calendar.DAY_OF_MONTH))) {
                weekTxt.setVisibility(View.INVISIBLE);
            } else {
                weekTxt.setVisibility(View.VISIBLE);
            }
            contentLayout.removeAllViews();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            String time = sdf.format(new Date());
            String now = sdf2.format(chooseDate);
            for (String dates : mMeetingListCopy) {
                if (dates.equals(now)) {
                    for (int i = 0; i < new Random().nextInt(10); i++) {

                        time = time + "在" + new Random().nextInt(1000) + "开会";
                        String message = "";
                        if (i != 0) {
                            message = content1;
                        } else if (i == 1) {
                            message = content2;
                        } else {
                            message = content3;
                        }
                        ContentItemViewAbs cia = new ContentItemViewAbs(this, "会议", time, message);
                        contentLayout.addView(cia);
                    }
                }
            }
            Toast.makeText(this, "" + date, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
