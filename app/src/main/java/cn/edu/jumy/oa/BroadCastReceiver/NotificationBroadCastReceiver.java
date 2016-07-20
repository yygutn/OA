package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Date;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.UI.CalendarActivity_;

/**
 * Created by Jumy on 16/7/20 12:29.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotificationBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            CalendarActivity_.intent(context.getApplicationContext()).flags(Intent.FLAG_ACTIVITY_NEW_TASK).extra("date",intent.getLongExtra("date",new Date().getTime())).start();
        } catch (Exception e) {
            BaseActivity.showDebugException(e);
        }
    }

}
