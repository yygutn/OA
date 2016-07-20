package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Jumy on 16/6/23 17:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class UploadBroadcastReceiver extends BroadcastReceiver{

    String path = "";

    public static final String UPLOAD_RESULT = "cn.edu.jumy.UPLOAD_RESULT";

    public static final String EXTRA_PATH = "EXTRA_PATH";
    public static final String UPLOAD_BR_RESULT = "cn.edu.jumy.UPLOAD_BR_RESULT";
    public static final String UPLOAD_BR_RESULT_DELETE = "cn.edu.jumy.UPLOAD_BR_RESULT_DELETE";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == UPLOAD_RESULT){
            path = intent.getStringExtra(EXTRA_PATH);
        }
    }

    public String getPath() {
        return path;
    }
}
