package com.hally.influencerai.managers.network;

import com.hally.influencerai.model.Login;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
public interface APIService {

    @GET("/api/v1/user_login_api")
    Call<Login> getServiceInfo();

//    @GET("/api/v1/user_update_info_api")
//    Call<ServiceInfo> getServiceInfo();
}
