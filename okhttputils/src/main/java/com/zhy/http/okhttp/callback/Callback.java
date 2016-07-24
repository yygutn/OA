package com.zhy.http.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

public abstract class Callback<T>
{
    /**
     * UI Thread
     *
     * @param request
     */
    public void onBefore(Request request, int ID)
    {
    }

    /**
     * UI Thread
     *
     * @param
     */
    public void onAfter(int ID)
    {
    }

    /**
     * UI Thread
     *
     * @param progress
     */
    public void inProgress(float progress, long total , int ID)
    {

    }

    /**
     * if you parse reponse code in parseNetworkResponse, you should make this method return true.
     *
     * @param response
     * @return
     */
    public boolean validateReponse(Response response, int ID)
    {
        return response.isSuccessful();
    }

    /**
     * Thread Pool Thread
     *
     * @param response
     */
    public abstract T parseNetworkResponse(Response response, int ID) throws Exception;

    public abstract void onError(Call call, Exception e, int ID);

    public abstract void onResponse(T response, int ID);


    public static Callback CALLBACK_DEFAULT = new Callback()
    {

        @Override
        public Object parseNetworkResponse(Response response, int ID) throws Exception
        {
            return null;
        }

        @Override
        public void onError(Call call, Exception e, int ID)
        {

        }

        @Override
        public void onResponse(Object response, int ID)
        {

        }
    };

}