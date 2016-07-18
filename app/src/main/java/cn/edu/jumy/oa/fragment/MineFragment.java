package cn.edu.jumy.oa.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoApplication;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.ui.LoginActivity;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseUserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.OaPreference;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.SettingActivity_;
import cn.edu.jumy.oa.bean.User;
import cn.edu.jumy.oa.widget.customview.CircleImageView;
import cn.edu.jumy.oa.widget.customview.LineControllerView;

/**
 * Created by Jumy on 16/6/17 11:43.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_setting)
public class MineFragment extends BaseFragment {

    @ViewById(R.id.setting_avatar)
    protected CircleImageView mSettingAvatar;
    @ViewById(R.id.setting_name)
    protected TextView mSettingName;
    @ViewById(R.id.setting_text)
    protected TextView mSettingText;
    @ViewById(R.id.setting_description)
    protected LineControllerView mSettingDescription;
    @ViewById(R.id.setting_nav)
    protected LineControllerView mSettingNav;
    @ViewById(R.id.setting_logout)
    protected TextView mSettingLogout;


    @AfterViews
    void start() {
        try {
            mContext = getActivity();
            EaseUserUtils.setUserAvatar(mContext, DemoApplication.currentUserName, mSettingAvatar);
            EaseUserUtils.setUserNick(DemoApplication.currentUserName, mSettingText);
            mSettingName.setText(DemoApplication.currentUserName);
            //设置所属单位
            //mSettingDescription.setName("所属单位:"+user.getLevel());
            if (!DemoApplication.currentUserName.equals(EMClient.getInstance().getCurrentUser())) {
                asyncFetchUserInfo(EMClient.getInstance().getCurrentUser());
                DemoApplication.currentUserName = EMClient.getInstance().getCurrentUser();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click({R.id.setting_nav, R.id.setting_logout})
    void click(View view) {
        switch (view.getId()) {
            case R.id.setting_nav: {
                // TODO: 16/6/17 跳转到设置中心
                SettingActivity_.intent(mContext).start();
                break;
            }
            case R.id.setting_logout: {
                logout();
                break;
            }
        }
    }

    void logout() {
        final ProgressDialog pd = new ProgressDialog(getActivity());
        String st = getResources().getString(R.string.Are_logged_out);
        pd.setMessage(st);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        DemoHelper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        // 重新显示登陆页面
                        OaPreference.setLoginStatus(false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        AppManager.getInstance().finishCurActivity();

                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        pd.dismiss();
                        Toast.makeText(getActivity(), "unbind devicetokens failed", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }

    public void asyncFetchUserInfo(String username) {
        DemoHelper.getInstance().getUserProfileManager().asyncGetUserInfo(username, new EMValueCallBack<EaseUser>() {

            @Override
            public void onSuccess(EaseUser user) {
                if (user != null) {
                    DemoHelper.getInstance().saveContact(user);
                    User.saveUserInfo(null, user);
                    mSettingText.setText(user.getNick());
                    if (!TextUtils.isEmpty(user.getAvatar())) {
                        Glide.with(mContext).load(user.getAvatar()).placeholder(R.drawable.em_default_avatar).into(mSettingAvatar);
                    } else {
                        Glide.with(mContext).load(R.drawable.em_default_avatar).into(mSettingAvatar);
                    }
                }
            }

            @Override
            public void onError(int error, String errorMsg) {
            }
        });
    }

}
