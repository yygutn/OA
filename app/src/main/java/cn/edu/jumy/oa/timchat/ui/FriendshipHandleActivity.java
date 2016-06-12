package cn.edu.jumy.oa.timchat.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMFriendResult;
import com.tencent.TIMValueCallBack;
import com.tencent.qcloud.presentation.presenter.FriendshipManagerPresenter;
import com.tencent.qcloud.tlslibrary.activity.BaseActivity;

import cn.edu.jumy.oa.R;

public class FriendshipHandleActivity extends BaseActivity implements View.OnClickListener {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendship_handle);
        id = getIntent().getStringExtra("id");
        TextView tvName = (TextView) findViewById(R.id.name);
        tvName.setText(id);
        String word = getIntent().getStringExtra("word");
        TextView tvWord = (TextView) findViewById(R.id.word);
        tvWord.setText(word);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnReject) {
            FriendshipManagerPresenter.refuseFriendRequest(id, new TIMValueCallBack<TIMFriendResult>() {
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(FriendshipHandleActivity.this, getString(R.string.operate_fail), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(TIMFriendResult timFriendResult) {
                    Intent intent = new Intent();
                    intent.putExtra("operate", false);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        } else if (i == R.id.btnAgree) {
            FriendshipManagerPresenter.acceptFriendRequest(id, new TIMValueCallBack<TIMFriendResult>() {
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(FriendshipHandleActivity.this, getString(R.string.operate_fail), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(TIMFriendResult timFriendResult) {
                    Intent intent = new Intent();
                    intent.putExtra("operate", true);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });

        }
    }
}
