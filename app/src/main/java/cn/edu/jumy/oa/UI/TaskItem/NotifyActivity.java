package cn.edu.jumy.oa.UI.TaskItem;

import android.content.res.Resources;

import org.androidannotations.annotations.EActivity;

import java.util.Arrays;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.Utils.CardGenerator;

/**
 * Created by Jumy on 16/6/27 16:18.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_document_cabinet)
public class NotifyActivity extends BaseSearchRefreshActivity{
    @Override
    protected void setTile() {
        mTitleBar.setTitle("公告");
    }

    @Override
    protected void initData() {
        Resources resources = getResources();
        String [] test1 = resources.getStringArray(R.array.test1);
        String [] test2 = resources.getStringArray(R.array.test2);
        String [] test3 = resources.getStringArray(R.array.test3);
        String [] test4 = resources.getStringArray(R.array.test4);
        String [] test5 = resources.getStringArray(R.array.test5);
        String [] test6 = resources.getStringArray(R.array.test6);

        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test1),2));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test2),2));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test3),2));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test4),2));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test5),2));
        mList.add(CardGenerator.getStringFromArray(Arrays.asList(test6),2));
    }

    @Override
    protected void onTextSubmit(String str) {
        super.onTextSubmit(str);
    }

    @Override
    protected void doRefresh() {

    }

    @Override
    protected void doLoadMore() {

    }
}
