package com.my.app.glassapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.LoginUser;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    MutableLiveData<LoginUser> liveData;

    public LoginViewModel() {
        liveData=new MutableLiveData<>();
    }

    public MutableLiveData<LoginUser> getCreatedUserObserver(){
        return liveData;
    }
    public void apiCall(Map<String,Object> map){
        RetrofitService service = RetrofitClient.getClient();
        service.login(map).enqueue(new Callback<LoginUser>() {
            @Override
            public void onResponse(Call<LoginUser> call, Response<LoginUser> response) {
                if (response.isSuccessful()){
                    liveData.postValue(response.body());
                }else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginUser> call, Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}
