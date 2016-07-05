package cn.edu.jumy.oa;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.fsck.k9.K9;
import com.hyphenate.chatuidemo.DemoApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.litepal.LitePalApplication;

import java.util.concurrent.TimeUnit;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.CrashHandler;
import okhttp3.OkHttpClient;


/**
 * 全局Application
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "Application";

    private static volatile Context context;
    public static final String API_URL = "";
    public static String DEVICE_ID = "";

    @Override
    public void onCreate() {
        super.onCreate();
        AppManager.getInstance().init();
        K9.getInstance().onCreate(this);
        context = getApplicationContext();
        CrashHandler.getInstance().init(context);
        initOkHttpUtils();
        //Umeng Push init start
        PushAgent.getInstance(context).enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String DeviceId) {
                DEVICE_ID = DeviceId;
                Log.e(TAG, "onRegistered: " + DeviceId);
            }
        });
        PushAgent.getInstance(context).setNotificationClickHandler(new NotificationClickHandler());
        //Umeng Push init end
        //HX
        DemoApplication.getInstance().init(this);
        //end
        LeakCanary.install(this);
        //
        Logger
                .init("Jumy")
                .methodCount(3)
                .logLevel(LogLevel.FULL)
                .methodOffset(2);
        LitePalApplication.initialize(this);
    }

    private void initOkHttpUtils() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//可访问所有Https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static Context getContext() {
        if (context == null) {
            synchronized (MyApplication.class) {
                if (context == null) {
                    context = new MyApplication();
                }
            }
        }
        return context;
    }

}
