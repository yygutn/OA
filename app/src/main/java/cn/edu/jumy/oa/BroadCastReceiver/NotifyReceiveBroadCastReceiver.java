package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 通告推送广播
 * Created by Jumy on 16/7/26 11:19.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public abstract class NotifyReceiveBroadCastReceiver extends BroadcastReceiver{
    public static final String ACTION_NOTIFY = "cn.edu.jumy.oa.BroadCastReceiver.NotifyReceive";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_NOTIFY)){
            //通告推送
            onNotifyReceive(context,intent);
        }
    }
    public abstract void onNotifyReceive(Context context,Intent intent);
}
