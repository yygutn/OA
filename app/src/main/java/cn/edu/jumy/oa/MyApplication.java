package cn.edu.jumy.oa;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.support.multidex.MultiDexApplication;

import com.fsck.k9.K9;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatuidemo.DemoApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.litepal.LitePalApplication;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.jumyframework.CrashHandler;
import okhttp3.OkHttpClient;

//import com.squareup.leakcanary.LeakCanary;


/**
 * 全局Application
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "Application";

    private static volatile Context context;
    public static String DEVICE_ID = "";

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        AppManager.getInstance().init();
        CrashHandler.getInstance().init(context);
        initOkHttpUtils();
        //HX
        DemoApplication.getInstance().init(this);
        //
        Logger.init("Jumy")
                .methodCount(3)
                .logLevel(LogLevel.FULL)
                .methodOffset(1);
        LitePalApplication.initialize(this);
        K9.getInstance().onCreate(this);
    }

    private void initOkHttpUtils() {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);//可访问所有Https网站
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
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
