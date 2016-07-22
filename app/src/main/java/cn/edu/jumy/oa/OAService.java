package cn.edu.jumy.oa;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import cn.edu.jumy.jumyframework.BaseActivity;
import cn.edu.jumy.oa.Response.BaseResponse;
import cn.edu.jumy.oa.Response.DateResponse;
import cn.edu.jumy.oa.Response.DocResponse;
import cn.edu.jumy.oa.Response.MeetResponse;
import cn.edu.jumy.oa.safe.PasswordUtil;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.Response;

/**
 * Created by Jumy on 16/7/8 17:40.
 * Copyright (c) 2016, yygutn@gmail.com All Rights Reserved.
 */
public class OAService {

    public static String zip = "";
    public static String base = "";
    public static String username = EMClient.getInstance().getCurrentUser() + "_";
    public static final String BASE_URL_ONLINE = "http://121.41.102.69:8080/OA_console/phone/";
    public static final String BASE_URL_TEST = "http://192.168.3.124:8090/OA_console/phone/";

    public static final String BASE_URL = BASE_URL_ONLINE;
    public abstract static class DateCallBack extends Callback<String> {

        @Override
        public String parseNetworkResponse(Response response, int id) throws Exception {
            Gson gson = new Gson();
            DateResponse dateResponse = gson.fromJson(response.body().string(), DateResponse.class);
            if (dateResponse.code == 0) {
                base = username + dateResponse.data;
                zip = PasswordUtil.simpleEncpyt(base);
                return zip;
            } else {
                return null;
            }
        }
    }
    /**
     getTime(new DateCallBack() {
    @Override public void onError(Call call, Exception e, int id) {
    callback.onError(call,e,id);
    }

    @Override public void onResponse(String response, int id) {

    }
    });
     */

    /**
     * 获取服务器时间
     *
     * @param callback 回调
     */
    public static void getTime(Callback callback) {
        OkHttpUtils.post()
                .url(BASE_URL + "getTime")
                .addParams("","")
                .build()
                .execute(callback);
    }

    /**
     * 公文接收
     *
     * @param params   参数
     * @param callback 回调
     */
    public static void docReceive(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {

                params.put("value", response);

                OkHttpUtils.post()
                        .url(BASE_URL + "docReceive")
                        .params(params)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 公文发送
     *
     * @param params   参数
     * @param fileMap  附件表
     * @param callback 回调
     */
    public static void docSend(final Map<String, String> params, final Map<String, File> fileMap, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {

                params.put("value", response);

                OkHttpUtils.post()
                        .url(BASE_URL + "docSend")
                        .params(params)
                        .files("upfile", fileMap)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 获取当前用户可以发送公文的组织单位
     *
     * @param callback
     */
    public static void getOrganizationData(final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {

                OkHttpUtils.post()
                        .url(BASE_URL + "getOrganizationData")
                        .addParams("value", response)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 获取附件列表
     *
     * @param params
     * @param callback
     */
    public static void getAttachmentList(final Map<String, String> params, final Callback callback) {

        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "getAttach")
                        .params(params)
                        .build()
                        .execute(callback);
            }
        });

    }

    /**
     * 下载附件
     *
     * @param ids
     * @param callback
     */
    public static void downloadAttachment(final String ids, final Callback callback) {
        try {
            getTime(new DateCallBack() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    callback.onError(call, e, id);
                }

                @Override
                public void onResponse(String response, int id) {
                    OkHttpUtils.get()
                            .url(BASE_URL + "download")
                            .addParams("value", response)
                            .addParams("id", ids)
                            .build()
                            .execute(callback);
                }
            });
        } catch (Exception e) {
            BaseActivity.showDebugException(e);
        }
    }

    /**
     * 会议通知
     *
     * @param params
     * @param callback
     */
    public static void meetReceive(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "meetReceive")
                        .params(params)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 会议发布
     *
     * @param params
     * @param fileMap
     * @param callback
     */
    public static void meetSend(final Map<String, String> params, final Map<String, File> fileMap, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "meetSend")
                        .params(params)
                        .files("upfile", fileMap)
                        .build().execute(callback);
            }
        });
    }

    /**
     * 已发送公文
     *
     * @param params
     * @param callback
     */
    public static void docUser(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "docUser")
                        .params(params)
                        .build().execute(callback);
            }
        });
    }

    /**
     * 已发布会议
     *
     * @param params
     * @param callback
     */
    public static void meetUser(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "meetUser")
                        .params(params)
                        .build().execute(callback);
            }
        });
    }

    /**
     * 公文签收
     *
     * @param tid      任务ID
     * @param callback
     */
    public static void docSign(final String tid, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "docSign")
                        .addParams("value", response)
                        .addParams("tid", tid)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 获取签收信息-列表
     *
     * @param pid      公文ID||会议ID
     * @param callback
     */
    public static void getCheckInfo(final String pid, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "getCheckInfo")
                        .addParams("value", response)
                        .addParams("pid", pid)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 已发布会议
     *
     * @param params
     * @param callback
     */
    public static void meetCompany(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "meetCompany")
                        .params(params)
                        .build().execute(callback);
            }
        });
    }

    /**
     * 承办单位获取会议报名表
     *
     * @param mid      会议ID
     * @param callback
     */
    public static void getMEntryByPassStatus(final String mid, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "getMEntryByPassStatus")
                        .addParams("value", response)
                        .addParams("mid", mid)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 会议承办方审核人员报名，通过/退回
     * @param id 报名人员id
     * @param pass (true:通过  false:不通过)
     * @param passRemark 审批反馈(不通过时)
     * @param callback
     */
    public static void meetUserPass(final String id,final String pass,final String passRemark, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int sid) {
                OkHttpUtils.post()
                        .url(BASE_URL + "meetUserPass")
                        .addParams("value", response)
                        .addParams("id", id)
                        .addParams("pass", pass)
                        .addParams("passRemark", passRemark)
                        .build()
                        .execute(callback);
            }
        });
    }


    public static void meetSign(final String tid, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "meetSign")
                        .addParams("value", response)
                        .addParams("tid", tid)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     * 公文催收
     * @param oid 组织id
     * @param did 公文id
     * @param callback 回调
     */
    public static void docUrge(final String oid,final String did ,final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "docUrge")
                        .addParams("value", response)
                        .addParams("oid", oid)
                        .addParams("did", did)
                        .build()
                        .execute(callback);
            }
        });
    }
    /**
     * 会议催收
     * @param oid 组织id
     * @param did 会议id
     * @param callback 回调
     */
    public static void meetUrge(final String oid,final String did ,final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "meetUrge")
                        .addParams("value", response)
                        .addParams("oid", oid)
                        .addParams("mid", did)
                        .build()
                        .execute(callback);
            }
        });
    }
    /**
     * 添加修改报名人员
     */
    public static void updateMEntry(final Map<String, String> params, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                params.put("value", response);
                OkHttpUtils.post()
                        .url(BASE_URL + "updateMEntry")
                        .params(params)
                        .build().execute(callback);
            }
        });
    }

    /**
     * 获取单个报名人员信息
     * @param id
     * @param callback
     */
    public static void getMEntry(final String id, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int ids) {
                callback.onError(call, e, ids);
            }

            @Override
            public void onResponse(String response, int ids) {
                OkHttpUtils.post()
                        .url(BASE_URL + "getMEntry")
                        .addParams("value", response)
                        .addParams("id", id)
                        .build()
                        .execute(callback);
            }
        });
    }

    /**
     *获取接收单位会议的报名表
     */
    public static void getMEntryByPid(final String pid, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int ids) {
                callback.onError(call, e, ids);
            }

            @Override
            public void onResponse(String response, int ids) {
                OkHttpUtils.post()
                        .url(BASE_URL + "getMEntryByPid")
                        .addParams("value", response)
                        .addParams("pid", pid)
                        .build()
                        .execute(callback);
            }
        });
    }
    /**
     *添加常用单位组
     */
    public static void insertMagroup(final String ids,final String name,final int sort, final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "insertMagroup")
                        .addParams("value", response)
                        .addParams("ids", ids)
                        .addParams("name", name)
                        .addParams("sort", sort+"")
                        .build()
                        .execute(callback);
            }
        });
    }
   /**
     *获取常用单位组
     */
    public static void getMagroupAll(final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "getMagroupAll")
                        .addParams("value", response)
                        .addParams("select", "all")
                        .addParams("id", "")
                        .build()
                        .execute(callback);
            }
        });
    }
    /**
     *修改常用单位组
     */
    public static void updateMagroup(final String Id,final String ids,final String name,final int sort,final Callback callback) {
        getTime(new DateCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                callback.onError(call, e, id);
            }

            @Override
            public void onResponse(String response, int id) {
                OkHttpUtils.post()
                        .url(BASE_URL + "updateMagroup")
                        .addParams("value", response)
                        .addParams("id", Id)
                        .addParams("ids", ids)
                        .addParams("name", name)
                        .addParams("sort", sort+"")
                        .build()
                        .execute(callback);
            }
        });
    }

}
