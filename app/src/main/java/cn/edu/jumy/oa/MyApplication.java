package cn.edu.jumy.oa;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.tlslibrary.activity.AppManager;

import cn.edu.jumy.oa.timchat.utils.CrashHandler;
import cn.edu.jumy.oa.timchat.utils.Foreground;


/**
 * 全局Application
 */
public class MyApplication extends MultiDexApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init(this);
        Foreground.init(this);
        context = getApplicationContext();
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    notification.doNotify(getApplicationContext(), R.drawable.ic_launcher);
                }
            });
        }
        CrashHandler.getInstance().init(context);
    }

    public static Context getContext() {
        return context;
    }

}
