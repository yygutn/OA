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

import com.bluelinelabs.logansquare.LoganSquare;
import com.hyphenate.chat.EMClient;
import com.orhanobut.logger.Logger;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.API.GetSendUnit;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.ListDropDownAdapter;
import cn.edu.jumy.oa.adapter.MultiDropDownAdapter;
import cn.edu.jumy.oa.bean.Account;
import cn.edu.jumy.oa.bean.BaseResponse;
import cn.edu.jumy.oa.safe.PasswordUtil;
import cn.edu.jumy.oa.server.UploadServer;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollGridView;
import cn.edu.jumy.oa.widget.customview.NoScrollListView;
import cn.edu.jumy.oa.widget.customview.UploadItem;
import cn.edu.jumy.oa.widget.customview.UploadItem_;
import okhttp3.Call;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private Object str;

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
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        try {
            long time = new Date().getTime();
            String base = EMClient.getInstance().getCurrentUser()+"_"+time;
            String zip = PasswordUtil.simpleEncpyt(base);
            Logger.w(base+"\n"+zip);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value",zip);

            OkHttpUtils.post()
                    .url(MyApplication.API_URL+"getOrganizationData")
                    .addParams("value",zip)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            String string = response.body().toString();
                            return string;
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            e.printStackTrace();
                            Logger.e(e.getMessage());
                        }

                        @Override
                        public void onResponse(Object response, int id) {

                        }
                    });

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MyApplication.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            GetSendUnit getSendUnit = retrofit.create(GetSendUnit.class);
            retrofit2.Call<String> call = getSendUnit.getUnits(zip);
            call.enqueue(new retrofit2.Callback<String>() {

                @Override
                public void onResponse(retrofit2.Call<String> call, retrofit2.Response<String> response) {

                }

                @Override
                public void onFailure(retrofit2.Call<String> call, Throwable t) {

                }
            });
            initUnitView();
            initLevelView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(UploadServer.UPLOAD_BR_RESULT);
        registerReceiver(uploadBroadcastReceiver,filter);

        UploadItem item = UploadItem_.build(mContext,this);
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
