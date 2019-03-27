package com.hally.influencerai.managers.network;

import com.hally.influencerai.model.Login;
import com.hally.influencerai.model.RegisterUserRes;
import com.hally.influencerai.model.UpdateUserRes;
import com.hally.influencerai.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
interface APIService {

    @GET("/api/v1/user_login_api")
    Call<Login> login(@Field("jwt") String jwt);

    @POST("/api/v1/user_register_api")
    Call<List<RegisterUserRes>> registerUser(@Body User user);

    @POST("/api/v1/user_update_info_api")
    Call<UpdateUserRes> updateUser(@Body User user);

}
