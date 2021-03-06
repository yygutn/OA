package cn.edu.jumy.oa.timchat.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.TIMFriendAllowType;
import com.tencent.TIMUserProfile;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.FriendInfoView;
import cn.edu.jumy.jumyframework.BaseFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.oa.HomeActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.timchat.ui.customview.LineControllerView;
import cn.edu.jumy.oa.timchat.ui.customview.ListPickerDialog;

/**
 * 设置页面
 */
public class SettingFragment extends BaseFragment implements FriendInfoView{

    private static final String TAG = SettingFragment.class.getSimpleName();
    private View view;
    private FriendshipManagerPresenter friendshipManagerPresenter;
    private TextView id,name;
    private LineControllerView friendConfirm;
    private final int REQ_CHANGE_NICK = 1000;
    private Map<String, TIMFriendAllowType> allowTypeContent;



    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null){
            try {
                view = inflater.inflate(R.layout.fragment_setting, container, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            id = (TextView) view.findViewById(R.id.idtext);
            name = (TextView) view.findViewById(R.id.name);
            friendshipManagerPresenter = new FriendshipManagerPresenter(this);
            friendshipManagerPresenter.getMyProfile();
            TextView logout = (TextView) view.findViewById(R.id.logout);
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginBusiness.logout(new TIMCallBack() {
                        @Override
                        public void onError(int i, String s) {
                            Toast.makeText(getActivity(),getResources().getString(R.string.setting_logout_fail),Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess() {
                            ((HomeActivity) getActivity()).logout();
                        }
                    });
                }
            });
//            nickName = (LineControllerView) view.findViewById(R.id.nickName);
//            nickName.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    EditActivity.navToEdit(SettingFragment.this, getResources().getString(R.string.setting_nick_name_change), name.getText().toString(), REQ_CHANGE_NICK, new EditActivity.EditInterface() {
//                        @Override
//                        public void onEdit(String text, TIMCallBack callBack) {
//                            FriendshipManagerPresenter.setMyNick(text, callBack);
//                        }
//                    },20);
//
//                }
//            });
            allowTypeContent = new HashMap<>();
            allowTypeContent.put(getString(R.string.friend_allow_all), TIMFriendAllowType.TIM_FRIEND_ALLOW_ANY);
            allowTypeContent.put(getString(R.string.friend_need_confirm), TIMFriendAllowType.TIM_FRIEND_NEED_CONFIRM);
            allowTypeContent.put(getString(R.string.friend_refuse_all), TIMFriendAllowType.TIM_FRIEND_DENY_ANY);
            final String[] stringList = allowTypeContent.keySet().toArray(new String[allowTypeContent.size()]);
            friendConfirm = (LineControllerView) view.findViewById(R.id.friendConfirm);
            friendConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new ListPickerDialog().show(stringList, getFragmentManager(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, final int which) {
                            FriendshipManagerPresenter.setFriendAllowType(allowTypeContent.get(stringList[which]), new TIMCallBack() {
                                @Override
                                public void onError(int i, String s) {
                                    Toast.makeText(getActivity(), getString(R.string.setting_friend_confirm_change_err), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onSuccess() {
                                    friendConfirm.setContent(stringList[which]);
                                }
                            });
                        }
                    });
                }
            });
            LineControllerView about = (LineControllerView) view.findViewById(R.id.about);
            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(getActivity(), AboutActivity.class);
//                    startActivity(intent);
                }
            });
            LineControllerView folder = (LineControllerView) view.findViewById(R.id.folder);
            folder.setCanNav(false);

        }
        return view ;
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CHANGE_NICK){
            if (resultCode == getActivity().RESULT_OK){
                setNickName(data.getStringExtra(EditActivity.RETURN_EXTRA));
            }
        }

    }

    private void setNickName(String name){
        if (name == null) return;
        this.name.setText(name);
    }


    /**
     * 显示用户信息
     *
     * @param users 资料列表
     */
    @Override
    public void showUserInfo(List<TIMUserProfile> users) {
        id.setText(users.get(0).getIdentifier());
        setNickName(users.get(0).getNickName());
        for (String item : allowTypeContent.keySet()){
            if (allowTypeContent.get(item) == users.get(0).getAllowType()){
                friendConfirm.setContent(item);
                break;
            }
        }

    }
}
