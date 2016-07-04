package cn.edu.jumy.oa.bean;

import android.content.Context;

import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import cn.edu.jumy.oa.MainActivity;


/**
 * Created by Jumy on 16/6/14 16:14.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class NotificationClickHandler extends UmengNotificationClickHandler{
    @Override
    public void openActivity(Context context, UMessage uMessage) {
        super.openActivity(context, uMessage);
    }

    @Override
    public void launchApp(Context context, UMessage uMessage) {
        MainActivity.getMainInstance().mTabHost.onTabChanged(MainActivity.getMainInstance().mTextViewArray[0]);
        super.launchApp(context, uMessage);
    }
}
