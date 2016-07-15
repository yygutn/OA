package cn.edu.jumy.oa.widget.customview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.UI.TaskItem.DocumentReleaseActivity;
import cn.edu.jumy.oa.server.UploadServer;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.util.StorageUtils;

/**
 * Created by Jumy on 16/6/27 10:53.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EViewGroup(R.layout.widget_select_upload)
public class UploadItem extends LinearLayout{


    @ViewById
    TextView selectClick;
    @ViewById
    TextView selectName;

    private Context mContext;
    private Activity ac;

    private String fileName = "";
    private String filePath = "";

    public String getPath() {
        return filePath;
    }

    public void setPath(String path) {
        this.filePath = path;
    }

    public UploadItem(Context context) {
        super(context);
        initView();
    }

    public UploadItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public UploadItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public UploadItem(Context context,Activity activity){
        super(context);
        ac = activity;
        mContext = context;
    }

    private void initView(){
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOrientation(HORIZONTAL);
    }

    @Click(R.id.select_click)
    void click(){
        FilePicker picker = new FilePicker(ac, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(mContext) + "Pictures/");
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                String path [] = currentPath.split("/");
                if (!TextUtils.isEmpty(filePath)){
                    Intent intent = new Intent(UploadServer.UPLOAD_BR_RESULT_DELETE);
                    intent.putExtra(UploadServer.EXTRA_PATH, filePath);
                    mContext.sendBroadcast(intent);
                }
                fileName = path[path.length-1];
                filePath = currentPath.trim();
//                filePath = "file:/" + currentPath.trim();
                selectName.setText(fileName);

                Intent intent = new Intent(UploadServer.UPLOAD_BR_RESULT);
                intent.putExtra(UploadServer.EXTRA_PATH, filePath);
                mContext.sendBroadcast(intent);
            }
        });
        picker.show();
    }
}
