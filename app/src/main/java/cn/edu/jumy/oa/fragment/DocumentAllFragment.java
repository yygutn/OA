package cn.edu.jumy.oa.fragment;

import org.androidannotations.annotations.EFragment;

import cn.edu.jumy.oa.R;
import cn.edu.jumy.oa.bean.Node;

/**
 * Created by Jumy on 16/6/22 10:17.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EFragment(R.layout.fragment_document_all)
public class DocumentAllFragment extends BaseSearchRefreshFragment {
    @Override
    protected void initList() {
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
        mList.add(new Node());
    }
}