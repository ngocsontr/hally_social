package com.hally.influencerai.managers.network;

import android.content.Context;

import com.hally.influencerai.Constants;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public class ApiUtils {

    public static final String BASE_URL_DEBUG = "http://35.236.66.95/";
    public static final String BASE_URL = "http://35.236.66.95/";

    public static APIService getAPIService(Context context) {
        return RetrofitClient.getClient(Constants.DEBUG ? BASE_URL_DEBUG : BASE_URL, context)
                .create(APIService.class);
    }

}