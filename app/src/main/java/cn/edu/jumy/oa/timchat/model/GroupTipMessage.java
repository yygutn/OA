package cn.edu.jumy.oa.timchat.model;

import android.content.Context;
import android.view.View;

import com.tencent.TIMGroupMemberInfo;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMMessage;
import cn.edu.jumy.oa.R;

import java.util.Iterator;
import java.util.Map;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.timchat.adapters.ChatAdapter;

/**
 * 群tips消息
 */
public class GroupTipMessage extends Message {


    public GroupTipMessage(TIMMessage message){
        this.message = message;
    }


    /**
     * 显示消息
     *
     * @param viewHolder 界面样式
     * @param context    显示消息的上下文
     */
    @Override
    public void showMessage(ChatAdapter.ViewHolder viewHolder, Context context) {
        viewHolder.leftPanel.setVisibility(View.GONE);
        viewHolder.rightPanel.setVisibility(View.GONE);
        viewHolder.systemMessage.setVisibility(View.VISIBLE);
        viewHolder.systemMessage.setText(getSummary());
    }

    /**
     * 获取消息摘要
     */
    @Override
    public String getSummary() {
        final TIMGroupTipsElem e = (TIMGroupTipsElem) message.getElement(0);
        switch (e.getTipsType()){
            case CancelAdmin:
            case SetAdmin:
                return MyApplication.getContext().getString(R.string.summary_group_admin_change);
            case Join:
                StringBuilder names = new StringBuilder();
                Iterator<Map.Entry<String,TIMGroupMemberInfo>> iterator = e.getChangedGroupMemberInfo().entrySet().iterator();
                while(iterator.hasNext()){
                    Map.Entry<String,TIMGroupMemberInfo> item = iterator.next();
                    names.append(getName(item.getValue()));
                    names.append(" ");
                }
                return names +
                        MyApplication.getContext().getString(R.string.summary_group_mem_add);
            case Kick:
                return e.getUserList().get(0) +
                        MyApplication.getContext().getString(R.string.summary_group_mem_kick);
            case ModifyMemberInfo:
                return e.getChangedUserInfo() +
                        MyApplication.getContext().getString(R.string.summary_group_mem_modify);
            case Quit:
                return e.getOpUser() +
                        MyApplication.getContext().getString(R.string.summary_group_mem_quit);
            case ModifyGroupInfo:
                return MyApplication.getContext().getString(R.string.summary_group_info_change);
        }
        return "";
    }

    private String getName(TIMGroupMemberInfo info){
        if (info.getNameCard().equals("")){
            return info.getUser();
        }
        return info.getNameCard();
    }
}
