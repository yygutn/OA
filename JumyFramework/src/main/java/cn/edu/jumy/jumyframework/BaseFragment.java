package cn.edu.jumy.jumyframework;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

/**
 * Created by Jumy on 16/5/25 15:48.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
public class BaseFragment extends Fragment {
    protected Context mContext;
    private static boolean DEBUG = BaseActivity.DEBUG;

    public static void showDebugLoge(CharSequence message) {
        if (DEBUG) {
            Logger.e(message.toString());
        }
    }

    public static void showDebugLogw(CharSequence message) {
        if (DEBUG) {
            Logger.w(message.toString());
        }
    }
    public static void showDebugLogv(CharSequence message) {
        if (DEBUG) {
            Logger.v(message.toString());
        }
    }
    public static void showDebugLogd(CharSequence message) {
        if (DEBUG) {
            Logger.d(message.toString());
        }
    }
    public static void showDebugLogd(String tag,CharSequence message) {
        if (DEBUG) {
            Logger.t(tag).d(message.toString());
        }
    }

    public static void showDebugException(Exception e){
        if (DEBUG){
            e.printStackTrace();
        }
    }

    public void showToast(CharSequence message) {
        Toast.makeText(getActivity(), message.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        StatusBarCompat.compat(getActivity(), getResources().getColor(R.color.pressed));
    }
}
