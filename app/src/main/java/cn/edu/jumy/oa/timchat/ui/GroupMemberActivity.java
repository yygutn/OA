package cn.edu.jumy.oa.timchat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import cn.edu.jumy.oa.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.timchat.adapters.ProfileSummaryAdapter;
import cn.edu.jumy.oa.timchat.model.GroupMemberProfile;
import cn.edu.jumy.oa.timchat.model.ProfileSummary;

public class GroupMemberActivity extends BaseActivity implements TIMValueCallBack<List<TIMGroupMemberInfo>> {

    ProfileSummaryAdapter adapter;
    List<ProfileSummary> list = new ArrayList<>();
    ListView listView;
    String groupId,type;
    private final int MEM_REQ = 100;
    private int memIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        groupId = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        listView = (ListView) findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        TIMGroupManager.getInstance().getGroupMembers(groupId, this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                memIndex = position;
                Intent intent = new Intent(GroupMemberActivity.this, GroupMemberProfileActivity.class);
                GroupMemberProfile profile = (GroupMemberProfile) list.get(position);
                intent.putExtra("data", profile);
                intent.putExtra("groupId", groupId);
                intent.putExtra("type",type);
                startActivityForResult(intent, MEM_REQ);
            }
        });
    }

    @Override
    public void onError(int i, String s) {

    }

    @Override
    public void onSuccess(List<TIMGroupMemberInfo> timGroupMemberInfos) {
        list.clear();
        if (timGroupMemberInfos == null) return;
        for (TIMGroupMemberInfo item : timGroupMemberInfos){
            list.add(new GroupMemberProfile(item));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (MEM_REQ == requestCode) {
            if (resultCode == RESULT_OK){
                boolean isKick = data.getBooleanExtra("isKick", false);
                if (isKick){
                    list.remove(memIndex);
                    adapter.notifyDataSetChanged();
                }else{
                    GroupMemberProfile profile = (GroupMemberProfile) data.getSerializableExtra("data");
                    if (list.get(memIndex).getIdentify().equals(profile.getIdentify())){
                        GroupMemberProfile mMemberProfile = (GroupMemberProfile) list.get(memIndex);
                        mMemberProfile.setRoleType(profile.getRole());
                        mMemberProfile.setQuietTime(profile.getQuietTime());
                        mMemberProfile.setName(profile.getNameCard());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }




}
