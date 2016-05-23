package cn.edu.jumy.oa.timchat.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tencent.TIMGroupDetailInfo;
import com.tencent.qcloud.presentation.presenter.GroupManagerPresenter;
import com.tencent.qcloud.presentation.viewfeatures.GroupInfoView;
import cn.edu.jumy.oa.R;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.oa.timchat.adapters.ProfileSummaryAdapter;
import cn.edu.jumy.oa.timchat.model.GroupProfile;
import cn.edu.jumy.oa.timchat.model.ProfileSummary;

public class SearchGroupActivity extends Activity implements GroupInfoView, View.OnKeyListener{

    private final String TAG = "SearchGroupActivity";

    private GroupManagerPresenter groupManagerPresenter;
    private List<ProfileSummary> list= new ArrayList<>();
    private ProfileSummaryAdapter adapter;
    private EditText searchInput;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group);
        searchInput = (EditText) findViewById(R.id.inputSearch);
        listView =(ListView) findViewById(R.id.list);
        adapter = new ProfileSummaryAdapter(this, R.layout.item_profile_summary, list);
        listView.setAdapter(adapter);
        groupManagerPresenter = new GroupManagerPresenter(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                list.get(position).onClick(SearchGroupActivity.this);
            }
        });
        TextView tvCancel = (TextView) findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        searchInput.setOnKeyListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP){   // 忽略其它事件
            return false;
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_ENTER:
                list.clear();
                adapter.notifyDataSetChanged();
                groupManagerPresenter.searchGroupByName(searchInput.getText().toString());
                return true;
            default:
                return false;
        }
    }

    /**
     * 显示群资料
     *
     * @param groupInfos 群资料信息列表
     */
    @Override
    public void showGroupInfo(List<TIMGroupDetailInfo> groupInfos) {
        list.clear();
        for (TIMGroupDetailInfo item : groupInfos){
            list.add(new GroupProfile(item));
        }
        adapter.notifyDataSetChanged();
    }
}
