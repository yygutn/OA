package cn.edu.jumy.oa;

import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.fsck.k9.K9;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.https.HttpsUtils;

import java.util.concurrent.TimeUnit;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.oa.bean.NotificationClickHandler;
import cn.edu.jumy.oa.timchat.utils.CrashHandler;
import cn.edu.jumy.oa.timchat.utils.Foreground;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;


/**
 * 全局Application
 */
public class MyApplication extends MultiDexApplication {

    private static final String TAG = "Application";

    private static Context context;
    public static final String API_URL = "";
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
        initOkHttpUtils();
        //Umeng Push init start
        PushAgent.getInstance(context).enable(new IUmengRegisterCallback() {
            @Override
            public void onRegistered(String registrationId) {
                Log.e(TAG, "onRegistered: "+registrationId);
            }
        });
        PushAgent.getInstance(context).setNotificationClickHandler(new NotificationClickHandler());
        //Umeng Push init end
        RealmConfiguration config = new RealmConfiguration.Builder(context).build();
        Realm.setDefaultConfiguration(config);
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
        return context;
    }

}
