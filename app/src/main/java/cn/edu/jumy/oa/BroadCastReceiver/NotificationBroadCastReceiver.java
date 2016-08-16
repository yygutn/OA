package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MainActivity;
import cn.edu.jumy.oa.UI.CalendarActivity_;

/**
 * Created by Jumy on 16/7/20 12:29.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotificationBroadCastReceiver extends BroadcastReceiver {
    public static final String NOTIFY_FROM_HOME = "NOTIFY_FROM_HOME";
    public static final String NOTIFY_REFRESH_HOME = "NOTIFY_REFRESH_HOME";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (intent.getAction().equals(NOTIFY_FROM_HOME)) {
                AppManager.getInstance().back2Level1();
//                MainActivity.getMainInstance().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        MainActivity.getMainInstance().mTabHost.onTabChanged("notify");
//                        MainActivity.getMainInstance().mTabHost.setCurrentTab(0);
//                    }
//                });
                Intent refreshIntent = new Intent(NOTIFY_REFRESH_HOME);
                context.sendBroadcast(refreshIntent);
            } else {
                CalendarActivity_.intent(context.getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .extra("date", intent.getLongExtra("date", new Date().getTime())).start();
            }
            BaseActivity.showDebugLoge(NOTIFY_REFRESH_HOME);
        } catch (Exception e) {
            BaseActivity.showDebugException(e);
        }
    }

}
