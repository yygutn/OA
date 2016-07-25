package cn.edu.jumy.oa.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

import java.util.ArrayList;

import cn.edu.jumy.oa.BroadCastReceiver.MeetBroadcastReceiver;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Meet;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/24  下午5:55
 */
@EFragment(R.layout.fragment_document_all)
public class AlreadyApprovalFragment extends BaseSearchRefreshFragment{
    MeetingCardAdapter adapter;
    ArrayList<Meet> mList = new ArrayList<>();

    MeetBroadcastReceiver meetBroadcastReceiver = new MeetBroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (getType() == 1){
                mList = getMeetList();
                adapter.setList(mList);
            }
        }
    };

    @Override
    protected void initList() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MeetBroadcastReceiver.MEET);
        mContext.registerReceiver(meetBroadcastReceiver, filter);
    }
    @Override
    protected void updateListView() {
        adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification,mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onTextChanged(CharSequence message) {
        if (TextUtils.isEmpty(message)){
            return;
        }
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet meet : mList){
            if (meet.docTitle.contains(message)){
                list.add(meet);
            }
        }
        adapter.setList(list);
    }

    @Override
    public void onSearchCancel() {
        adapter.setList(mList);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        //下一步，进入详情界面...
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (meetBroadcastReceiver != null) {
                mContext.unregisterReceiver(meetBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
