package cn.edu.jumy.oa.CallBack;

import android.widget.Toast;

import com.google.gson.Gson;
import com.zhy.http.okhttp.callback.Callback;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.MyApplication;
import cn.edu.jumy.oa.Response.AuditResponse;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.DocResponse;
import okhttp3.Call;
import okhttp3.Response;

/**
 * User: Jumy (yygutn@gmail.com)
 * Date: 16/7/13  下午9:54
 */
public abstract class AuditCallback extends Callback<AuditResponse>{
    @Override
    public AuditResponse parseNetworkResponse(Response response, int id) throws Exception {
        String data = response.body().string();
        Gson gson = new Gson();
        BaseResponse baseResponse = gson.fromJson(data,BaseResponse.class);
        if (baseResponse.code==0) {
            return gson.fromJson(data, AuditResponse.class);
        } else {
            return new AuditResponse(baseResponse.msg,baseResponse.code);
        }
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        BaseActivity.showDebugException(e);
        Toast.makeText(MyApplication.getContext(),"当前网络不可用,获取列表失败",Toast.LENGTH_SHORT).show();
    }
}
