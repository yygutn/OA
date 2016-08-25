package cn.edu.jumy.oa.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fsck.k9.K9;
import com.fsck.k9.activity.Accounts;

import java.util.ArrayList;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseFragment;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.CalendarActivity_;
import cn.edu.jumy.oa.UI.MeetingCardActivity_;
import cn.edu.jumy.oa.UI.TaskItem.ApprovalFqActivity_;
import cn.edu.jumy.oa.UI.TaskItem.ApprovalSpActivity_;
import cn.edu.jumy.oa.UI.TaskItem.DocumentReadActivity_;
import cn.edu.jumy.oa.UI.TaskItem.DocumentReleaseActivity_;
import cn.edu.jumy.oa.UI.TaskItem.MeetingApprovalActivity_;
import cn.edu.jumy.oa.UI.TaskItem.NotifyActivity_;
import cn.edu.jumy.oa.UI.TaskItem.SendMeetingActivity_;
import cn.edu.jumy.oa.UI.TaskItem.SentDocumentActivity_;
import cn.edu.jumy.oa.UI.TaskItem.SentMeetingActivity_;
import cn.edu.jumy.oa.UI.VerifyActivity_;
import cn.edu.jumy.oa.UI.web.StudyOnlineActivity_;
import cn.edu.jumy.oa.widget.dragrecyclerview.adapter.RecyclerAdapter;
import cn.edu.jumy.oa.widget.dragrecyclerview.common.DividerGridItemDecoration;
import cn.edu.jumy.oa.widget.dragrecyclerview.entity.Item;
import cn.edu.jumy.oa.widget.dragrecyclerview.helper.MyItemTouchCallback;
import cn.edu.jumy.oa.widget.dragrecyclerview.helper.OnRecyclerItemClickListener;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.ACache;
import cn.edu.jumy.oa.widget.dragrecyclerview.utils.VibratorUtil;

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
public class TaskFragment extends BaseFragment implements MyItemTouchCallback.OnDragListener {

    private List<Item> results = new ArrayList<Item>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        ////////////////////////////////////////////////////////
        /////////初始化数据，如果缓存中有就使用缓存中的
        ArrayList<Item> items = (ArrayList<Item>) ACache.get(mContext).getAsObject("task_items");
        if (items != null && items.size() > 0) {
            showDebugLogv("from local cache");
            results.addAll(items);
        } else {
            showDebugLogv("from new data");
            results.add(new Item(4, "公文阅读", R.drawable.task_receive_file));
            results.add(new Item(5, "公文发布", R.drawable.task_send_file));
            results.add(new Item(3, "会议通知", R.drawable.task_approval));
            results.add(new Item(8, "会议发布", R.drawable.task_meet_send));
            results.add(new Item(9, "会议审核", R.drawable.task_meet_approval));
            //add 我的审批,我的转发
            results.add(new Item(1, "我的审批", R.drawable.normal_1));
//            results.add(new Item(11, "我的转发", R.drawable.normal_2));
            results.add(new Item(0, "我的转发", R.drawable.normal_2));
            results.add(new Item(6, "文件柜", R.drawable.task_folder));
            results.add(new Item(10, "日程管理", R.drawable.task_schedule_management));
            //add 公告
            results.add(new Item(12, "公告", R.drawable.task_notice));
            results.add(new Item(7, "在线学习", R.drawable.task_learn_online));
            results.add(new Item(13, "已发送公文", R.drawable.task_file_send));
            results.add(new Item(14, "已发布会议", R.drawable.task_meet_sent));
            results.add(new Item(2, "加密邮件", R.drawable.task_mail));
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return LayoutInflater.from(mContext).inflate(R.layout.fragment_task, null);
    }

    private ItemTouchHelper itemTouchHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerAdapter adapter = new RecyclerAdapter(R.layout.item_grid, results);
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(mContext));

        itemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(adapter).setOnDragListener(this));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        recyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(recyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                if (vh.getLayoutPosition() != results.size() - 1) {
                    itemTouchHelper.startDrag(vh);
                    VibratorUtil.Vibrate(mContext, 70);   //震动70ms
                }
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                Bundle bundle;
                Item item = results.get(vh.getLayoutPosition());
//                showToast(item.getId() + " " + item.getName());
                switch (item.getId()) {
                    case 0: {//我的转发
                        ApprovalFqActivity_.intent(mContext).start();
                        break;
                    }
                    case 1: {//我的审批
                        ApprovalSpActivity_.intent(mContext).start();
                        break;
                    }
                    case 2: {//邮件
                        if (MyApplication.isFirst){
                            final ProgressDialog progressDialog = new ProgressDialog(mContext);
                            progressDialog.setMessage("邮箱加载中...");
                            progressDialog.show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    K9.getInstance().onCreate(MyApplication.getInstance());
                                    progressDialog.dismiss();
                                }
                            },1500);
                            MyApplication.isFirst = false;
                        }
                        startActivity(new Intent(mContext, Accounts.class));
                        break;
                    }
                    case 3: {//会议
                        // TODO: 16/6/1 修改为纯卡片展示，显示所有会议卡片，按照时间排序
                        MeetingCardActivity_.intent(mContext).start();
                        break;
                    }
                    case 4: {//收文 公文阅读
//                        ReceiveFileActivity_.intent(mContext).start();
                        DocumentReadActivity_.intent(mContext).start();
                        break;
                    }
                    case 5: {//发文 公文发布
//                        SendFileWebActivity_.intent(mContext).start();
                        DocumentReleaseActivity_.intent(mContext).start();
                        break;
                    }
                    case 6: {//文件柜
                        bundle = new Bundle();
                        bundle.putString("file", "file");
                        VerifyActivity_.intent(mContext).extra("file", bundle).start();
                        break;
                    }
                    case 7: {//在线学习
                        StudyOnlineActivity_.intent(mContext).start();
//                        VideoPlayerActivity_.intent(mContext).start();
                        break;
                    }
                    case 8: {//会议发送
//                        MeetingSendActivity_.intent(mContext).start();
                        SendMeetingActivity_.intent(mContext).start();
                        break;
                    }
                    case 9: {//会议审核
//                        AuditActivity_.intent(mContext).start();
                        MeetingApprovalActivity_.intent(mContext).start();
                        break;
                    }
                    case 10: {//日程管理
                        CalendarActivity_.intent(mContext).start();
                        break;
                    }
                    case 11: {//
                        break;
                    }
                    case 12: {//公告
                        NotifyActivity_.intent(mContext).start();
                        break;
                    }
                    case 13: {//已发送公文
//                        FileSentActivity_.intent(mContext).start();
                        SentDocumentActivity_.intent(mContext).start();
                        break;
                    }
                    case 14: {
//                        MeetingSentActivity_.intent(mContext).start();
                        SentMeetingActivity_.intent(mContext).start();
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onFinishDrag() {
        //存入缓存
        ACache.get(mContext).put("task_items", (ArrayList<Item>) results);
    }
}