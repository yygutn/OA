package cn.edu.jumy.oa.UI.TaskItem;

import android.content.res.Resources;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.zhy.http.okhttp.callback.FileCallBack;

import org.androidannotations.annotations.EActivity;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import cn.edu.jumy.oa.OAService;
import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Utils.CallOtherOpenFile;
import cn.edu.jumy.oa.Utils.CardGenerator;
import cn.edu.jumy.oa.adapter.AnnexAdapter;
import cn.edu.jumy.oa.bean.Annex;
import okhttp3.Call;

/**
 * Created by Jumy on 16/6/20 13:57.
 *
 * @文件柜 文件列表和检索功能
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class DocumentCabinetActivity extends BaseSearchRefreshActivity {
    AnnexAdapter adapter;
    ArrayList<Annex> mList = new ArrayList<>();

    @Override
    protected void setTile() {
        mTitleBar.setTitle("文件柜");
    }

    @Override
    protected void initData() {
        mList = (ArrayList<Annex>) DataSupport.findAll(Annex.class);
        if (mList == null) {
            mList = new ArrayList<>();
        }
    }

    @Override
    protected void initListView() {
        adapter = new AnnexAdapter(mContext, R.layout.item_notify_notification, mList);
        mListView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(ViewGroup parent, View view, Object o, int position) {
        final File file = mList.get(position).getFile();
        if (file == null) {
            String id = mList.get(position).getID();
            String filepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download";
            String filename = mList.get(position).getFileName();
            File newFile = new File(filepath, filename);
            if (file != null && file.exists()) {
                CallOtherOpenFile.openFile(mContext, file);
                file.deleteOnExit();
            } else {
                OAService.downloadAttachment(id, new FileCallBack(filepath, filename) {
                    @Override
                    public void syncSaveToSQL(File file) {

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        if (response != null) {
                            CallOtherOpenFile.openFile(mContext, response);
                        }
                    }
                });
            }
        } else {
            CallOtherOpenFile.openFile(mContext, mList.get(position).getFile());
        }
    }

    @Override
    protected void onTextSubmit(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        ArrayList<Annex> list = new ArrayList<>();
        for (Annex annex : mList) {
            if (annex.getFileName().contains(str)) {
                list.add(annex);
            }
        }
        adapter.setList(list);
    }

    @Override
    protected void onSearchClose() {
        adapter.setList(mList);
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void doLoadMore() {

    }
}