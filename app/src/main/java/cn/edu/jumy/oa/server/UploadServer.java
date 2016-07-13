package cn.edu.jumy.oa.server;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/6/23 17:37.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class UploadServer extends IntentService {

    private static final String ACTION_UPLOAD_IMG = "UPLOAD_IMG";
    private static final String ACTION_UPLOAD_FILE = "UPLOAD_FILE";
    public static final String EXTRA_PATH = "EXTRA_PATH";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String UPLOAD_BR_RESULT = "cn.edu.jumy.UPLOAD_BR_RESULT";
    public static final String UPLOAD_BR_RESULT_DELETE = "cn.edu.jumy.UPLOAD_BR_RESULT_DELETE";

    public static void startUpload(Context context, String path, String name,int type)
    {
        Intent intent = new Intent(context.getApplicationContext(), UploadServer.class);
        intent.setAction(type==0?ACTION_UPLOAD_IMG:ACTION_UPLOAD_FILE);
        intent.putExtra(EXTRA_PATH, path);
        intent.putExtra(EXTRA_NAME, name);
        context.getApplicationContext().startService(intent);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * <p/>
     * Used to name the worker thread, important only for debugging.
     */
    public UploadServer() {
        super("UploadServer");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        final String action = intent.getAction();
        if (ACTION_UPLOAD_IMG.equals(action)) {
            final String path = intent.getStringExtra(EXTRA_PATH);
            final String name = intent.getStringExtra(EXTRA_NAME);
            handleUpload(path,name,0);
        }
        if (ACTION_UPLOAD_FILE.equals(action)) {
            final String path = intent.getStringExtra(EXTRA_PATH);
            final String name = intent.getStringExtra(EXTRA_NAME);
            handleUpload(path,name,1);
        }
    }

    private void handleUpload(String path,String name,int type) {
        OkHttpUtils.post().addFile(type==0?"image":"file",name,new File(path)).build().execute(new Callback() {
            @Override
            public Object parseNetworkResponse(Response response, int id) throws Exception {
                return null;
            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(Object response, int id) {
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = new NotificationCompat.Builder(MyApplication.getContext());
                Notification notification = builder
                        .setContentTitle("会议发送成功")
                        .setContentText("附件已成功上传")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(1, notification);
                stopSelf();
            }
        });
    }


}
