package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.bean.Relay;

/**
 * Created by Jumy on 16/6/23 17:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class RelayBroadcastReceiver extends BroadcastReceiver{

    private ArrayList<Relay> docList;
    private int type;

    public static final String RELAY = "cn.edu.jumy.Meet";
    public static final String TYPE = "cn.edu.jumy.TYPE";
    public static final String RELAY_LIST = "cn.edu.jumy.RELAY_LIST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == RELAY){
            docList = intent.getParcelableArrayListExtra(RELAY_LIST);
            type = intent.getIntExtra(TYPE,-1);
            if (BaseActivity.DEBUG){
                Logger.t("RelayBroadcastReceiver").v(docList.toString());
            }
        }
    }

    public ArrayList<Relay> getList() {
        return docList;
    }

    public int getType() {
        return type;
    }
}