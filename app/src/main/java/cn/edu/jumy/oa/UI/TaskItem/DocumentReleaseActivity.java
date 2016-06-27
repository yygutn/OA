package cn.edu.jumy.oa.UI.TaskItem;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.ListDropDownAdapter;
import cn.edu.jumy.oa.adapter.MultiDropDownAdapter;
import cn.edu.jumy.oa.server.UploadServer;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollGridView;
import cn.edu.jumy.oa.widget.customview.NoScrollListView;
import cn.edu.jumy.oa.widget.customview.UploadItem;
import cn.edu.jumy.oa.widget.customview.UploadItem_;

/**
 * Created by Jumy on 16/6/23 11:58.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_release)
public class DocumentReleaseActivity extends BaseActivity {
    @ViewById(R.id.dropDownMenu_1)
    protected DropDownMenu mDropDownMenu1;
    @ViewById(R.id.dropDownMenu_2)
    protected DropDownMenu mDropDownMenu2;
    @ViewById(R.id.et_1)
    protected AppCompatEditText mEt1;
    @ViewById(R.id.et_2)
    protected AppCompatEditText mEt2;
    @ViewById(R.id.et_3)
    protected AppCompatEditText mEt3;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById
    TextView submit;

    List<String> mFilePath = new ArrayList<>();
    @ViewById(R.id.uploadView)
    LinearLayout uploadView;
    int index = 0;

    static DocumentReleaseActivity instance;
    private Object str;

    public static DocumentReleaseActivity getInstance() {
        return instance;
    }

    private MultiDropDownAdapter mUnitAdapter;
    private ListDropDownAdapter mLevelAdapter;

    private String[] mUnits = {"省委办公厅", "省信访局", "省档案局", "省委机要局", "省人大常委办公厅"};
    private String[] mLevel = {"请选择", "特急", "加急", "平急", "特提"};
    private String headers[] = {"请选择"};

    private List<View> popupView1 = new ArrayList<>();
    private List<View> popupView2 = new ArrayList<>();

    BroadcastReceiver uploadBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == UploadServer.UPLOAD_BR_RESULT){
                String path= intent.getStringExtra(UploadServer.EXTRA_PATH);
                if (!mFilePath.contains(path)){
                    mFilePath.add(path);
                }
            }
        }
    };

    @AfterViews
    void start() {
        instance = this;
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            initUnitView();
            initLevelView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadServer.UPLOAD_BR_RESULT);
        registerReceiver(uploadBroadcastReceiver,filter);


        UploadItem item = UploadItem_.build(mContext,this);
        uploadView.addView(item,index++);
    }

    @Click({R.id.submit,R.id.addUpload})
    void clickSubmit(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                confirmSubmit();
                break;
            }
            case R.id.addUpload:{
                UploadItem item = UploadItem_.build(mContext,this);
                uploadView.addView(item,index++);
                break;
            }
            default:
                break;
        }
    }

    private void confirmSubmit() {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).
                setTitle("确认发送")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("发送成功");
                        getStr();
                        // TODO: 16/6/27 启动后台上传服务，关闭当前页面
                        backToPreActivity();
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void initUnitView() {
        final View view = getLayoutInflater().inflate(R.layout.layout_custom_release,null);
        NoScrollGridView gridView = (NoScrollGridView) view.findViewById(R.id.gridView);
        mUnitAdapter = new MultiDropDownAdapter(mContext, Arrays.asList(mUnits));
        gridView.setAdapter(mUnitAdapter);

        TextView textView = (TextView) view.findViewById(R.id.ok);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDropDownMenu1.setTabText(mUnitAdapter.getNeedString());
                mDropDownMenu1.closeMenu();
            }
        });

        popupView1.add(view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUnitAdapter.setCheckItem(position);
            }
        });

        mDropDownMenu1.setDropDownMenu(Arrays.asList(headers), popupView1, null);

    }

    private void initLevelView() {
        final NoScrollListView listView = new NoScrollListView(mContext);
        listView.setDividerHeight(0);
        mLevelAdapter = new ListDropDownAdapter(mContext, Arrays.asList(mLevel));
        listView.setAdapter(mLevelAdapter);

        popupView2.add(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mLevelAdapter.setCheckItem(position);
                mDropDownMenu2.setTabText(mLevel[position]);
                mDropDownMenu2.setTabTextColor(pressed);
                mDropDownMenu2.closeMenu();
            }
        });

        mDropDownMenu2.setDropDownMenu(Arrays.asList(headers), popupView2, null);
    }

    @ColorRes(R.color.pressed)
    int pressed;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(uploadBroadcastReceiver);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu1.isShowing()) {
            mDropDownMenu1.closeMenu();
        } else if (mDropDownMenu2.isShowing()) {
            mDropDownMenu2.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    private void getStr() {
        // TODO: 16/6/27 获取输入框内文字
    }
}