package cn.edu.jumy.oa.DB;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.hyphenate.chatui.DemoHelper;

import java.util.List;

import cn.edu.jumy.oa.OaPreference;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/20 14:42.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class FileExplorerManager {
    private static FileExplorerManager manager = new FileExplorerManager();
    private DbFileOpenHelper dbHelper;

    public FileExplorerManager() {
        dbHelper = DbFileOpenHelper.getInstance();
    }

    public static synchronized FileExplorerManager getInstance() {
        if (manager == null) {
            manager = new FileExplorerManager();
        }
        return manager;
    }

    synchronized public void saveFileList(List<Node> list) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (db.isOpen()) {
            db.delete(FileDao.TABLE_NAME, null, null);
            for (Node node : list) {
                ContentValues values = new ContentValues();
                String username = OaPreference.getInstance().get("username");
                if (TextUtils.isEmpty(username)||!DemoHelper.getInstance().getCurrentUserName().equals(username)){
                    OaPreference.getInstance().set("username",DemoHelper.getInstance().getCurrentUserName());
                }
                values.put(FileDao.COLUMN_NAME_ID, OaPreference.getInstance().get("username"));
                db.replace(FileDao.TABLE_NAME,null,values);
            }
        }
    }

}
