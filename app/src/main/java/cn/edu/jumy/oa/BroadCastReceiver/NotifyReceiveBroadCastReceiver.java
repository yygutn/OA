package cn.edu.jumy.oa.BroadCastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.NotifyBroadCastResponse;
import cn.edu.jumy.oa.Utils.NotifyUtils;
import cn.edu.jumy.oa.bean.Alarm;

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
            final NotifyBroadCastResponse response = new Gson().fromJson(intent.getStringExtra(NotifyUtils.ACTION),NotifyBroadCastResponse.class);
            onNotifyReceive(response);

            // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            String message = "";
            switch (response.action) {
                case "docSend":
                case "docReceive":{
                    message = "您有一个新的公文,请注意签收";
                    break;
                }
                case "docUrge": {
                    message = "您有一个新的公文正在被催收,请尽快签收";
                    break;
                }
                case "meetReceive":{
                    message = "您有一个新的会议,请注意签收";
                    break;
                }
                case "meetSend":
                case "meetUrge": {
                    message = "您有一个新的会议正在被催收,请尽快签收";
                    break;
                }
                case "noticeSend":
                case "getNotice": {
                    message = "您有一个新的公告,请注意签收";
                    break;
                }
                default:
                    break;
            }
            showNotification(context,manager,message);
        }
    }
    private void showNotification(Context context, NotificationManager manager,String message) {
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify = new Notification.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("您有新的消息,请注意查收!")// 设置在status
                // bar上显示的提示文字
                .setContentTitle("移动办公系统")// 设置在下拉statusBar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText(message)// TextView中显示的详细内容
//                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notify);
    }
    public abstract void onNotifyReceive(NotifyBroadCastResponse action);
}
