package com.hally.influencerai.managers.network;

import com.hally.influencerai.model.LoginRes;
import com.hally.influencerai.model.RegisterUserRes;
import com.hally.influencerai.model.UpdateUserRes;
import com.hally.influencerai.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
interface APIService {

    @POST("/api/v1/user_login_api")
    Call<LoginRes> login(@Header("Authorization") String jwt);

    @POST("/api/v1/user_register_api")
    Call<RegisterUserRes> registerUser(@Body User user);

    @POST("/api/v1/user_update_info_api")
    Call<UpdateUserRes> updateUser(@Header("Authorization") String token, @Body User user);

}
