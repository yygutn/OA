package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chatui.ui.BaseActivity;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.MeetResponse;
import okhttp3.Call;
import okhttp3.Response;

public abstract class MeetCallback extends Callback<MeetResponse> {
        @Override
        public MeetResponse parseNetworkResponse(Response response, int id) throws Exception {
            synchronized (response) {
                try {
                    String data = response.body().string();
                    Gson gson = new Gson();
                    BaseResponse baseResponse = gson.fromJson(data, BaseResponse.class);
                    if (baseResponse.code == 0) {
                        return gson.fromJson(data, MeetResponse.class);
                    } else if (baseResponse.code == 1) {
                        return new MeetResponse(baseResponse);
                    }
                } catch (Exception e) {
                    onError(null, e, 0);
                }
                return null;
            }
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            BaseActivity.showDebugException(e);
            Toast.makeText(MyApplication.getContext(),"获取会议列表失败",Toast.LENGTH_SHORT).show();
        }
    }