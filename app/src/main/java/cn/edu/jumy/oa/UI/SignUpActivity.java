package cn.edu.jumy.oa.UI;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.callback.StringCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.adapter.MultiDropDownAdapter;
import cn.edu.jumy.oa.safe.PasswordUtil;
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollGridView;
import okhttp3.Call;

/**
 * 报名
 * Created by Jumy on 16/7/1 11:49.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_sign_up)
@OptionsMenu(R.menu.sign_up_details)
public class SignUpActivity extends BaseActivity {
    @ViewById(R.id.title_bar)
    protected Toolbar mToolBar;
    @ViewById(R.id.sign_up_name)
    protected AppCompatEditText mSignUpName;
    @ViewById(R.id.sign_up_position)
    protected AppCompatEditText mSignUpPosition;
    @ViewById(R.id.sign_up_phone)
    protected AppCompatEditText mSignUpPhone;
    @ViewById(R.id.dropDownMenu)
    protected DropDownMenu mDropDownMenu;
    @ViewById(R.id.sign_up_join_button)
    protected CheckBox mSignUpJoinButton;
    @ViewById(R.id.sign_up_listen_button)
    protected CheckBox mSignUpListenButton;
    @ViewById(R.id.sign_up_leave_button)
    protected CheckBox mSignUpLeaveButton;
    @ViewById(R.id.sign_up_leave)
    protected AppCompatEditText mSignUpLeave;
    @ViewById(R.id.submit)
    protected TextView mSubmit;


    private String[] mUnits = {"省委办公厅", "省信访局", "省档案局", "省委机要局", "省人大常委办公厅"};
    private String headers[] = {"请选择"};
    private List<View> popupView1 = new ArrayList<>();
    private MultiDropDownAdapter mUnitAdapter;

    /**
     * 初始化--控件绑定之后
     */
    @AfterViews
    void start() {
        setSupportActionBar(mToolBar);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        initUnitView();
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
                mDropDownMenu.setTabText(mUnitAdapter.getNeedString());
                mDropDownMenu.closeMenu();
            }
        });

        popupView1.add(view);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUnitAdapter.setSingleCheckItem(position);
            }
        });

        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupView1, null);

    }
    @OptionsItem(R.id.action_details)
    void skipToDetails(){
        // TODO: 16/7/4 跳转到详情
        SignUpDetailsActivity_.intent(mContext).start();
    }

    /**
     * 三个会议状态的点击事件
     * @param view
     */
    @Click({R.id.sign_up_ll_join, R.id.sign_up_ll_leave, R.id.sign_up_ll_listen})
    void click(View view) {
        switch (view.getId()) {
            case R.id.sign_up_ll_join: {//参会
                resetButton();
                mSignUpJoinButton.setChecked(true);
                break;
            }
            case R.id.sign_up_ll_leave: {//请假
                resetButton();
                mSignUpLeaveButton.setChecked(true);
                mSignUpLeave.setFocusable(true);
                break;
            }
            case R.id.sign_up_ll_listen: {//听会
                resetButton();
                mSignUpListenButton.setChecked(true);
                break;
            }
            default:break;
        }
    }
    @Click(R.id.submit)
    void submit(){
        // TODO: 16/7/1 确认报名
        doMsgUpload();
    }

    /**
     * 上传（个人/单位）会议报名信息
     */
    AlertDialog alertDialog;
    private void doMsgUpload() {
        alertDialog = new AlertDialog.Builder(mContext)
                .setTitle("确认报名")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("报名成功");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        backToPreActivity();

                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    /**
     * 重置按钮和文本焦点
     */
    private void resetButton(){
        mSignUpLeaveButton.setChecked(false);
        mSignUpJoinButton.setChecked(false);
        mSignUpListenButton.setChecked(false);
        mSignUpLeave.clearFocus();
        mSignUpLeave.setFocusable(false);
    }

    /**
     * 清除焦点
     */
    private void clearFocus(){
        mSignUpName.clearFocus();
        mSignUpPosition.clearFocus();
        mSignUpPhone.clearFocus();
        mSignUpLeave.clearFocus();
    }
}
