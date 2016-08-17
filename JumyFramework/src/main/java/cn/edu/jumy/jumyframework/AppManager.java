package cn.edu.jumy.jumyframework;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.Stack;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/5/16  下午5:37
 */
public class AppManager {
    private static final String TAG = AppManager.class.getSimpleName();
    private static CopyOnWriteArrayList<BaseActivity> mActivityStack;
    private static AppManager instance;


    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public static int getStackSize() {
        return mActivityStack.size();
    }

    public void init() {
        mActivityStack = new CopyOnWriteArrayList<>();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(BaseActivity activity) {
        if (mActivityStack == null) {
            mActivityStack = new CopyOnWriteArrayList<>();
        }
        mActivityStack.add(activity);
        Logger.t(TAG).w("add " + activity.getLocalClassName() + "\n" + "current size is : " + mActivityStack.size());
        logStackInfo();
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        return mActivityStack.get(mActivityStack.size() - 1);
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurActivity() {
        Activity activity = mActivityStack.remove(mActivityStack.size() - 1);
        activity.finish();
        logStackInfo();
    }

    private void logStackInfo() {
        String message = "StackSize = " + mActivityStack.size() + "\n";
        for (Activity ac : mActivityStack) {
            message += ac.getClass().getSimpleName() + "\n";
        }
        Logger.t(TAG).w(message);
    }

    /**
     * 结束除了当前Activity（堆栈中最后一个压入的）所有的Activity
     */
    public void finishAllBesideTop() {
        BaseActivity activity = mActivityStack.get(mActivityStack.size() - 1);
        if (activity == null) {
            return;
        }
        String name = activity.getClass().getSimpleName();
        for (BaseActivity activities : mActivityStack) {
            if (!activities.getClass().getSimpleName().equals(name)) {
                finishActivity(activities);
            }
        }
    }

    public void back2Level2() {
        while (mActivityStack.size() > 2) {
            finishActivity(mActivityStack.remove(mActivityStack.size() - 1));
        }
    }

    public void back2Level1() {
        while (mActivityStack.size() > 1) {
            finishActivity(mActivityStack.remove(mActivityStack.size() - 1));
        }
    }

    /**
     * 移除Activity
     *
     * @param baseActivity
     */
    public void removeActivity(BaseActivity baseActivity) {
        mActivityStack.remove(baseActivity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(BaseActivity activity) {
        String message = "";
        message += "Finishing " + activity.getClass().getSimpleName() + "\n";
        message += "Before finish, the Stack size is :" + AppManager.getStackSize() + "\n";
        finishActivity(activity.getClass());
        activity.finish();
        message += ("After finished, the Stack size is :" + AppManager.getStackSize()) + "\n";
        showDebugLog(message);
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            for (BaseActivity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    removeActivity(activity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        while (!mActivityStack.isEmpty()) {
            mActivityStack.remove(mActivityStack.size() - 1).finish();
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
//            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDebugLog(CharSequence message) {
        if (BaseActivity.DEBUG) {
            Logger.t(TAG).w(message.toString());
        }
    }
}
