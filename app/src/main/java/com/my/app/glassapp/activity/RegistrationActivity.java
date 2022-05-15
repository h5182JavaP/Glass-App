package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.my.app.glassapp.R;
import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.RegisterUser;
import com.my.app.glassapp.databinding.ActivityRegistrationBinding;
import com.my.app.glassapp.model.AllSalesPersonItem;
import com.my.app.glassapp.viewmodel.RegistrationViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityRegistrationBinding binding;
    private RegistrationViewModel viewModel;
    private String referenceUserName;
    List<String> referenceUserList = new ArrayList<>();
    private List<AllSalesPersonItem> model;
    private int referenceUserPos;
    private ArrayAdapter<String> dataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showLoading();

        initViewModel();
        fetchData();
        setAllUsersSpinnerAdapter();

        binding.cvRegister.setOnClickListener(view -> {

            if (TextUtils.isEmpty(binding.etRgName.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etRgEmail.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isValidEmail(binding.etRgEmail.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etLrgAddress.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etRgMobile.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etRgMobile.getText().toString().length() < 10) {
                Toast.makeText(RegistrationActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            } else if (binding.etRgMobile.getText().toString().length() > 10) {
                Toast.makeText(RegistrationActivity.this, "Please enter valid mobile number", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(binding.etRgPassword.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etRgPassword.getText().toString().length() < 8) {
                Toast.makeText(RegistrationActivity.this, "Please enter minimum 8 characters of password", Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
//                binding.autoCompleteTextView.getText().toString();

                viewModel.apiCall(binding.etRgEmail.getText().toString(),
                        binding.etRgName.getText().toString(),
                        binding.etRgPassword.getText().toString(),
                        "user",
                        binding.etRgName.getText().toString(),
                        binding.etLrgAddress.getText().toString(),
                        model.get(referenceUserPos).getId(),
//                        referencePosition,
                        binding.etRgMobile.getText().toString(),
                        binding.etRgGst.getText().toString());
            }
        });

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    public void setAllUsersSpinnerAdapter() {
        dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, referenceUserList);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spReferenceName.setAdapter(dataAdapter);

        binding.spReferenceName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                referenceUserPos = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dataAdapter.notifyDataSetChanged();
    }

    private void fetchData() {
        RetrofitService service = RetrofitClient.getClient();
        Call<List<AllSalesPersonItem>> call = service.getAllUser();
        call.enqueue(new Callback<List<AllSalesPersonItem>>() {
            @Override
            public void onResponse(Call<List<AllSalesPersonItem>> call, Response<List<AllSalesPersonItem>> response) {
                model = response.body();
                if (model != null)
                    Log.i("TAG", "onResponse: " + response.body());
                for (int i = 0; i < model.size(); i++) {
                    referenceUserName = model.get(i).getName();
                    referenceUserList.add(referenceUserName);
                }
                dataAdapter.notifyDataSetChanged();
                hideLoading();
            }

            @Override
            public void onFailure(Call<List<AllSalesPersonItem>> call, Throwable t) {
                Toast.makeText(RegistrationActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        viewModel.getCreatedUserObserver().observe(this, new Observer<RegisterUser>() {
            @Override
            public void onChanged(RegisterUser registerUser) {
                hideLoading();
                if (registerUser != null) {
                    if (registerUser.getStatus() == 500) {
                        if (registerUser.getMessage().getEmail() != null)
                            Toast.makeText(RegistrationActivity.this, "" + registerUser.getMessage().getEmail(), Toast.LENGTH_SHORT).show();
                        else if (registerUser.getMessage().getMobileNo() != null)
                            Toast.makeText(RegistrationActivity.this, "" + registerUser.getMessage().getMobileNo(), Toast.LENGTH_SHORT).show();
                    } else if (registerUser.getStatus() == 200) {
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putBoolean("login", true);
                        myEdit.commit();
                        Toast.makeText(RegistrationActivity.this, "" + registerUser.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                        intent.putExtra("emailId", binding.etRgEmail.getText().toString());
                        intent.putExtra("password", binding.etRgPassword.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(RegistrationActivity.this, "Fail to load server", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showLoading() {
        binding.rgProgress.setVisibility(View.VISIBLE);
        binding.rgLay.setVisibility(View.GONE);
        binding.cvRegister.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.rgProgress.setVisibility(View.GONE);
        binding.rgLay.setVisibility(View.VISIBLE);
        binding.cvRegister.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}