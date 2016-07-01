package cn.edu.jumy.jumyframework;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/5/17  下午2:57
 */
@EActivity
public class BaseActivity extends AppCompatActivity {

    public static boolean DEBUG = true;
    public Context mContext;
    protected BaseActivity instance;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置为强制竖屏，不使用横屏显示
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addToStack();
        mContext = this;
        instance = this;
    }
    @AfterViews
    public void initStatusBarColor(){
        StatusBarCompat.compat(this,getResources().getColor(R.color.pressed));
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
                Log.w("JumyXx","再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                //结束所有activity并清空堆栈
                Log.w("JumyXx","退出程序");
                AppManager.getInstance().AppExit(this);
            }
        } else if (size > 1) {
//            Log.w("Jumy", "Before finish, the Stack size is :" + AppManager.getStackSize());
            AppManager.getInstance().finishActivity(this);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToPreActivity();
    }


    public void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDebugToast(String message) {
        if (!TextUtils.isEmpty(message) && BuildConfig.DEBUG) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(@StringRes int resId) {
            Toast.makeText(this,getResources().getText(resId), Toast.LENGTH_SHORT).show();
    }
    public void showDebugLoge(CharSequence message){
        if (DEBUG){
            Logger.e(message.toString());
        }
    }
    public void showDebugLogw(CharSequence message){
        if (DEBUG){
            Logger.w(message.toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        instance = null;
        mContext = null;
    }
}
