package cn.edu.jumy.oa.timchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupAddOpt;
import com.tencent.TIMGroupDetailInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.qcloud.presentation.presenter.GroupInfoPresenter;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.GroupInfoView;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import cn.edu.jumy.oa.R;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.oa.timchat.model.GroupInfo;
import cn.edu.jumy.oa.timchat.model.UserInfo;
import cn.edu.jumy.oa.timchat.ui.customview.LineControllerView;
import cn.edu.jumy.oa.timchat.ui.customview.ListPickerDialog;

public class GroupProfileActivity extends BaseActivity implements GroupInfoView, View.OnClickListener {

    private final String TAG = "GroupProfileActivity";

    private String identify,type;
    private GroupInfoPresenter groupInfoPresenter;
    private boolean isInGroup;
    private boolean isGroupOwner;
    private final int REQ_CHANGE_NAME = 100, REQ_CHANGE_INTRO = 200;
    private TIMGroupMemberRoleType roleType = TIMGroupMemberRoleType.NotMember;
    private Map<String, TIMGroupAddOpt> allowTypeContent;
    private LineControllerView name,intro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat_setting);
        identify = getIntent().getStringExtra("identify");
        isInGroup = GroupInfo.getInstance().isInGroup(identify);
        groupInfoPresenter = new GroupInfoPresenter(this, Collections.singletonList(identify), isInGroup);
        groupInfoPresenter.getGroupDetailInfo();
        name = (LineControllerView) findViewById(R.id.nameText);
        intro = (LineControllerView) findViewById(R.id.groupIntro);
        LinearLayout controlInGroup = (LinearLayout) findViewById(R.id.controlInGroup);
        controlInGroup.setVisibility(isInGroup? View.VISIBLE:View.GONE);
        TextView controlOutGroup = (TextView) findViewById(R.id.controlOutGroup);
        controlOutGroup.setVisibility(isInGroup ? View.GONE : View.VISIBLE);
    }

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        TIMGroupDetailInfo info = groupInfos.get(0);
        isGroupOwner = info.getGroupOwner().equals(UserInfo.getInstance().getId());
        roleType = GroupInfo.getInstance().getRole(identify);
        type = info.getGroupType();
        LineControllerView member = (LineControllerView) findViewById(R.id.member);
        if (isInGroup){
            member.setContent(String.valueOf(info.getMemberNum()));
            member.setOnClickListener(this);
        }else{
            member.setVisibility(View.GONE);
        }
        name.setContent(info.getGroupName());
        LineControllerView id = (LineControllerView) findViewById(R.id.idText);
        id.setContent(info.getGroupId());

        intro.setContent(info.getGroupIntroduction());
        LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
        switch (info.getGroupAddOpt()){
            case TIM_GROUP_ADD_AUTH:
                opt.setContent(getString(R.string.chat_setting_group_auth));
                break;
            case TIM_GROUP_ADD_ANY:
                opt.setContent(getString(R.string.chat_setting_group_all_accept));
                break;
            case TIM_GROUP_ADD_FORBID:
                opt.setContent(getString(R.string.chat_setting_group_all_reject));
                break;
        }
        if (isManager()){
            opt.setCanNav(true);
            opt.setOnClickListener(this);
            allowTypeContent = new HashMap<>();
            allowTypeContent.put(getString(R.string.chat_setting_group_auth), TIMGroupAddOpt.TIM_GROUP_ADD_AUTH);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_accept), TIMGroupAddOpt.TIM_GROUP_ADD_ANY);
            allowTypeContent.put(getString(R.string.chat_setting_group_all_reject), TIMGroupAddOpt.TIM_GROUP_ADD_FORBID);
            name.setCanNav(true);
            name.setOnClickListener(this);
            intro.setCanNav(true);
            intro.setOnClickListener(this);
        }
        TextView btnDel = (TextView) findViewById(R.id.btnDel);
        btnDel.setText(isGroupOwner ? getString(R.string.chat_setting_dismiss) : getString(R.string.chat_setting_quit));

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnChat) {
            ChatActivity.navToChat(this, identify, TIMConversationType.Group);

        } else if (i == R.id.btnDel) {
            if (isGroupOwner) {
                GroupManagerPresenter.dismissGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onError code" + i + " msg " + s);
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_dismiss_succ), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } else {
                GroupManagerPresenter.quitGroup(identify, new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.i(TAG, "onError code" + i + " msg " + s);
                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_quit_succ), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }

        } else if (i == R.id.controlOutGroup) {
            Intent intent = new Intent(this, ApplyGroupActivity.class);
            intent.putExtra("identify", identify);
            startActivity(intent);

        } else if (i == R.id.member) {
            Intent intentGroupMem = new Intent(this, GroupMemberActivity.class);
            intentGroupMem.putExtra("id", identify);
            intentGroupMem.putExtra("type", type);
            startActivity(intentGroupMem);

        } else if (i == R.id.addOpt) {
            final String[] stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);
            new ListPickerDialog().show(stringList, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    TIMGroupManager.getInstance().modifyGroupAddOpt(identify, allowTypeContent.get(stringList[which]), new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(GroupProfileActivity.this, getString(R.string.chat_setting_change_err), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            LineControllerView opt = (LineControllerView) findViewById(R.id.addOpt);
                            opt.setContent(stringList[which]);
                        }
                    });
                }
            });

        } else if (i == R.id.nameText) {
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_name), name.getContent(), REQ_CHANGE_NAME, new EditActivity.EditInterface() {
                        @Override
                        public void onEdit(final String text, TIMCallBack callBack) {
                            TIMGroupManager.getInstance().modifyGroupName(identify, text, callBack);
                        }
                    }, 20);

                }
            });

        } else if (i == R.id.groupIntro) {
            intro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditActivity.navToEdit(GroupProfileActivity.this, getString(R.string.chat_setting_change_group_intro), intro.getContent(), REQ_CHANGE_INTRO, new EditActivity.EditInterface() {
                        @Override
                        public void onEdit(final String text, TIMCallBack callBack) {
                            TIMGroupManager.getInstance().modifyGroupIntroduction(identify, text, callBack);
                        }
                    }, 20);

                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHANGE_NAME){
            if (resultCode == RESULT_OK){
                name.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }else if (requestCode == REQ_CHANGE_INTRO){
            if (resultCode == RESULT_OK){
                intro.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }

    private boolean isManager(){
        return roleType == TIMGroupMemberRoleType.Owner || roleType == TIMGroupMemberRoleType.Admin;
    }
}
