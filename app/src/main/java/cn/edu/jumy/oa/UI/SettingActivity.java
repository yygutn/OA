package cn.edu.jumy.oa.UI;

import com.hyphenate.chatuidemo.ui.SettingsFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;

/**
 * Created by Jumy on 16/6/17 12:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.em_activity_chat)
public class SettingActivity extends BaseActivity{
    @AfterViews
    void start(){
        getSupportFragmentManager().beginTransaction().add(R.id.container, new SettingsFragment()).commit();
    }
}
