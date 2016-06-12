package cn.edu.jumy.oa.timchat.ui;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.tencent.TIMLogLevel;
import com.tencent.TIMManager;
import com.tencent.qalsdk.QALSDKManager;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import cn.edu.jumy.oa.R;

import cn.edu.jumy.oa.timchat.ui.customview.LineControllerView;
import cn.edu.jumy.oa.timchat.ui.customview.ListPickerDialog;
import tencent.tls.platform.TLSHelper;

public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        LineControllerView imsdk = (LineControllerView) findViewById(R.id.imsdk);
        imsdk.setContent(TIMManager.getInstance().getVersion());
        LineControllerView qalsdk = (LineControllerView) findViewById(R.id.qalsdk);
        qalsdk.setContent(QALSDKManager.getInstance().getSdkVersion());
        LineControllerView tlssdk = (LineControllerView) findViewById(R.id.tlssdk);
        tlssdk.setContent(TLSHelper.getInstance().getSDKVersion());
        final LineControllerView log = (LineControllerView) findViewById(R.id.logLevel);
        final TIMLogLevel[] logLevels = TIMLogLevel.values();
        final String[] logNames = new String[logLevels.length];
        for (int i = 0 ; i < logLevels.length ; ++i){
            logNames[i] = logLevels[i].name();
        }
        log.setContent(TIMManager.getInstance().getLogLevel().name());
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ListPickerDialog().show(logNames, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, final int which) {
                        log.setContent(logNames[which]);
                        SharedPreferences.Editor editor = getSharedPreferences("data", MODE_PRIVATE).edit();
                        editor.putInt("loglvl", which);
                        editor.apply();
                    }
                });
            }
        });
    }



}
