package cn.edu.jumy.oa;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.fsck.k9.K9;
import com.github.mmin18.layoutcast.LayoutCast;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.tlslibrary.activity.AppManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import cn.edu.jumy.oa.timchat.utils.CrashHandler;
import cn.edu.jumy.oa.timchat.utils.Foreground;
import okhttp3.OkHttpClient;


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
        K9.getInstance().onCreate(this);
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
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//可访问所有Https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
        LayoutCast.init(this);
    }

    public static Context getContext() {
        return context;
    }

}
