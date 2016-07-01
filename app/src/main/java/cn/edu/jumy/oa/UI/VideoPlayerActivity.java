package cn.edu.jumy.oa.UI;

import android.support.v7.widget.Toolbar;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.R;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * 视频播放
 * Created by Jumy on 16/7/1 14:08.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
@EActivity(R.layout.activity_video_player)
public class VideoPlayerActivity extends BaseActivity{
    @ViewById(R.id.title_bar)
    protected Toolbar mTitleBar;
    @ViewById(R.id.player)
    protected JCVideoPlayerStandard mPlayer;

    @AfterViews
    void go(){
        mTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToPreActivity();
            }
        });
        initPlayer();
    }

    private void initPlayer() {
        mPlayer.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4","233");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPlayer.release();
    }
}
