package com.which.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tomeramir on 27/08/2016.
 */
public class ServerConnection {
    private static final String SERVER_URI = "http://172.16.24.160:8080";

    private static Retrofit createRetrofit() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(SERVER_URI)
                .build();
    }

    public static IdentityAPI createIdentityAPI() {
        return createRetrofit().create(IdentityAPI.class);
    }
}
