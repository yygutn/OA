package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chatui.ui.BaseActivity;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.RelayResponse;
import okhttp3.Call;
import okhttp3.Response;

public abstract class RelayCallback extends Callback<RelayResponse> {
    @Override
    public RelayResponse parseNetworkResponse(Response response, int id) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
        if (baseResponse.code == 0) {
            return gson.fromJson(data, RelayResponse.class);
        } else {
            return new RelayResponse(baseResponse);
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        BaseActivity.showDebugException(e);
        Toast.makeText(MyApplication.getContext(), "获取列表失败", Toast.LENGTH_SHORT).show();
    }
}