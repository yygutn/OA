package cn.edu.jumy.oa.BroadCastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.server.UploadServer;

/**
 * Created by Jumy on 16/6/23 17:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DocumentBroadcastReceiver extends BroadcastReceiver{

    private ArrayList<Doc> docList;
    private int type;

    public static final String DOC = "cn.edu.jumy.DOC";
    public static final String TYPE = "cn.edu.jumy.TYPE";
    public static final String DOC_LIST = "cn.edu.jumy.DOC_LIST";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == DOC){
            docList = intent.getParcelableArrayListExtra(DOC_LIST);
            type = intent.getIntExtra(TYPE,0);
            if (BaseActivity.DEBUG){
                Logger.t("DocumentBroadcastReceiver").v(docList.toString());
            }
        }
    }

    public ArrayList<Doc> getDocList() {
        return docList;
    }

    public int getType() {
        return type;
    }
}
