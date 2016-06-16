package com.hyphenate.chatuidemo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chatuidemo.DemoHelper;
import com.hyphenate.chatuidemo.R;

import cn.edu.jumy.jumyframework.AppManager;

//import com.easemob.redpacketsdk.RPCallback;
//import com.easemob.redpacketsdk.RedPacket;

/**
 * 开屏页
 *
 */
public class SplashActivity extends BaseActivity {
	private FrameLayout rootLayout;
	
	private static final int sleepTime = 2000;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		try {
			setContentView(R.layout.em_activity_splash);
			rootLayout = (FrameLayout) findViewById(R.id.splash_root);
			AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
			animation.setDuration(2000);
			rootLayout.startAnimation(animation);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();

		try {
			new Thread(new Runnable() {
                public void run() {
                    if (DemoHelper.getInstance().isLoggedIn()) {
                        // ** 免登陆情况 加载所有本地群和会话
                        //不是必须的，不加sdk也会自动异步去加载(不会重复加载)；
                        //加上的话保证进了主页面会话和群组都已经load完毕
                        long start = System.currentTimeMillis();
                        EMClient.getInstance().groupManager().loadAllGroups();
                        EMClient.getInstance().chatManager().loadAllConversations();

    //					RedPacket.getInstance().initRPToken(DemoHelper.getInstance().getCurrentUsernName(), DemoHelper.getInstance().getCurrentUsernName(), EMClient.getInstance().getChatConfig().getAccessToken(), new RPCallback() {
    //						@Override
    //						public void onSuccess() {
    //
    //						}
    //
    //						@Override
    //						public void onError(String s, String s1) {
    //
    //						}
    //					});
                        long costTime = System.currentTimeMillis() - start;
                        //等待sleeptime时长
                        if (sleepTime - costTime > 0) {
                            try {
                                Thread.sleep(sleepTime - costTime);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //进入主页面
                        startActivity(new Intent(SplashActivity.this, cn.edu.jumy.oa.MainActivity.class));
						AppManager.getInstance().finishCurActivity();
                    }else {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                        }
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
						AppManager.getInstance().finishCurActivity();
                    }
                }
            }).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获取当前应用程序的版本号
	 */
	private String getVersion() {
	    return EMClient.getInstance().getChatConfig().getVersion();
	}
}
