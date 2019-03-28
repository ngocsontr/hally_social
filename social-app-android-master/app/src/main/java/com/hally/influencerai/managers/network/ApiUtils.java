package com.hally.influencerai.managers.network;

import android.content.Context;

import com.hally.influencerai.Constants;
import com.hally.influencerai.model.Login;
import com.hally.influencerai.model.RegisterUserRes;
import com.hally.influencerai.model.UpdateUserRes;
import com.hally.influencerai.model.User;
import com.hally.influencerai.utils.SharePreUtil;

import retrofit2.Callback;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public class ApiUtils {

    private static final String BASE_URL_DEBUG = "http://35.236.66.95/";
    private static final String BASE_URL = "http://35.236.66.95/";

    private static APIService getAPIService(Context context) {
        return RetrofitClient.getClient(Constants.DEBUG ? BASE_URL_DEBUG : BASE_URL, context)
                .create(APIService.class);
    }

    public static void login(Context context, String jwt, Callback<Login> callback) {
        getAPIService(context).login(jwt).enqueue(callback);
    }

    public static void registerUser(Context context, User user, Callback<RegisterUserRes> callback) {
        getAPIService(context).registerUser(user).enqueue(callback);
    }

    public static void updateUser(Context context, User user, Callback<UpdateUserRes> callback) {
        String token = SharePreUtil.getLoginToken(context);
        getAPIService(context).updateUser(token, user).enqueue(callback);
    }
}