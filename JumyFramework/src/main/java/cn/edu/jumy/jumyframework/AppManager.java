package cn.edu.jumy.jumyframework;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.util.Stack;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/5/16  下午5:37
 */
public class AppManager {
    private static final String TAG = AppManager.class.getSimpleName();
    private static Stack<Activity> mActivityStack;
    private static Activity mCurInstance = null;
    private static AppManager instance;


    public static AppManager getInstance() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    public static Context getCurActivityInstance() {
        return mCurInstance.getBaseContext();
    }

    public static int getStackSize() {
        return mActivityStack.size();
    }

    public void init() {
        mActivityStack = new Stack<>();
    }

    public static void addActivities(Activity activity) {
        AppManager.getInstance().addActivity(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mCurInstance = activity;
        mActivityStack.push(mCurInstance);
        Logger.t(TAG).w("add " + activity.getLocalClassName() + "\n" + "current size is : " + mActivityStack.size());
        logStackInfo();
    }

    /**
     * 添加单例Activity到堆栈
     */
    public void addSingleActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        if (!mActivityStack.contains(activity)) {
            mActivityStack.push(activity);
            mCurInstance = activity;
            Log.e("AppManager", "add Single" + activity.getLocalClassName() + "\n" + "current size is : " + mActivityStack.size());
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getCurrentActivity() {
        return mActivityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishCurActivity() {
        Activity activity = mActivityStack.pop();
        activity.finish();
        activity = null;
        mCurInstance = mActivityStack.size() >= 1 ? mActivityStack.lastElement():null;
        logStackInfo();
    }

    private void logStackInfo() {
        String message = "StackSize = " + mActivityStack.size() + "\n";
        for(Activity ac : mActivityStack){
            message += ac.getClass().getSimpleName() + "\n";
        }
        Logger.t(TAG).w(message);
    }

    /**
     * 结束除了当前Activity（堆栈中最后一个压入的）所有的Activity
     */
    public void finishAllBesideTop() {
        Activity activity = mActivityStack.lastElement();
        if (activity == null){
            return;
        }
        String name = activity.getClass().getSimpleName();
        for (Activity activities : mActivityStack){
            if (!activities.getClass().getSimpleName().equals(name)){
                finishActivity(activities);
            }
        }
    }

    /**
     * 移除Activity
     *
     * @param baseActivity
     */
    public void removeActivity(Activity baseActivity) {
        mActivityStack.remove(baseActivity);
    }


    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        showDebugLog("Finishing " + activity.getClass().getSimpleName());
        showDebugLog("Before finish, the Stack size is :" + AppManager.getStackSize());
        if (activity != null) {
            finishActivity(activity.getClass());
            activity.finish();
            activity = null;
        }
        showDebugLog("After finished, the Stack size is :" + AppManager.getStackSize());
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        try {
            mCurInstance = null;
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    removeActivity(activity);
                }
            }
            mCurInstance = mActivityStack.size() >= 1 ? mActivityStack.lastElement():null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        mCurInstance = null;
        while (!mActivityStack.empty()) {
            mActivityStack.pop().finish();
        }
        mActivityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
//            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
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
