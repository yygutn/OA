package cn.edu.jumy.oa.UI.TaskItem;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
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
import cn.edu.jumy.oa.widget.DropDownMenu;
import cn.edu.jumy.oa.widget.customview.NoScrollListView;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.util.StorageUtils;

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
    @ViewById(R.id.select_file)

    TextView mSelectFile;
    @ViewById(R.id.select_img)
    TextView mSelectImg;
    @ViewById(R.id.toolbar)
    Toolbar mToolbar;
    @ViewById
    TextView submit;

    String mFilePath = "";
    String mImagePath = "";

    private ListDropDownAdapter mUnitAdapter;
    private ListDropDownAdapter mLevelAdapter;

    private String[] mUnits = {"请选择", "省委办公厅", "省信访局", "省档案局", "省委机要局", "省人大常委办公厅"};
    private String[] mLevel = {"请选择", "特急", "加急", "平急", "特提"};
    private String headers[] = {"请选择"};

    private List<View> popupView1 = new ArrayList<>();
    private List<View> popupView2 = new ArrayList<>();

    @AfterViews
    void start() {
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
    }

    @Click({R.id.submit, R.id.select_file_click, R.id.select_img_click})
    void clickSubmit(View view) {
        switch (view.getId()) {
            case R.id.submit: {
                confirmSubmit();
                break;
            }
            case R.id.select_file_click: {
                selectFile();
                break;
            }
            case R.id.select_img_click: {
                selectImg();
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
                        backToPreActivity();
                    }
                }).setNegativeButton("取消", null)
                .create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
    }

    private void selectImg() {
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                String path [] = currentPath.split("/");
                mSelectImg.setText(path[path.length-1]);
                showDebugLogw(currentPath);
                mImagePath = "file:/" + currentPath.trim();
            }
        });
        picker.show();
    }

    private void selectFile() {
        FilePicker picker = new FilePicker(this, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                String path [] = currentPath.split("/");
                mSelectFile.setText(path[path.length-1]);
                showDebugLogw(currentPath);
                mFilePath = "file:/" + currentPath.trim();
            }
        });
        picker.show();
    }

    private void initUnitView() {
        final NoScrollListView listView = new NoScrollListView(mContext);
        listView.setDividerHeight(0);
        mUnitAdapter = new ListDropDownAdapter(mContext, Arrays.asList(mUnits));
        listView.setAdapter(mUnitAdapter);
        popupView1.add(listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mUnitAdapter.setCheckItem(position);
                mDropDownMenu1.setTabText(mUnits[position]);
                mDropDownMenu1.setTabTextColor(pressed);
                mDropDownMenu1.closeMenu();
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
}
