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
import cn.edu.jumy.oa.BroadCastReceiver.RelayBroadcastReceiver;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.adapter.RelayAdapter;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Relay;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/24  下午5:55
 */
@EFragment(R.layout.fragment_document_all)
public class AlreadyApprovalFragment extends BaseSearchRefreshFragment{
    RelayAdapter adapter;
    ArrayList<Relay> mList = new ArrayList<>();

    RelayBroadcastReceiver relayBroadcastReceiver = new RelayBroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
            if (getType() == 1){
                mList = getList();
                adapter.setList(mList);
            }
        }
    };

    @Override
    protected void initList() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(MeetBroadcastReceiver.MEET);
        mContext.registerReceiver(relayBroadcastReceiver, filter);
    }
    @Override
    protected void updateListView() {
        adapter = new RelayAdapter(mContext, R.layout.item_card_notification,mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onTextChanged(CharSequence message) {
        if (TextUtils.isEmpty(message)){
            return;
        }
        ArrayList<Relay> list = new ArrayList<>();
        for (Relay meet : mList){
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
        DetailsActivity_.intent(mContext).extra("from_SP",true).extra("details",new Node((Relay)o)).startForResult(2048);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (relayBroadcastReceiver != null) {
                mContext.unregisterReceiver(relayBroadcastReceiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
