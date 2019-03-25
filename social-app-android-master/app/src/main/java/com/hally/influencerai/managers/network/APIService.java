package com.hally.influencerai.managers.network;

import com.hally.influencerai.model.Login;
import com.hally.influencerai.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public interface APIService {

    @GET("/api/v1/user_login_api")
    Call<Login> getLoginInfo(@Header("Authorization") String credentials);

    @POST("/api/v1/user_register_api")
    Call<User> registerUser(@Body User user);
}
