package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Meet;

/**
 * Created by Jumy on 16/6/23 17:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class MeetBroadcastReceiver extends BroadcastReceiver{

    private ArrayList<Meet> docList;
    private int type;

    public static final String MEET = "cn.edu.jumy.Meet";
    public static final String TYPE = "cn.edu.jumy.TYPE";
    public static final String MEET_LIST = "cn.edu.jumy.Meet_LIST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(MEET)){
            docList = intent.getParcelableArrayListExtra(MEET_LIST);
            type = intent.getIntExtra(TYPE,-1);
        }
    }

    public ArrayList<Meet> getMeetList() {
        return docList;
    }

    public int getType() {
        return type;
    }
}
