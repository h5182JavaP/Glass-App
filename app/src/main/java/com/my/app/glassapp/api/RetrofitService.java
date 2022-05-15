package com.my.app.glassapp.api;


import com.my.app.glassapp.api.model.InquiryData;
import com.my.app.glassapp.api.model.LoginUser;
import com.my.app.glassapp.api.model.RegisterUser;
import com.my.app.glassapp.model.AllSalesPersonItem;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RetrofitService {

    @GET("api/users/getAllSalesPerson")
    Call<List<AllSalesPersonItem>> getAllUser();

    @POST("api/login")
    Call<LoginUser> login(@QueryMap Map<String, Object> map);


    @POST("api/auth")
    Call<RegisterUser> register(@Query("email") String email,
                                @Query("username") String username,
                                @Query("password") String password,
                                @Query("type") String type,
                                @Query("name") String name,
                                @Query("address") String address,
                                @Query("reference") String reference,
                                @Query("mobile_no") String mobile_no,
                                @Query("gst") String gst
    );

    //    @Headers({"Content-Type: application/json"})
    @Multipart
    @POST("api/inquiry")
    Call<InquiryData> inquiry(
            @Header("Authorization") String authHeader,
            @Part MultipartBody.Part[] file,
            @PartMap() Map<String, RequestBody> map
    );
}
