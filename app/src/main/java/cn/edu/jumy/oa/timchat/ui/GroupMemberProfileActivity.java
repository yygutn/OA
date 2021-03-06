package cn.edu.jumy.oa.timchat.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMValueCallBack;
import cn.edu.jumy.jumyframework.BaseActivity;

import cn.edu.jumy.oa.R;

import java.util.Collections;
import java.util.List;

import cn.edu.jumy.oa.timchat.model.GroupInfo;
import cn.edu.jumy.oa.timchat.model.GroupMemberProfile;
import cn.edu.jumy.oa.timchat.model.UserInfo;
import cn.edu.jumy.oa.timchat.ui.customview.LineControllerView;
import cn.edu.jumy.oa.timchat.ui.customview.ListPickerDialog;
import cn.edu.jumy.oa.timchat.ui.customview.TemplateTitle;

public class GroupMemberProfileActivity extends BaseActivity {

    private String userIdentify, groupIdentify, userCard,groupType;
    private TIMGroupMemberRoleType currentUserRole;
    private GroupMemberProfile profile;
    private LineControllerView setManager;
    private String[] quietingOpt;
    private String[] quietOpt;
    private long[] quietTimeOpt = new long[] {600, 3600, 24*3600};

    private final int CARD_REQ = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member_profile);
        profile = (GroupMemberProfile) getIntent().getSerializableExtra("data");
        userIdentify = profile.getIdentify();
        groupIdentify = getIntent().getStringExtra("groupId");
        groupType = getIntent().getStringExtra("type");
        userCard = profile.getNameCard();


        currentUserRole = GroupInfo.getInstance().getRole(groupIdentify);
        quietingOpt = new String[] {getString(R.string.group_member_quiet_cancel)};
        quietOpt = new String[] {getString(R.string.group_member_quiet_ten_min),
                getString(R.string.group_member_quiet_one_hour),
                getString(R.string.group_member_quiet_one_day),
        };
        TemplateTitle title = (TemplateTitle) findViewById(R.id.GroupMemTitle);
        title.setBackListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBackResult(false);
                finish();
            }
        });
        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(userIdentify);
        TextView tvKick = (TextView) findViewById(R.id.kick);
        tvKick.setVisibility(canManage()&&!groupType.equals(GroupInfo.privateGroup)? View.VISIBLE:View.GONE);
        tvKick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMGroupManager.getInstance().deleteGroupMember(groupIdentify, Collections.singletonList(userIdentify), new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_del_err), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                        Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_del_succ), Toast.LENGTH_SHORT).show();
                        setBackResult(true);
                        finish();
                    }
                });
            }
        });
        setManager = (LineControllerView) findViewById(R.id.manager);
        setManager.setVisibility(currentUserRole == TIMGroupMemberRoleType.Owner && currentUserRole != profile.getRole() &&!groupType.equals(GroupInfo.privateGroup) ? View.VISIBLE : View.GONE);
        setManager.setSwitch(profile.getRole() == TIMGroupMemberRoleType.Admin);
        setManager.setCheckListener(checkListener);
        final LineControllerView setQuiet = (LineControllerView) findViewById(R.id.setQuiet);
        setQuiet.setVisibility(canManage()&&!groupType.equals(GroupInfo.privateGroup) ? View.VISIBLE : View.GONE);
        if (canManage()){
            if (profile.getQuietTime() != 0){
                setQuiet.setContent(getString(R.string.group_member_quiet_ing));
            }
            setQuiet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ListPickerDialog().show(getQuietOption(), getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            TIMGroupManager.getInstance().modifyGroupMemberInfoSetSilence(groupIdentify, userIdentify, getQuietTime(which),
                                    new TIMCallBack() {
                                        @Override
                                        public void onError(int i, String s) {
                                            Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_quiet_err), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onSuccess() {
                                            if (getQuietTime(which) == 0){
                                                setQuiet.setContent("");
                                            }else{
                                                setQuiet.setContent(getString(R.string.group_member_quiet_ing));
                                            }
                                            profile.setQuietTime(getQuietTime(which));
                                        }
                                    });
                        }
                    });
                }
            });
        }
        LineControllerView nameCard = (LineControllerView) findViewById(R.id.groupCard);
        nameCard.setContent(userCard);
        if (UserInfo.getInstance().getId().equals(userIdentify)){
            nameCard.setCanNav(true);
            nameCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditActivity.navToEdit(GroupMemberProfileActivity.this, getResources().getString(R.string.group_member_change_card), userCard, CARD_REQ, new EditActivity.EditInterface() {
                        @Override
                        public void onEdit(String text, TIMCallBack callBack) {
                            TIMGroupManager.getInstance().modifyGroupMemberInfoSetNameCard(groupIdentify, userIdentify, text, callBack);
                        }
                    },20);
                }
            });
        }

    }

    @Override
    public void onBackPressed() {
        setBackResult(false);
        super.onBackPressed();
    }


    private boolean canManage(){
        if ((currentUserRole == TIMGroupMemberRoleType.Owner && profile.getRole() != TIMGroupMemberRoleType.Owner) ||
                (currentUserRole == TIMGroupMemberRoleType.Admin && profile.getRole() == TIMGroupMemberRoleType.Normal)) return true;
        return false;
    }

    private String[] getQuietOption(){
        if (profile.getQuietTime() == 0){
            return quietOpt;
        }else{
            return quietingOpt;
        }
    }

    private long getQuietTime(int which){
        if (profile.getQuietTime() == 0){
            return quietTimeOpt[which];
        }
        return 0;
    }

    private void setBackResult(boolean isKick){
        Intent mIntent = new Intent();
        mIntent.putExtra("data", profile);
        mIntent.putExtra("isKick", isKick);
        setResult(RESULT_OK, mIntent);
    }

    private final CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
            TIMGroupManager.getInstance().modifyGroupMemberInfoSetRole(groupIdentify, userIdentify,
                    isChecked ? TIMGroupMemberRoleType.Admin : TIMGroupMemberRoleType.Normal,
                    new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            switch (i){
                                case 10004:
                                    Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_type_err), Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_err), Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            //防止循环调用
                            setManager.setCheckListener(null);
                            setManager.setSwitch(!isChecked);
                            setManager.setCheckListener(checkListener);
                        }

                        @Override
                        public void onSuccess() {
                            Toast.makeText(GroupMemberProfileActivity.this, getString(R.string.group_member_manage_set_succ), Toast.LENGTH_SHORT).show();
                            profile.setRoleType(isChecked ? TIMGroupMemberRoleType.Admin : TIMGroupMemberRoleType.Normal);
                        }
                    });
        }
    };


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CARD_REQ){
            if (resultCode == RESULT_OK){
                LineControllerView nameCard = (LineControllerView) findViewById(R.id.groupCard);
                nameCard.setContent(data.getStringExtra(EditActivity.RETURN_EXTRA));
                profile.setName(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }
}
