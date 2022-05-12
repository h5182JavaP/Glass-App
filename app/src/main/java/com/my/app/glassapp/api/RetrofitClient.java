package com.my.app.glassapp.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    https://dg.i2i-info.com/

    public static final String BASE_URL = "https://dg.i2i-info.com/";
    public static RetrofitService service;

    public static RetrofitService getClient() {
        if (service == null) {
/*
            OkHttpClient httpClient = new OkHttpClient();
            httpClient.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Authorization", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkdXRjaF9nbGFzcyIsInVzZXIiOnsiZW1haWwiOiJzdWdhckBnbWFpbC5jb20iLCJhdXRoX2lkIjoiMSIsInVzZXJfaWQiOiI0MSJ9LCJpYXQiOjE2NTE5MTA4NjYsIm5iZiI6MTAwMTAsImV4cCI6MTY1MTkyMDg2Nn0.X9mvErsrvURXrwSlTDS7HtlZf0oX-m_KeVxNIRU9POF0zV78u2X23jbDyXTvM3V7MszcAXBN0-B_a-svB2K64w");
//                    Request request = new Request.Builder()
//                            .url("https://192.168.29.108:8080/api/inquiry")
//                            .method("POST", body)
//                            .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkdXRjaF9nbGFzcyIsInVzZXIiOnsiZW1haWwiOiJkZ2xhc3MxNkBnbWFpbC5jb20iLCJhdXRoX2lkIjoiMiIsInVzZXJfaWQiOiI0MiJ9LCJpYXQiOjE2NTE3NDg0NTUsIm5iZiI6MTAwMTAsImV4cCI6MTY1MTc1ODQ1NX0.3W7Ui1EbQ102LDuqrKxGUwoBlyarFYskFAHwzdUxd1qqZg1dKanNBzzCFIc3AixhP1Fg7eG7TpHYwZuFmPgcpg")
//                            .build();
                    return chain.proceed(requestBuilder.build());
                }
            });*/

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
//                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            service = retrofit.create(RetrofitService.class);
        }
        return service;
    }

    ;

}
