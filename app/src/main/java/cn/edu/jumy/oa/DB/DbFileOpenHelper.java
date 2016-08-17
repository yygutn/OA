package cn.edu.jumy.oa.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hyphenate.chatui.DemoHelper;

import cn.edu.jumy.oa.MyApplication;

/**
 * Created by Jumy on 16/6/20 14:44.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class DbFileOpenHelper extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static DbFileOpenHelper instance;

    private static final String FILE_TABLE_CREATE = "CREATE TABLE "
            + FileDao.TABLE_NAME + " ("
            + FileDao.COLUMN_NAME_TOKEN + " TEXT, "
            +FileDao.COLUMN_FILE + "FILE, "
            + FileDao.COLUMN_NAME_ID + " TEXT PRIMARY KEY);";

    public DbFileOpenHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    private static String getUserDatabaseName() {
        return  DemoHelper.getInstance().getCurrentUserName() + "_file.db";
    }

    public static DbFileOpenHelper getInstance() {
        if (instance == null){
            instance = new DbFileOpenHelper(MyApplication.getContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FILE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void closeDB() {
        if (instance != null) {
            try {
                SQLiteDatabase db = instance.getWritableDatabase();
                db.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            instance = null;
        }
    }
}
