package cn.edu.jumy.oa.Utils;

import android.content.Context;
import android.content.Intent;

import cn.edu.jumy.oa.BroadCastReceiver.NotifyReceiveBroadCastReceiver;

/**
 * Created by Jumy on 16/7/26 12:22.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotifyUtils {
    public static final String ACTION = "cn.edu.jumy.oa.Utils.NotifyUtils.ACTION_GET";
    public static void sendNotifyBroadCast(Context mContext, String action) {
        Intent data = new Intent(NotifyReceiveBroadCastReceiver.ACTION_NOTIFY);
        data.putExtra(ACTION,action);
        mContext.sendBroadcast(data);
    }
}
