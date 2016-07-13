package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.DocResponse;
import okhttp3.Call;
import okhttp3.Response;

public abstract class DocCallback extends Callback<DocResponse> {

        @Override
        public DocResponse parseNetworkResponse(Response response, int id) throws Exception {
            String data = response.body().string();
            Gson gson = new Gson();
            BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
            if (baseResponse.code==0) {
                return gson.fromJson(data, DocResponse.class);
            } else {
                return new DocResponse(baseResponse.msg,baseResponse.code,null);
            }
        }

        @Override
        public void onError(Call call, Exception e, int id) {
            BaseActivity.showDebugException(e);
            Toast.makeText(MyApplication.getContext(),"网络异常,获取公文失败",Toast.LENGTH_SHORT).show();
        }
    }