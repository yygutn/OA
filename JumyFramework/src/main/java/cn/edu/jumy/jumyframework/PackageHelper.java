package cn.edu.jumy.jumyframework;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * Created by Jumy on 16/7/5 15:02.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
class PackageHelper {
    private PackageInfo info = null;
    private PackageManager pm;

    public PackageHelper(Context context) {
        pm=context.getPackageManager();
        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public int getLocalVersionCode() {
        return info != null ? info.versionCode : Integer.MAX_VALUE;
    }

    public String getLocalVersionName() {
        return info != null ? info.versionName : "";
    }
    public String getAppName() {
        return info != null ? (String) info.applicationInfo.loadLabel(pm) : "";
    }

    public String getPackageName() {
        return info != null ? info.packageName : "";
    }

    public int getAppIcon() {
        return info != null ? info.applicationInfo.icon
                : android.R.drawable.ic_dialog_info;
    }

}
