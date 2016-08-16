package cn.edu.jumy.oa.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.zhy.base.adapter.ViewHolder;
import com.zhy.base.adapter.recyclerview.CommonAdapter;

import java.util.List;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.UploadItemBean;
import cn.qqtheme.framework.picker.FilePicker;
import cn.qqtheme.framework.util.StorageUtils;

/**
 * Created by Jumy on 16/8/16 15:40.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class UploadItemAdapter extends CommonAdapter<UploadItemBean> {
    public UploadItemAdapter(Context context, int layoutId, List list, Activity activity) {
        super(context, layoutId, list);
        this.mActivity = activity;
    }

    @Override
    public void convert(final ViewHolder holder, final UploadItemBean item) {
        holder.setOnClickListener(R.id.select_click, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(item);
            }
        });
        holder.setText(R.id.select_name, item.fileName);
        holder.setOnClickListener(R.id.select_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatas.remove(holder.getLayoutPosition());
                notifyDataSetChanged();
            }
        });
    }

    private Activity mActivity;
    private String fileName = "";
    private String filePath = "";

    void click(final UploadItemBean item) {
        FilePicker picker = new FilePicker(mActivity, FilePicker.FILE);
        picker.setShowHideDir(false);
        picker.setRootPath(StorageUtils.getRootPath(mContext) + "Pictures/");
        picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
            @Override
            public void onFilePicked(String currentPath) {
                String path[] = currentPath.split("/");
                fileName = path[path.length - 1];
                filePath = currentPath.trim();

                item.fileName = fileName;
                item.filePath = filePath;
                notifyDataSetChanged();
            }
        });
        picker.show();
    }
}
