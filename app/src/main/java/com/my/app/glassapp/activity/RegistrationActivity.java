package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.my.app.glassapp.R;
import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.RegisterUser;
import com.my.app.glassapp.databinding.ActivityRegistrationBinding;
import com.my.app.glassapp.model.AllUsersResponse;
import com.my.app.glassapp.viewmodel.RegistrationViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityRegistrationBinding binding;
    private RegistrationViewModel viewModel;
    //    String[] country = {"India", "USA", "China", "Japan", "Other"};
    private String referenceUserName;
    List<String> referenceUserList = new ArrayList<>();
    String referencePosition="";
    private AllUsersResponse model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();
        fetchData();
        setAllUsersSpinnerAdapter();

        binding.cvRegister.setOnClickListener(view -> {
            for (int i = 0; i < referenceUserList.size(); i++) {
                if (binding.autoCompleteTextView.getText().toString().equals(model.getUsers().get(i).getName())) {
                    referencePosition = model.getUsers().get(i).getId();
                }
            }
            if (TextUtils.isEmpty(binding.etRgEmail.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "No Email Provided!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isValidEmail(binding.etRgEmail.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "Email is invalid!", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etRgPassword.getText().toString())) {
                Toast.makeText(RegistrationActivity.this, "No Password Entered!", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etRgMobile.getText().toString().length() < 10) {
                Toast.makeText(RegistrationActivity.this, "Please Enter valid number", Toast.LENGTH_SHORT).show();
            } else if (binding.etRgMobile.getText().toString().length() > 10) {
                Toast.makeText(RegistrationActivity.this, "Please Enter valid number", Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
                binding.autoCompleteTextView.getText().toString();

                viewModel.apiCall(binding.etRgEmail.getText().toString(),
                        binding.etRgName.getText().toString(),
                        binding.etRgPassword.getText().toString(),
                        "user",
                        binding.etRgName.getText().toString(),
                        binding.etLrgAddress.getText().toString(),
                        referencePosition,
                        binding.etRgMobile.getText().toString(),
                        binding.etRgGst.getText().toString());
            }
        });

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    public void setAllUsersSpinnerAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, referenceUserList);
        //Getting the instance of AutoCompleteTextView
        binding.autoCompleteTextView.setThreshold(1);//will start working from first character
        binding.autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.autoCompleteTextView.setTextColor(Color.BLACK);
    }

    private void fetchData() {
        RetrofitService service = RetrofitClient.getClient();
        Call<AllUsersResponse> call = service.getAllUser();
        call.enqueue(new Callback<AllUsersResponse>() {
            @Override
            public void onResponse(Call<AllUsersResponse> call, Response<AllUsersResponse> response) {
                model = response.body();
                Log.i("TAG", "onResponse: " + model.getUsers());

                for (int i = 0; i < model.getUsers().size(); i++) {
                    referenceUserName = model.getUsers().get(i).getName();
                    referenceUserList.add(referenceUserName);
                }
            }

            @Override
            public void onFailure(Call<AllUsersResponse> call, Throwable t) {
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
                if (registerUser == null) {
                    Toast.makeText(RegistrationActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                } else {
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
            }
        });
    }

    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
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