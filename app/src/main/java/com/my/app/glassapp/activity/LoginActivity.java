package com.my.app.glassapp.activity;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;

import com.my.app.glassapp.api.model.LoginUser;
import com.my.app.glassapp.databinding.ActivityLoginBinding;
import com.my.app.glassapp.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String email = intent.getStringExtra("emailId");
        String password = intent.getStringExtra("password");

        if (email != null)
            binding.etLoginEmail.setText(email);
        if (password != null)
            binding.etLoginPassword.setText(password);

        initViewModel();

        binding.cvLogin.setOnClickListener(view -> {

            if (TextUtils.isEmpty(binding.etLoginEmail.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please enter email address", Toast.LENGTH_SHORT).show();
                return;
            } else if (!isValidEmail(binding.etLoginEmail.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etLoginPassword.getText().toString())) {
                Toast.makeText(LoginActivity.this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etLoginPassword.getText().toString().length() < 8) {
                Toast.makeText(LoginActivity.this, "Please enter minimum 8 characters", Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
                Map<String, Object> map = new HashMap<>();
                map.put("email", binding.etLoginEmail.getText().toString());
                map.put("password", binding.etLoginPassword.getText().toString());
                viewModel.apiCall(map);
            }
        });

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.getCreatedUserObserver().observe(this, new Observer<LoginUser>() {
            @Override
            public void onChanged(LoginUser loginUser) {
                hideLoading();
                if (loginUser == null) {
                    Toast.makeText(LoginActivity.this, "Incorrect email & password", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences sharedPreferences1 = getSharedPreferences("TokenPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit1 = sharedPreferences1.edit();
                    myEdit1.putString("token", loginUser.getData().getAccessToken());
                    myEdit1.commit();

                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putBoolean("login", true);
                    myEdit.commit();
                    Toast.makeText(LoginActivity.this, "" + loginUser.getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, OrderListActivity.class));
                    finish();
                }
            }
        });
    }


/*
    public boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
//        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
*/

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void showLoading() {
        binding.loginProgress.setVisibility(View.VISIBLE);
        binding.loginLay.setVisibility(View.GONE);
        binding.cvLogin.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.loginProgress.setVisibility(View.GONE);
        binding.loginLay.setVisibility(View.VISIBLE);
        binding.cvLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }
}
//9824103200
//        9104693195