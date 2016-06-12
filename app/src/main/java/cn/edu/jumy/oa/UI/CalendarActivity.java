package cn.edu.jumy.oa.UI;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.jumy.jumyframework.BaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.widget.datepicker.calendar.bizs.decors.DPDecor;
import cn.edu.jumy.oa.widget.datepicker.calendar.cons.DPMode;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.MonthView;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.WeekView;
import cn.edu.jumy.oa.widget.datepicker.view.ContentItemViewAbs;

@EActivity(R.layout.activity_calendar)
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
