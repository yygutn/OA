package cn.edu.jumy.oa.fragment;

import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;
import com.zhy.base.adapter.recyclerview.OnItemClickListener;
import com.zhy.http.okhttp.callback.Callback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.BroadCastReceiver.NotifyReceiveBroadCastReceiver;
import cn.edu.jumy.oa.CallBack.DocCallback;
import cn.edu.jumy.oa.CallBack.MeetCallback;
import cn.edu.jumy.oa.CallBack.RelayCallback2;
import cn.edu.jumy.oa.CallBack.StringCallback2;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.OaPreference;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.DocResponse;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.Response.NotifyBroadCastResponse;
import cn.edu.jumy.oa.Response.Relay2Response;
import cn.edu.jumy.oa.Response.SingleNotifyResponse;
import cn.edu.jumy.oa.Response.StringListResponse;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity;
import cn.edu.jumy.oa.UI.TaskItem.DetailsActivity_;
import cn.edu.jumy.oa.Utils.NotifyUtils;
import cn.edu.jumy.oa.adapter.NotifyCardAdapter;
import cn.edu.jumy.oa.bean.Doc;
import cn.edu.jumy.oa.bean.Meet;
import cn.edu.jumy.oa.bean.Node;
import cn.edu.jumy.oa.bean.Relay;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Jumy on 16/5/19 12:07.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 * *****************************************************
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #                佛祖保佑         永无BUG
 * #                                                   #
 * *****************************************************
 */
@EFragment(R.layout.fragment_notify)
public class NotifyFragment extends BaseFragment implements OnItemClickListener {
    @ViewById(R.id.notify_listView)
    PullToRefreshRecyclerView mListView;

    ImageView mEmptyImageView;
    private final CopyOnWriteArrayList<Relay> mList = new CopyOnWriteArrayList<>();
    NotifyCardAdapter adapter;
    private int lastClickPosition = 0;

    NotifyReceiveBroadCastReceiver notifyReceiveBroadCastReceiver = new NotifyReceiveBroadCastReceiver() {
        @Override
        public void onNotifyReceive(NotifyBroadCastResponse response) {
            showDebugLogd("onNotifyReceive", response.toString());
            if (TextUtils.isEmpty(response.id)) {
                return;
            }
            switch (response.action) {
                case "docSend":
                case "docReceive":
                case "docUrge": {
                    getDoc(response.id);
                    break;
                }
                case "meetReceive":
                case "meetSend":
                case "meetUserPass":
                case "meetUrge": {
                    getMeet(response.id);
                    break;
                }
                case "noticeSend":
                case "getNotice": {
                    getNotify(response.id);
                    break;
                }
                default:
                    break;
            }
        }
    };

    @AfterViews
    void start() {
        getIndexData();

        mEmptyImageView = (ImageView) View.inflate(mContext, R.layout.item_empty_view, null);

        mListView.setEmptyView(mEmptyImageView);

        mListView.setLayoutManager(new LinearLayoutManager(mContext));

        initListView();
        //注册接收透传消息的广播
        IntentFilter filter = new IntentFilter(NotifyReceiveBroadCastReceiver.ACTION_NOTIFY);
        mContext.registerReceiver(notifyReceiveBroadCastReceiver, filter);
    }

    private void getIndexData() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                mListView.setRefreshing(true);
            }
        });
        OAService.indexFirstGet(new RelayCallback2() {
            @Override
            public void onResponse(final Relay2Response response, int ID) {
                if (response.code == 1) {
                    showToast("获取列表失败");
                    onError(null, null, 0);
                    return;
                }
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
//                        for (Relay relay : response.data) {
//                            mList.add(new Node(relay));
//                        }
                        mList.addAll(response.data);
                        response.data = null;
                        mListView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                mListView.setOnRefreshComplete();
                            }
                        }, 1000);
                    }
                });
            }

            @Override
            public void onError(Call call, Exception e, int ID) {
                mListView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mListView.setOnRefreshComplete();
                    }
                }, 1000);
            }
        });

        OAService.indexNoPassMeet(new StringCallback2() {

            @Override
            public void onResponse(final StringListResponse response, int ID) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        for (String id : response.data) {
                            String action = "{\"action\":\"meetUserPass\",\"id\":\"" + id + "\"}";
                            NotifyUtils.sendNotifyBroadCast(mContext, action);
                        }
                    }
                });
            }
        });
    }


    private void initListView() {
        adapter = new NotifyCardAdapter(mContext, R.layout.item_card_notification, mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        Node node = new Node((Relay)o);
        DetailsActivity.FROM_NOTIFY = true;
        DetailsActivity_.intent(mContext).extra("details", node).startForResult(2048);
        lastClickPosition = position;
    }

    @Override
    public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
        return false;
    }

    @Override
    public void onDestroy() {
        if (notifyReceiveBroadCastReceiver != null) {
            mContext.unregisterReceiver(notifyReceiveBroadCastReceiver);
        }
        if (adapter != null){
            adapter = null;
        }
        super.onDestroy();
    }

    private Map<String, String> getParams(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        return params;
    }

    private synchronized void getDoc(String id) {
        OAService.docReceive(getParams(id), new DocCallback() {
            @Override
            public void onResponse(DocResponse response, int id) {
                if (response.code != 0) {
                    return;
                }
                synchronized (mList) {
                    try {
                        if (response.data.pageObject.size() > 0) {
                            Doc doc = response.data.pageObject.get(0);
                            for (Relay node : mList) {
                                if (doc.id.equals(node.id)) {
                                    mList.remove(node);
                                }
                            }
                            mList.add(0, new Relay(doc));
                            response.data = null;
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        showDebugException(e);
                    }
                }
            }
        });
    }

    private synchronized void getMeet(String id) {
        OAService.meetReceive(getParams(id), new MeetCallback() {
            @Override
            public void onResponse(MeetResponse response, int ID) {
                if (response.code != 0) {
                    return;
                }
                synchronized (mList) {
                    if (response.data.pageObject.size() > 0) {
                        try {
                            Meet meet = response.data.pageObject.get(0);
                            for (Relay node : mList) {
                                if (meet.id.equals(node.id)) {
                                    mList.remove(node);
                                }
                            }
                            mList.add(0, new Relay(response.data.pageObject.get(0)));
                            response.data = null;
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            showDebugException(e);
                        }
                    }
                }
            }
        });
    }

    private synchronized void getNotify(String id) {
        OAService.getNotice(id, new NotifyCallBack() {
            @Override
            public void onResponse(SingleNotifyResponse response, int ID) {
                if (response.code != 0 || response.data == null) {
                    return;
                }
                synchronized (mList) {
                    try {
                        Relay temp = new Relay(response.data);
                        for (Relay node : mList) {
                            if (node.id.equals(temp.id)) {
                                mList.remove(node);
                            }
                        }
                        mList.add(0, temp);
                        response.data = null;
                        adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        showDebugException(e);
                    }
                }
            }
        });
    }

    public void onResult(int resultCode) {
        if (resultCode == 1025) {
            mList.remove(lastClickPosition);
            adapter.notifyDataSetChanged();
        }
    }

    abstract class NotifyCallBack extends Callback<SingleNotifyResponse> {
        @Override
        public SingleNotifyResponse parseNetworkResponse(Response response, int ID) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
            if (baseResponse.code == 0) {
                return gson.fromJson(data, SingleNotifyResponse.class);
            } else {
                return new SingleNotifyResponse(baseResponse);
            }
        }

        @Override
        public void onError(Call call, Exception e, int ID) {

        }
    }
}