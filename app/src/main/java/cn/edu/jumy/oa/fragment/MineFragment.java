package cn.edu.jumy.oa.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chatui.DemoApplication;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.ui.LoginActivity;
import com.hyphenate.easeui.utils.EaseUserUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.CallBack.UserCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.UserResponse;
import cn.edu.jumy.oa.UI.SettingActivity_;
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
        EaseUserUtils.setUserAvatar(mContext, DemoApplication.currentUserName, mSettingAvatar);
        mSettingName.setText(DemoApplication.currentUserName);
        //设置所属单位
        //mSettingDescription.setName("所属单位:"+user.getLevel());
        if (!DemoApplication.currentUserName.equals(EMClient.getInstance().getCurrentUser())) {
            asyncFetchUserInfo(EMClient.getInstance().getCurrentUser());
            DemoApplication.currentUserName = EMClient.getInstance().getCurrentUser();
        }
        OAService.getMyUser(new UserCallback() {
            @Override
            public void onResponse(UserResponse response, int ID) {
                String orgname[] = response.data.orgname.split(",");
                mSettingText.setText(response.data.name);
                String ans = "";
                int len = orgname.length;
                for (int i = 0; i < len; i++) {
                    if (i == 0) {
                        ans = orgname[i];
                    } else {
                        ans += "/" + orgname[i];
                    }
                }
                mSettingDescription.setName("所属单位:" + ans);
            }
        });
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
        EaseUserUtils.setUserAvatar(mContext, username, mSettingAvatar);
    }

}
