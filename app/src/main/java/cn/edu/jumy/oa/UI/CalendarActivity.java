package cn.edu.jumy.oa.UI;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.widget.datepicker.calendar.cons.DPMode;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.MonthView;
import cn.edu.jumy.oa.widget.datepicker.calendar.views.WeekView;
import cn.edu.jumy.oa.widget.datepicker.view.ContentItemViewAbs;

@EActivity
public class CalendarActivity extends BaseActivity implements MonthView.OnDateChangeListener, MonthView.OnDatePickedListener {

    private MonthView monthView;
    private WeekView weekView;
    private Toolbar toolbar;
    private LinearLayout contentLayout;
    private TextView weekTxt;
    private Calendar now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        AppManager.getInstance().addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        now = Calendar.getInstance();
        toolbar.setSubtitle(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1));
        setSupportActionBar(toolbar);


        monthView = (MonthView) findViewById(R.id.month_calendar);
        weekView = (WeekView) findViewById(R.id.week_calendar);
        contentLayout = (LinearLayout) findViewById(R.id.content_layout);
        weekTxt = (TextView) findViewById(R.id.week_text);

        monthView.setDPMode(DPMode.SINGLE);
        monthView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        monthView.setFestivalDisplay(true);
        monthView.setTodayDisplay(true);
        monthView.setOnDateChangeListener(this);
        monthView.setOnDatePickedListener(this);

        weekView.setDPMode(DPMode.SINGLE);
        weekView.setDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1);
        weekView.setFestivalDisplay(true);
        weekView.setTodayDisplay(true);
        weekView.setOnDatePickedListener(this);
        initNotification();

    }

    private void initNotification() {
        for (int i = 0; i < new Random().nextInt(10); i++) {
            ContentItemViewAbs cia = new ContentItemViewAbs(this);
            contentLayout.addView(cia);
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
            Date choosedate = format1.parse(date);
            weekTxt.setText(format2.format(choosedate));
            if (date.equals(now.get(Calendar.YEAR) + "." + (now.get(Calendar.MONTH) + 1) + "." + now.get(Calendar.DAY_OF_MONTH))) {
                weekTxt.setVisibility(View.INVISIBLE);
            } else {
                weekTxt.setVisibility(View.VISIBLE);
            }
            contentLayout.removeAllViews();
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String time=   sdf.format( new  Date());
            for (int i = 0; i < new Random().nextInt(10); i++) {
                ContentItemViewAbs cia = new ContentItemViewAbs(this,R.style.MainLayout,"会议",time+"在"+new Random().nextInt(1000)+"开会","会议提纲");
                contentLayout.addView(cia);
            }
            Toast.makeText(this, "" + date, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
