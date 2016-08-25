package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import java.util.ArrayList;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.RelayCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.RelayResponse;
import cn.edu.jumy.oa.adapter.RelayAdapter;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Relay;
import okhttp3.Call;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/24  下午5:41
 */
@EActivity(R.layout.activity_fa_qi)
public class ApprovalFqActivity extends BaseSearchRefreshActivity {

    RelayAdapter adapter;
    ArrayList<Relay> mList = new ArrayList<>();

    int index = 1;
    static final int basePages = 20;

    @AfterViews
    void getData() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setRefreshing(true);
            }
        });
        OAService.findRelay(getParams(index), new RelayCallback() {
            @Override
            public void onResponse(RelayResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList.addAll(response.data.pageObject);
                    mListView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setList(new ArrayList(mList));
                            mListView.setLoadMoreCount(index * basePages);
                            index++;
                            mListView.setOnRefreshComplete();
                        }
                    }, 1000);
                } else {
                    onError(null, null, 0);
                }
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                super.onError(call, e, id);
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setOnRefreshComplete();
                    }
                }, 1000);
            }
        });
    }

    @NonNull
    private Map<String, String> getParams(int Index) {

        Map<String, String> params = new ArrayMap<>();
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("keywords", "");
        return params;
    }

    @Override
    protected void setTile() {
        mTitleBar.setTitle("我的转发");
    }


    @Override
    protected void initListView() {
        adapter = new RelayAdapter(mContext, R.layout.item_card_notification, new ArrayList(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        showDebugLogd(o.toString());
        Relay node = (Relay) o;
        DetailsActivity_.intent(mContext).extra("details", new Node(node)).extra("FromFQ", true).start();
//        AuditDetailsActivity_.intent(mContext).extra("id", node.oldid).extra("type", (node.type == 1 ? 1 : 2) + "").extra("From_FQ",true).start();
    }

    @Override
    protected void doLoadMore() {
        //加载更多
        OAService.findRelay(getParams(index++), new RelayCallback() {
            @Override
            public void onResponse(RelayResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    mList.addAll(response.data.pageObject);
                    adapter.setList(new ArrayList(mList));
                    mListView.setLoadMoreCount(index * basePages);
                    index++;
                }
            }
        });
    }

    @Override
    public void doRefresh() {
        //下拉刷新
        OAService.findRelay(getParams(1), new RelayCallback() {
            @Override
            public void onResponse(RelayResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    int size = response.data.pageObject.size();
                    Relay node = mList.get(0);
                    int position = 0;
                    for (int i = size - 1; i >= 0; i--) {
                        if (response.data.pageObject.get(i).id.equals(node.id)) {
                            position = i;
                            break;
                        }
                    }
                    for (int i = position - 1; i >= 0; i--) {
                        mList.add(0, response.data.pageObject.get(i));
                    }
                    mListView.setLoadMoreCount((index - 1) * basePages + position);
                    adapter.setList(new ArrayList(mList));
                }
            }
        });
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)) {
            showToast("请输入关键字");
            return;
        }
        ArrayList<Relay> list = new ArrayList<>();
        for (Relay node : mList) {
            if (node.remark.contains(str) || node.docTitle.contains(str) || node.docSummary.contains(str)) {
                list.add(node);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(new ArrayList<>(mList));
    }
}
