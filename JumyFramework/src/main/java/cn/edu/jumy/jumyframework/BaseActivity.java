package cn.edu.jumy.jumyframework;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.io.File;
import java.lang.ref.WeakReference;


/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/5/17  下午2:57
 */
@EActivity
public class BaseActivity extends AppCompatActivity {

    public static boolean DEBUG = true;
    public Context mContext;
    private WeakReference<BaseActivity> instance;

    protected BaseActivity getInstance() {
        return instance.get();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为强制竖屏，不使用横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addToStack();
        mContext = this;
        instance = new WeakReference<BaseActivity>(this);
    }

    @AfterViews
    public void initStatusBarColor() {
        StatusBarCompat.compat(this, getResources().getColor(R.color.pressed));
    }

    protected void addToStack() {
        AppManager.getInstance().addActivity(this);
    }

    /**
     * 返回事件
     */
    private static long exitTime = 0;

    public void backToPreActivity() {
        int size = AppManager.getStackSize();
        Activity mCurPage = AppManager.getInstance().getCurrentActivity();
        if (size <= 1) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                showToast("再按一次退出程序");
                Log.w("JumyXx", "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                //结束所有activity并清空堆栈
                Log.w("JumyXx", "退出程序");
                deleteFilesByDirectory(getExternalCacheDir());
                AppManager.getInstance().AppExit(this);
            }
        } else if (size > 1) {
//            Log.w("Jumy", "Before finish, the Stack size is :" + AppManager.getStackSize());
            AppManager.getInstance().finishActivity(this);
        }

    }

    @Override
    public void onBackPressed() {
        backToPreActivity();
    }

    public void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
    public void showToast(String message,int duration) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(mContext, message, duration).show();
        }
    }

    public void showDebugToast(String message) {
        if (!TextUtils.isEmpty(message) && BuildConfig.DEBUG) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(this, getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }

    public static void showDebugLoge(CharSequence message) {
        if (DEBUG) {
            Logger.e(message.toString());
        }
    }

    public static void showDebugLoge(String tag, CharSequence message) {
        if (DEBUG) {
            Logger.t(tag).e(message.toString());
        }
    }

    public static void showDebugLogw(CharSequence message) {
        if (DEBUG) {
            Logger.w(message.toString());
        }
    }

    public static void showDebugLogw(String tag, CharSequence message) {
        if (DEBUG) {
            Logger.t(tag).w(message.toString());
        }
    }

    public static void showDebugLogd(String tag, CharSequence message) {
        if (DEBUG) {
            Logger.t(tag).d(message.toString());
        }
    }

    public static void showDebugException(Exception e) {
        if (DEBUG) {
            e.printStackTrace();
        }
    }

    public static void showDebugLogd(CharSequence message) {
        if (DEBUG) {
            Logger.d(message.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (instance != null) {
            instance = null;
        }
        if (mContext != null) {
            mContext = null;
        }
    }

    @Override
    public void finish() {
        super.finish();
    }

    /**
     * 删除方法 这里只会删除某个文件夹下的文件，
     * 如果传入的directory是个文件，将不做处理
     *
     * @param directory 文件夹
     */
    private static void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
}
