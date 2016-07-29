package cn.edu.jumy.oa.UI.TaskItem;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.AfterExtras;
import org.androidannotations.annotations.EActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.adapter.MeetingCardAdapter;
import cn.edu.jumy.oa.bean.Meet;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/24  下午5:41
 */
@EActivity(R.layout.activity_fa_qi)
public class ApprovalFqActivity extends BaseSearchRefreshActivity {

    MeetingCardAdapter adapter;
    ArrayList<Meet> mList = new ArrayList<>();

    int index = 1;
    static final int basePages = 20;

    @AfterExtras
    void getData() {
//        OAService.meetUser(getParams(index), new MeetCallback() {
//            @Override
//            public void onResponse(MeetResponse response, int id) {
//                if (response != null && response.code == 0 && response.data != null) {
//                    mList.addAll(response.data.pageObject);
//                    adapter.setList(new ArrayList(mList));
//                    mListView.setLoadMoreCount(index * basePages);
//                    index++;
//                }
//            }
//        });
    }

    @NonNull
    private Map<String, String> getParams(int Index) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        int year, month, day;
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR) - 1900;
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if ((month < 8 && (month & 1) == 1) || (month >= 8 && (month & 1) == 0)) {
            if (day == 31) {
                day--;
            }
            if (month == 2 && day > 28) {
                day = 28;
            }
            if (month == 0) {
                month = 11;
                year--;
            } else {
                month--;
            }
        } else {
            month--;
        }
        String before = sdf.format(new Date(year, month, day));
        String now = sdf.format(date);

        Map<String, String> params = new HashMap<>();
        params.put("page", Index + "");
        params.put("size", basePages + "");
        params.put("level", "");
        params.put("docNo", "");
        params.put("docTitle", "");
        params.put("startTime", before);
        params.put("endTime", now);
        params.put("signStatus", "");
        params.put("passStatus", "");
        params.put("meetCompany", "");
        return params;
    }

    @Override
    protected void setTile() {
        mTitleBar.setTitle("由我发起");
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initListView() {
        adapter = new MeetingCardAdapter(mContext, R.layout.item_card_notification, new ArrayList(mList));
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        showDebugLogd(o.toString());
    }

    @Override
    protected void doLoadMore() {
        //加载更多
        OAService.meetUser(getParams(index++), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
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
        OAService.meetUser(getParams(1), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int id) {
                if (response != null && response.code == 0 && response.data != null) {
                    int size = response.data.pageObject.size();
                    Meet node = mList.get(0);
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
                    mListView.setLoadMoreCount((index-1)*basePages+position);
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
        ArrayList<Meet> list = new ArrayList<>();
        for (Meet node : mList) {
            if (node.docTitle.contains(str)) {
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
