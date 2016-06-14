package cn.edu.jumy.oa;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.tencent.TIMManager;
import com.tencent.TIMUserStatusListener;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.fragment.NotifyFragment_;
import cn.edu.jumy.oa.fragment.TaskFragment;
import cn.edu.jumy.oa.timchat.model.FriendshipInfo;
import cn.edu.jumy.oa.timchat.model.GroupInfo;
import cn.edu.jumy.oa.timchat.model.UserInfo;
import cn.edu.jumy.oa.timchat.ui.ContactFragment;
import cn.edu.jumy.oa.timchat.ui.ConversationFragment;
import cn.edu.jumy.oa.timchat.ui.SettingFragment;
import cn.edu.jumy.oa.timchat.ui.SplashActivity;
import cn.edu.jumy.oa.timchat.ui.customview.DialogActivity;

/**
 * Tab页主界面
 */
public class HomeActivity extends BaseActivity {
    private static final String TAG = HomeActivity.class.getSimpleName();
    private LayoutInflater layoutInflater;
    public FragmentTabHost mTabHost;
    private final Class fragmentArray[] = {NotifyFragment_.class, TaskFragment.class, ConversationFragment.class, ContactFragment.class, SettingFragment.class};
    private int mTitleArray[] = {R.string.home_notify_tab, R.string.home_work_tab, R.string.home_message_tab, R.string.home_contact_tab, R.string.home_me_tab};
    private int mImageViewArray[] = {R.drawable.tab_notify, R.drawable.tab_work, R.drawable.tab_message, R.drawable.tab_person, R.drawable.tab_settings};
    public String mTextViewArray[] = {"notify", "work","message",  "contact", "setting"};
    private ImageView msgUnread;

    private static HomeActivity instance;

    public static HomeActivity getInstance() {
        if (instance == null){
            instance = new HomeActivity();
        }
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        //互踢下线逻辑
        TIMManager.getInstance().setUserStatusListener(new TIMUserStatusListener() {
            @Override
            public void onForceOffline() {
                Log.d(TAG, "receive force offline message");
                Intent intent = new Intent(HomeActivity.this, DialogActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        layoutInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        int fragmentCount = fragmentArray.length;
        for (int i = 0; i < fragmentCount; ++i) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            mTabHost.getTabWidget().setDividerDrawable(null);

        }
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.e(TAG, "onTabChanged: "+tabId );
                int fragmentCount = fragmentArray.length;
                for (int i = 0; i < fragmentCount; i++) {
                    ((TextView) mTabHost.getTabWidget().getChildTabViewAt(i).findViewById(R.id.title)).
                            setTextColor(getResources().getColor(R.color.normal));
                }
                ((TextView) mTabHost.getTabWidget().getChildTabViewAt(mTabHost.getCurrentTab()).findViewById(R.id.title)).
                        setTextColor(getResources().getColor(R.color.pressed));
            }
        });
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.home_tab, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mTitleArray[index]);
        if (index == 0) {
            msgUnread = (ImageView) view.findViewById(R.id.tabUnread);
            title.setTextColor(getResources().getColor(R.color.pressed));
        }
        return view;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void logout() {
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
        Intent intent = new Intent(HomeActivity.this, SplashActivity.class);
        finish();
        startActivity(intent);

    }

    private void logout2Exit(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        MessageEvent.getInstance().clear();
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
    }

    @Override
    protected void onDestroy() {
//        logout2Exit();
        super.onDestroy();
    }

    /**
     * 设置未读tab显示
     */
    public void setMsgUnread(boolean noUnread) {
        msgUnread.setVisibility(noUnread ? View.GONE : View.VISIBLE);
    }


}
