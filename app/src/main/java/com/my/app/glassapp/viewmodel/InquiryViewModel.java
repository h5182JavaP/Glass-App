package com.my.app.glassapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.InquiryData;
import com.my.app.glassapp.api.model.LoginUser;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InquiryViewModel extends ViewModel {

    MutableLiveData<InquiryData> liveData;

    public InquiryViewModel() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<InquiryData> getCreatedUserObserver() {
        return liveData;
    }

    public void apiCall(Map<String, RequestBody> map, String token, MultipartBody.Part path) {
//    public void apiCall(Map<String, RequestBody> map, String token) {
        RetrofitService service = RetrofitClient.getClient();
//        File file=new File(path);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), path);
//        MultipartBody.Part multipartBody =MultipartBody.Part.createFormData("order[0][order_items][2][image]",file.getName(),requestFile);
        service.inquiry(token,map, path ).enqueue(new Callback<InquiryData>() {
//        service.inquiry(map,  token).enqueue(new Callback<InquiryData>() {
            @Override
            public void onResponse(Call<InquiryData> call, Response<InquiryData> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<InquiryData> call, Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}
