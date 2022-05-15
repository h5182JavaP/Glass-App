package com.my.app.glassapp.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.RegisterUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationViewModel extends ViewModel {

    MutableLiveData<RegisterUser> liveData;

    public RegistrationViewModel() {
        liveData = new MutableLiveData<>();
    }

    public MutableLiveData<RegisterUser> getCreatedUserObserver() {
        return liveData;
    }

    public void apiCall(String email, String userName, String password, String type, String name, String address,
                        String reference, String mobileNum, String gst) {

        RetrofitService service = RetrofitClient.getClient();
        service.register(email, userName, password, type, name, address, reference, mobileNum, gst).enqueue(new Callback<RegisterUser>() {
            @Override
            public void onResponse(Call<RegisterUser> call, Response<RegisterUser> response) {
                if (response.isSuccessful()) {
                    liveData.postValue(response.body());
                } else {
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterUser> call, Throwable t) {
                liveData.postValue(null);
            }
        });
    }
}
