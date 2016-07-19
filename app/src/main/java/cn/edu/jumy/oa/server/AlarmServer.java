package cn.edu.jumy.oa.server;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.litepal.crud.DataSupport;

import java.util.Date;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MainActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Alarm;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/19  上午11:41
 */
public class AlarmServer extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 在Android进行通知处理，首先需要重系统哪里获得通知管理器NotificationManager，它是一个系统Service。
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        List<Alarm> list = DataSupport.findAll(Alarm.class);
        BaseActivity.showDebugLogd(list.toString());
        Date date = new Date();
        for (Alarm alarm : list) {
            if (alarm.getTime().after(date) && sameDay(alarm.getTime(), date)) {
                showNotification(manager, alarm.getContent());
            }
        }
    }

    private void showNotification(NotificationManager manager, String content) {
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);
        // 通过Notification.Builder来创建通知，注意API Level
        // API11之后才支持
        Notification notify2 = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏中的小图片，尺寸一般建议在24×24，这个图片同样也是在下拉状态栏中所显示，如果在那里需要更换更大的图片，可以使用setLargeIcon(Bitmap
                // icon)
                .setTicker("TickerText:" + "您有新短消息，请注意查收！")// 设置在status
                // bar上显示的提示文字
                .setContentTitle("会议通知")// 设置在下拉status
                // bar后Activity，本例子中的NotififyMessage的TextView中显示的标题
                .setContentText(content)// TextView中显示的详细内容
                .setContentIntent(pendingIntent2) // 关联PendingIntent
                .setNumber(1) // 在TextView的右方显示的数字，可放大图片看，在最右侧。这个number同时也起到一个序列号的左右，如果多个触发多个通知（同一ID），可以指定显示哪一个。
                .getNotification(); // 需要注意build()是在API level
        // 16及之后增加的，在API11中可以使用getNotificatin()来代替
        notify2.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notify2);
    }

    private boolean sameDay(Date date, Date date2) {
        if (date.getDay() == date2.getDay() && date.getYear() == date2.getYear() && date.getMonth() == date2.getMonth()) {
            return true;
        }
        return false;
    }
}
