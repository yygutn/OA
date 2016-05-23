package cn.edu.jumy.oa.timchat.ui.customview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.tencent.TIMCallBack;
import com.tencent.qcloud.presentation.business.LoginBusiness;
import cn.edu.jumy.oa.R;
import com.tencent.qcloud.tlslibrary.service.TlsBusiness;

import cn.edu.jumy.oa.timchat.model.FriendshipInfo;
import cn.edu.jumy.oa.timchat.model.GroupInfo;
import cn.edu.jumy.oa.timchat.model.UserInfo;
import cn.edu.jumy.oa.timchat.ui.SplashActivity;

public class DialogActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_dialog);
        setFinishOnTouchOutside(false);

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOk) {
            LoginBusiness.loginIm(UserInfo.getInstance().getId(), UserInfo.getInstance().getUserSig(), new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    Toast.makeText(DialogActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                    logout();
                }

                @Override
                public void onSuccess() {
                    Toast.makeText(DialogActivity.this, getString(R.string.login_succ), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });

        } else if (i == R.id.btnCancel) {
            logout();

        }
    }

    private void logout(){
        TlsBusiness.logout(UserInfo.getInstance().getId());
        UserInfo.getInstance().setId(null);
        FriendshipInfo.getInstance().clear();
        GroupInfo.getInstance().clear();
        Intent intent = new Intent(this,SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
