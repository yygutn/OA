package cn.edu.jumy.oa;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.support.multidex.MultiDexApplication;

import com.fsck.k9.K9;
import com.hyphenate.chatui.DemoApplication;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.litepal.LitePalApplication;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.CrashHandler;
import okhttp3.OkHttpClient;

//import com.fsck.k9.K9;

//import com.squareup.leakcanary.LeakCanary;


/**
 * 全局Application
 */
public class MyApplication extends MultiDexApplication {
    private static volatile Context context;
    private static volatile MyApplication instance;
    public static boolean isFirst = true;

    @Override
    public void onCreate() {
        if (!isInMainProcess(this)) {
            return;
        }
        super.onCreate();
        context = getApplicationContext();
        instance = this;
        AppManager.getInstance().init();
        initOkHttpUtils();
        //HX
        DemoApplication.getInstance().init(this);
        //
        if (BuildConfig.DEBUG) {
            Logger.init("Jumy").hideThreadInfo().methodOffset(0);
        } else {
            Logger.init("Release").logLevel(LogLevel.NONE);
        }
        LitePalApplication.initialize(this);
        CrashHandler.getInstance().init(this,MainActivity.class);
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

    public static MyApplication getInstance() {
        if (instance == null) {
            synchronized (MyApplication.class) {
                if (instance == null) {
                    instance = new MyApplication();
                }
                return instance;
            }
        }
        return instance;
    }

    /**
     * 注意：因为推送服务等设置为运行在另外一个进程，这导致本Application会被实例化两次。
     * 而有些操作我们需要让应用的主进程时才进行，所以用到了这个方法
     */
    public static boolean isInMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processes = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processes) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        System.gc();
    }
}
