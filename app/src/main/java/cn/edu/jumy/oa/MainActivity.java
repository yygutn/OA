package cn.edu.jumy.oa;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chatui.Constant;
import com.hyphenate.chatui.DemoApplication;
import com.hyphenate.chatui.DemoHelper;
import com.hyphenate.chatui.db.InviteMessageDao;
import com.hyphenate.chatui.db.UserDao;
import com.hyphenate.chatui.domain.InviteMessage;
import com.hyphenate.chatui.runtimepermissions.PermissionsManager;
import com.hyphenate.chatui.runtimepermissions.PermissionsResultAction;
import com.hyphenate.chatui.ui.ChatActivity;
import com.hyphenate.chatui.ui.ContactListFragment;
import com.hyphenate.chatui.ui.ConversationListFragment;
import com.hyphenate.chatui.ui.GroupsActivity;
import com.hyphenate.chatui.ui.LoginActivity;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.EMLog;

import java.util.List;

import cn.edu.jumy.jumyframework.AppManager;
import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.BroadCastReceiver.AlarmBroadCastReceiver;
import cn.edu.jumy.oa.Utils.NotifyUtils;
import cn.edu.jumy.oa.fragment.MineFragment_;
import cn.edu.jumy.oa.fragment.NotifyFragment_;
import cn.edu.jumy.oa.fragment.TaskFragment;
import cn.edu.jumy.oa.widget.FragmentTabHost;

/**
 * Tab页主界面
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private LayoutInflater layoutInflater;
    public FragmentTabHost mTabHost;
    private final Class fragmentArray[] = {NotifyFragment_.class, TaskFragment.class, ConversationListFragment.class, ContactListFragment.class, MineFragment_.class};
    private int mTitleArray[] = {R.string.home_notify_tab, R.string.home_work_tab, R.string.home_message_tab, R.string.home_contact_tab, R.string.home_me_tab};
    private int mImageViewArray[] = {R.drawable.tab_notify, R.drawable.tab_work, R.drawable.tab_message, R.drawable.tab_person, R.drawable.tab_settings};
    public String mTextViewArray[] = {"notify", "work", "message", "contact", "setting"};

    private static MainActivity instance;

    public static MainActivity getMainInstance() {
        if (instance == null) {
            instance = new MainActivity();
        }
        return instance;
    }

    // 未读消息textview
    private TextView unreadLabel;
    // 未读通讯录textview
    private TextView unreadAddressLabel;
    // 账号在别处登录
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;


    private android.app.AlertDialog.Builder conflictBuilder;
    private android.app.AlertDialog.Builder accountRemovedBuilder;
    private boolean isConflictDialogShow;
    private boolean isAccountRemovedDialogShow;
    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private AlarmBroadCastReceiver alarmBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().finishAllBesideTop();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                //intent.setAction(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            }
        }

        if (savedInstanceState != null && savedInstanceState.getBoolean(Constant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            DemoHelper.getInstance().logout(false, null);
            AppManager.getInstance().finishCurActivity();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean("isConflict", false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            AppManager.getInstance().finishCurActivity();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_home);

        DemoApplication.currentUserName = EMClient.getInstance().getCurrentUser();


        try {
            //6.0运行时权限处理，target api设成23时，demo这里做的比较简单，直接请求所有需要的运行时权限
            requestPermissions();
            initView();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (getIntent().getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (getIntent().getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }

        inviteMessageDao = new InviteMessageDao(this);
        userDao = new UserDao(this);

        //注册local广播接收者，用于接收demohelper中发出的群组联系人的变动通知
        registerBroadcastReceiver();

        initMeetBroadCastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());

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
        mTabHost.onTabChanged(mTextViewArray[0]);
    }

    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.home_tab, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(mImageViewArray[index]);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(mTitleArray[index]);
        if (index == 2) {
            unreadLabel = (TextView) view.findViewById(R.id.tabUnread);
//            title.setTextColor(getResources().getColor(R.color.pressed));
        }
        if (index == 3) {
            unreadAddressLabel = (TextView) view.findViewById(R.id.tabUnread);
//            title.setTextColor(getResources().getColor(R.color.pressed));
        }
        return view;
    }

    @TargetApi(23)
    private void requestPermissions() {
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this, new PermissionsResultAction() {
            @Override
            public void onGranted() {
//				Toast.makeText(MainActivity.this, "All permissions have been granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(String permission) {
//                Toast.makeText(MainActivity.this, "Permission " + permission + " has been denied", Toast.LENGTH_SHORT).show();
            }
        });
    }


    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // 提示新消息
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            showDebugLogd(TAG, "收到透传消息");
            for (EMMessage message : messages) {
                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                final String action = cmdMsgBody.action();//获取自定义action
                showDebugLogd(TAG, "action:" + action);
                NotifyUtils.sendNotifyBroadCast(mContext,action);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                updateUnreadLabel();
                if (mTabHost.getCurrentTab() == 2) {
                    // 当前页面如果为聊天历史页面，刷新此页面
                    if (mTabHost.mTabs.get(2) != null) {
                        ((ConversationListFragment) mTabHost.mTabs.get(2).fragment).refresh();
                    }
                }
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                updateUnreadAddressLabel();
                switch (mTabHost.getCurrentTab()) {
                    case 2:
                        // 当前页面如果为聊天历史页面，刷新此页面
                        if (mTabHost.mTabs.get(2) != null) {
                            ((ConversationListFragment) mTabHost.mTabs.get(2).fragment).refresh();
                        }
                        break;
                    case 3:
                        if (mTabHost.mTabs.get(3) != null) {
                            ((ContactListFragment) mTabHost.mTabs.get(3).fragment).refresh();
                        }
                        break;
                }
                String action = intent.getAction();
                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
                        GroupsActivity.instance.onResume();
                    }
                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (ChatActivity.activityInstance != null && ChatActivity.activityInstance.toChatUsername != null &&
                            username.equals(ChatActivity.activityInstance.toChatUsername)) {
                        String st10 = getResources().getString(com.hyphenate.chatui.R.string.have_you_removed);
                        Toast.makeText(MainActivity.this, ChatActivity.activityInstance.getToChatUsername() + st10, Toast.LENGTH_LONG)
                                .show();
                        ChatActivity.activityInstance.backToPreActivity();
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    private void unregisterBroadcastReceiver() {
        broadcastManager.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (conflictBuilder != null) {
            conflictBuilder.create().dismiss();
            conflictBuilder = null;
        }
        unregisterBroadcastReceiver();
        try {
            if (alarmBroadCastReceiver != null) {
                unregisterReceiver(alarmBroadCastReceiver);
            }
        } catch (Exception e) {
            showDebugException(e);
        }
    }

    /**
     * 刷新未读消息数
     */
    public void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        if (count > 0) {
            unreadLabel.setText(String.valueOf(count));
            unreadLabel.setVisibility(View.VISIBLE);
        } else {
            unreadLabel.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 刷新申请与通知消息数
     */
    public void updateUnreadAddressLabel() {
        runOnUiThread(new Runnable() {
            public void run() {
                int count = getUnreadAddressCountTotal();
                if (count > 0) {
//					unreadAddressLabel.setText(String.valueOf(count));
                    unreadAddressLabel.setVisibility(View.VISIBLE);
                } else {
                    unreadAddressLabel.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    /**
     * 获取未读申请与通知消息
     *
     * @return int
     */
    public int getUnreadAddressCountTotal() {
        int unreadAddressCountTotal;
        unreadAddressCountTotal = inviteMessageDao.getUnreadMessagesCount();
        return unreadAddressCountTotal;
    }

    /**
     * 获取未读消息数
     *
     * @return int
     */
    public int getUnreadMsgCountTotal() {
        int unreadMsgCountTotal;
        int chatRoomUnreadMsgCount = 0;
        unreadMsgCountTotal = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        for (EMConversation conversation : EMClient.getInstance().chatManager().getAllConversations().values()) {
            if (conversation.getType() == EMConversation.EMConversationType.ChatRoom)
                chatRoomUnreadMsgCount = chatRoomUnreadMsgCount + conversation.getUnreadMsgCount();
        }
        return unreadMsgCountTotal - chatRoomUnreadMsgCount;
    }

    private InviteMessageDao inviteMessageDao;
    private UserDao userDao;


    /**
     * 保存提示新消息
     *
     * @param msg void
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        saveInviteMsg(msg);
        // 提示有新消息
        DemoHelper.getInstance().getNotifier().viberateAndPlayTone(null);

        // 刷新bottom bar消息未读数
        updateUnreadAddressLabel();
        // 刷新好友页面ui
        if (mTabHost.getCurrentTab() == 3)
            ((ContactListFragment) mTabHost.mTabs.get(3).fragment).refresh();
    }

    /**
     * 保存邀请等msg
     *
     * @param msg void
     */
    private void saveInviteMsg(InviteMessage msg) {
        // 保存msg
        inviteMessageDao.saveMessage(msg);
        //保存未读数，这里没有精确计算
        inviteMessageDao.saveUnreadMessageCount(1);
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
            updateUnreadAddressLabel();
        }

        // unregister this event listener when this activity enters the
        // background
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);

        EMClient.getInstance().chatManager().addMessageListener(messageListener);

        System.gc();
    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);

        super.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("isConflict", isConflict);
        outState.putBoolean(Constant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    /**
     * 显示帐号在别处登录dialog
     */
    private void showConflictDialog() {
        isConflictDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st = getResources().getString(com.hyphenate.chatui.R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (conflictBuilder == null)
                    conflictBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                conflictBuilder.setTitle(st);
                conflictBuilder.setMessage(com.hyphenate.chatui.R.string.connect_conflict);
                conflictBuilder.setPositiveButton(com.hyphenate.chatui.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        conflictBuilder = null;
                        AppManager.getInstance().finishActivity(getInstance());
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                conflictBuilder.setCancelable(false);
                conflictBuilder.create().show();
                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }

        }

    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        isAccountRemovedDialogShow = true;
        DemoHelper.getInstance().logout(false, null);
        String st5 = getResources().getString(com.hyphenate.chatui.R.string.Remove_the_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (accountRemovedBuilder == null)
                    accountRemovedBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                accountRemovedBuilder.setTitle(st5);
                accountRemovedBuilder.setMessage(com.hyphenate.chatui.R.string.em_user_remove);
                accountRemovedBuilder.setPositiveButton(com.hyphenate.chatui.R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        accountRemovedBuilder = null;
                        AppManager.getInstance().finishCurActivity();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                accountRemovedBuilder.setCancelable(false);
                accountRemovedBuilder.create().show();
                isCurrentAccountRemoved = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color userRemovedBuilder error" + e.getMessage());
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false) && !isConflictDialogShow) {
            showConflictDialog();
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false) && !isAccountRemovedDialogShow) {
            showAccountRemovedDialog();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //getMenuInflater().inflate(R.menu.context_tab_contact, menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private void initMeetBroadCastReceiver() {
        if (alarmBroadCastReceiver != null){
            return;
        }
        try {
            alarmBroadCastReceiver = new AlarmBroadCastReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(AlarmBroadCastReceiver.MEET_ALARM);

            registerReceiver(alarmBroadCastReceiver, filter);

            sendBroadcast(new Intent(AlarmBroadCastReceiver.MEET_ALARM));
        } catch (Exception e) {
            showDebugException(e);
        }
    }
}
