package com.hally.influencerai.managers.network;

import android.content.Context;
import android.provider.Settings;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HallyTran on 3/25/2019.
 * transon97uet@gmail.com
 */
class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl, Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("UUID", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID))
                                .addHeader("X-Requested-With", "XMLHttpRequest")
                                .build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)).build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
