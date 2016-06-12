package cn.edu.jumy.oa.timchat.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMFriendStatus;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.FriendshipManageView;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;

import cn.edu.jumy.oa.timchat.model.FriendshipInfo;
import cn.edu.jumy.oa.timchat.ui.customview.LineControllerView;
import cn.edu.jumy.oa.timchat.ui.customview.ListPickerDialog;

/**
 * 申请添加好友界面
 */
public class AddFriendActivity extends BaseActivity implements View.OnClickListener, FriendshipManageView {


    private TextView tvName, btnAdd;
    private EditText editRemark, editMessage;
    private LineControllerView idField, groupField;
    private FriendshipManagerPresenter presenter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        tvName = (TextView) findViewById(R.id.name);
        idField = (LineControllerView) findViewById(R.id.id);
        id = getIntent().getStringExtra("id");
        tvName.setText(getIntent().getStringExtra("name"));
        idField.setContent(id);
        groupField = (LineControllerView) findViewById(R.id.group);
        btnAdd = (TextView) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        editMessage = (EditText) findViewById(R.id.editMessage);
        editRemark = (EditText) findViewById(R.id.editNickname);
        presenter = new FriendshipManagerPresenter(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAdd) {
            presenter.addFriend(id, editRemark.getText().toString(), groupField.getContent().equals(getString(R.string.default_group_name))?"":groupField.getContent(), editMessage.getText().toString());
        }else if (view.getId() == R.id.group){
            final String[] groups = FriendshipInfo.getInstance().getGroupsArray();
            for (int i = 0; i < groups.length; ++i) {
                if (groups[i].equals("")) {
                    groups[i] = getString(R.string.default_group_name);
                    break;
                }
            }
            new ListPickerDialog().show(groups, getSupportFragmentManager(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    groupField.setContent(groups[which]);
                }
            });
        }
    }

    /**
     * 添加好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onAddFriend(TIMFriendStatus status) {
        switch (status){
            case TIM_ADD_FRIEND_STATUS_PENDING:
                Toast.makeText(this, getResources().getString(R.string.add_friend_succeed), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_FRIEND_STATUS_SUCC:
                Toast.makeText(this, getResources().getString(R.string.add_friend_added), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                Toast.makeText(this, getResources().getString(R.string.add_friend_refuse_all), Toast.LENGTH_SHORT).show();
                finish();
                break;
            case TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                Toast.makeText(this, getResources().getString(R.string.add_friend_to_blacklist), Toast.LENGTH_SHORT).show();
                finish();
                break;
            default:
                Toast.makeText(this, getResources().getString(R.string.add_friend_error), Toast.LENGTH_SHORT).show();
                break;
        }

    }

    /**
     * 删除好友结果回调
     *
     * @param status 返回状态
     */
    @Override
    public void onDelFriend(TIMFriendStatus status) {

    }

    /**
     * 修改好友分组回调
     *
     * @param status    返回状态
     * @param groupName 分组名
     */
    @Override
    public void onChangeGroup(TIMFriendStatus status, String groupName) {

    }

}
