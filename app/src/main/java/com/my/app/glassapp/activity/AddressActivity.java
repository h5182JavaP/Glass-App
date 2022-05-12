package com.my.app.glassapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.my.app.glassapp.UploadService;
import com.my.app.glassapp.api.model.LoginUser;
import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.api.RetrofitClient;
import com.my.app.glassapp.api.RetrofitService;
import com.my.app.glassapp.api.model.InquiryData;
import com.my.app.glassapp.database.DBDao;
import com.my.app.glassapp.database.DBRoom;
import com.my.app.glassapp.databinding.ActivityAddressBinding;
import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;
import com.my.app.glassapp.viewmodel.InquiryViewModel;
import com.my.app.glassapp.viewmodel.LoginViewModel;

import net.gotev.uploadservice.MultipartUploadRequest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class AddressActivity extends AppCompatActivity {

    ActivityAddressBinding binding;
    private List<SGUTable> sguData;
    private List<DGUTable> dguData;
    private List<LaminationTable> laminationData;
    private List<AnnealedTable> annealedData;
    private List<LaminatedDGUTable> lDGUData;
    private DBDao dao;
    Map<String, RequestBody> params;
    private String accessToken;
    private InquiryViewModel viewModel;
    File fileSgu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityAddressBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViewModel();

        dao = DBRoom.getInstance(this).dao();
//        inquirySaveTable = new InquirySaveTable();

        binding.cardSave.setOnClickListener(view -> {
//
            sguData = dao.getSGUData();
            dguData = dao.getDGUData();
            laminationData = dao.getLaminationData();
            annealedData = dao.getAnnealedData();
            lDGUData = dao.getLDGUData();

            params = new HashMap<>();

            params.put("address[street_no]", createPartFromString(binding.etStreetNo.getText().toString()));
            params.put("address[address]", createPartFromString(binding.etAddress.getText().toString()));
            params.put("address[state]", createPartFromString(binding.etState.getText().toString()));
            params.put("address[city]", createPartFromString(binding.etCity.getText().toString()));
            params.put("address[pincode]", createPartFromString(binding.etPincode.getText().toString()));

            if (!sguData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 0 + "][glass_type]");
                params.put("order[0][glass_type]", createPartFromString("SGU"));
                for (int i = 0; i < sguData.size(); i++) {
                    params.put("order[0][standard]", createPartFromString(sguData.get(i).getSgu_standard()));
                    params.put("order[0][thickness]", createPartFromString(sguData.get(i).getSgu_thickness()));
                    params.put("order[0][material_details]", createPartFromString(sguData.get(i).getSgu_materialDetails()));
                    params.put("order[" + 0 + "][order_items][" + i + "]" + "[width]", createPartFromString(sguData.get(i).getSgu_glassWidth()));
                    params.put("order[" + 0 + "][order_items][" + i + "]" + "[height]", createPartFromString(sguData.get(i).getSgu_glassHeight()));
                    params.put("order[" + 0 + "][order_items][" + i + "]" + "[quantity]", createPartFromString(sguData.get(i).getSgu_quantity()));
                    params.put("order[" + 0 + "][order_items][" + i + "]" + "[notes]", createPartFromString(sguData.get(i).getSgu_note()));
                    if (sguData.get(i).getSgu_image() != null) {
                        Bitmap bmpImage = DataConverter.convertByteArray2Image(sguData.get(i).getSgu_image());
//                        params.put("order[" + 0 + "][order_items][" + i + "]" + "[image]", bmpImage);
                        fileSgu = new File(sguData.get(i).getSgu_path());
//                        RequestBody reqFile =  0 + "][order_items][" + i + "]" + "[image]", body);
                    } else {
                    }
                }
            }
            if (!dguData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 1 + "][glass_type]");
                params.put("order[1][glass_type]", createPartFromString("DGU"));
                for (int i = 0; i < dguData.size(); i++) {
                    params.put("order[1][standard]", createPartFromString(dguData.get(i).getDgu_standard()));
                    params.put("order[1][glass1]", createPartFromString(dguData.get(i).getDgu_glass_1()));
                    params.put("order[1][glass2]", createPartFromString(dguData.get(i).getDgu_glass_2()));

                    if (dguData.get(i).getDgu_gap().equals("0")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("06"));
                    } else if (dguData.get(i).getDgu_gap().equals("1")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("08"));
                    } else if (dguData.get(i).getDgu_gap().equals("2")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("10"));
                    } else if (dguData.get(i).getDgu_gap().equals("3")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("12"));
                    } else if (dguData.get(i).getDgu_gap().equals("4")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("14"));
                    } else if (dguData.get(i).getDgu_gap().equals("5")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("15"));
                    } else if (dguData.get(i).getDgu_gap().equals("6")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("16"));
                    } else if (dguData.get(i).getDgu_gap().equals("7")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("18"));
                    } else if (dguData.get(i).getDgu_gap().equals("8")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("20"));
                    } else if (dguData.get(i).getDgu_gap().equals("9")) {
                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[Gap]", createPartFromString("22"));
                    }

                    params.put("order[" + 1 + "][order_items][" + i + "]" + "[width]", createPartFromString(dguData.get(i).getDgu_glassWidth()));
                    params.put("order[" + 1 + "][order_items][" + i + "]" + "[height]", createPartFromString(dguData.get(i).getDgu_glassHeight()));
                    params.put("order[" + 1 + "][order_items][" + i + "]" + "[quantity]", createPartFromString(dguData.get(i).getDgu_quantity()));
                    params.put("order[" + 1 + "][order_items][" + i + "]" + "[notes]", createPartFromString(dguData.get(i).getDgu_note()));

                    if (dguData.get(i).getDgu_image() != null) {
                        Bitmap bmpImage = DataConverter.convertByteArray2Image(dguData.get(i).getDgu_image());
//                        params.put("order[" + 1 + "][order_items][" + i + "]" + "[image]", bmpImage);
                        fileSgu = new File(dguData.get(i).getDgu_path());
                    } else {
                    }
                }
            }
            if (!laminationData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 2 + "][glass_type]");
                params.put("order[2][glass_type]", createPartFromString("Lamination Glass"));
                for (int i = 0; i < laminationData.size(); i++) {
                    params.put("order[2][standard]", createPartFromString(laminationData.get(i).getLamination_standard()));
                    params.put("order[2][thickness]", createPartFromString(laminationData.get(i).getLamination_thickness()));
                    params.put("order[2][material_details]", createPartFromString(laminationData.get(i).getLamination_materialDetails()));
                    params.put("order[" + 2 + "][order_items][" + i + "]" + "[pvb]", createPartFromString(laminationData.get(i).getLamination_pvb()));
                    params.put("order[" + 2 + "][order_items][" + i + "]" + "[width]", createPartFromString(laminationData.get(i).getLamination_glassWidth()));
                    params.put("order[" + 2 + "][order_items][" + i + "]" + "[height]", createPartFromString(laminationData.get(i).getLamination_glassHeight()));
                    params.put("order[" + 2 + "][order_items][" + i + "]" + "[quantity]", createPartFromString(laminationData.get(i).getLamination_quantity()));
                    params.put("order[" + 2 + "][order_items][" + i + "]" + "[notes]", createPartFromString(laminationData.get(i).getLamination_note()));
                    if (laminationData.get(i).getLamination_image() != null) {
                        Bitmap bmpImage = DataConverter.convertByteArray2Image(laminationData.get(i).getLamination_image());
                        File file = new File(laminationData.get(i).getLamination_path());
                    } else {
                    }

                }
            }
            if (!annealedData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 3 + "][glass_type]");
                params.put("order[3][glass_type]", createPartFromString("Annealed Glass"));
//                inquirySaveTable.setData("SGU");
                for (int i = 0; i < annealedData.size(); i++) {
                    params.put("order[3][standard]", createPartFromString(annealedData.get(i).getAnnealed_standard()));
                    params.put("order[3][thickness]", createPartFromString(annealedData.get(i).getAnnealed_thickness()));
                    params.put("order[3][material_details]", createPartFromString(annealedData.get(i).getAnnealed_materialDetails()));
                    params.put("order[" + 3 + "][order_items][" + i + "]" + "[width]", createPartFromString(annealedData.get(i).getAnnealed_glassWidth()));
                    params.put("order[" + 3 + "][order_items][" + i + "]" + "[height]", createPartFromString(annealedData.get(i).getAnnealed_glassHeight()));
                    params.put("order[" + 3 + "][order_items][" + i + "]" + "[quantity]", createPartFromString(annealedData.get(i).getAnnealed_quantity()));
                    params.put("order[" + 3 + "][order_items][" + i + "]" + "[notes]", createPartFromString(annealedData.get(i).getAnnealed_note()));
                    if (annealedData.get(i).getAnnealed_image() != null) {
                        Bitmap bmpImage = DataConverter.convertByteArray2Image(annealedData.get(i).getAnnealed_image());
                        File file = new File(annealedData.get(i).getAnnealed_path());
                    } else {
                    }
                }
            }
            if (!lDGUData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 4 + "][glass_type]");
                params.put("order[4][glass_type]", createPartFromString("Laminated DGU Glass"));
                for (int i = 0; i < lDGUData.size(); i++) {
                    params.put("order[4][standard]", createPartFromString(lDGUData.get(i).getLdgu_standard()));
                    params.put("order[4][glass1]", createPartFromString(lDGUData.get(i).getLdgu_glass_1()));
                    params.put("order[4][glass2]", createPartFromString(lDGUData.get(i).getLdgu_glass_2()));
                    params.put("order[4][glass3]", createPartFromString(lDGUData.get(i).getLdgu_glass_3()));
                    params.put("order[" + 4 + "][order_items][" + i + "]" + "[Gap]", createPartFromString(lDGUData.get(i).getLdgu_gap()));
                    params.put("order[" + 4 + "][order_items][" + i + "]" + "[width]", createPartFromString(lDGUData.get(i).getLdgu_glassWidth()));
                    params.put("order[" + 4 + "][order_items][" + i + "]" + "[height]", createPartFromString(lDGUData.get(i).getLdgu_glassHeight()));
                    params.put("order[" + 4 + "][order_items][" + i + "]" + "[quantity]", createPartFromString(lDGUData.get(i).getLdgu_quantity()));
                    params.put("order[" + 4 + "][order_items][" + i + "]" + "[notes]", createPartFromString(lDGUData.get(i).getLdgu_note()));
                    if (lDGUData.get(i).getLdgu_image() != null) {
                        Bitmap bmpImage = DataConverter.convertByteArray2Image(lDGUData.get(i).getLdgu_image());
                        File file = new File(lDGUData.get(i).getLdgu_path());
                    } else {
                    }
                }
            }
            Log.i("TAG", "onCreate: " + params.size());
            if (TextUtils.isEmpty(binding.etStreetNo.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Enter Street No", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etAddress.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Enter Address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etState.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Enter State", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etCity.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Enter City", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etPincode.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Enter Pincode", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etPincode.getText().toString().length() < 6) {
                Toast.makeText(AddressActivity.this, "Please Enter valid Pincode", Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
                SharedPreferences sh = getSharedPreferences("TokenPref", MODE_APPEND);
                if (sh != null)
                    accessToken = sh.getString("token", "");

                MultipartBody.Part fphoto = null;
                if (fileSgu != null) {
                    RequestBody requestFile =
                            RequestBody.create(MediaType.parse("multipart/form-data"), fileSgu);
                    fphoto =
                            MultipartBody.Part.createFormData("order[0][order_items][0][image]",
                                    fileSgu.getName(), requestFile);
                }
                Log.i("TAG", "onCreate1: " + fileSgu.toString());
                viewModel.apiCall(params, "Bearer " + accessToken, fphoto);
            }
        });

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.ivLogout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("login", false);
            myEdit.commit();
            startActivity(new Intent(AddressActivity.this, MainActivity.class));
            finish();
        });
    }

    public RequestBody createPartFromString(String desString) {
        return RequestBody.create(MediaType.parse("text/plain"), desString);
    }


    private void showLoading() {
        binding.formProgress.setVisibility(View.VISIBLE);
        binding.formScroll.setVisibility(View.GONE);
    }

    private void hideLoading() {
        binding.formProgress.setVisibility(View.GONE);
        binding.formScroll.setVisibility(View.VISIBLE);
    }

    private void initViewModel() {
        viewModel = new ViewModelProvider(this).get(InquiryViewModel.class);
        viewModel.getCreatedUserObserver().observe(this, new Observer<InquiryData>() {
            @Override
            public void onChanged(InquiryData loginUser) {
                hideLoading();
                if (loginUser == null) {
                    Toast.makeText(AddressActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AddressActivity.this, loginUser.getMsg(), Toast.LENGTH_SHORT).show();
                    dao.deleteSguData();
                    dao.deleteDguData();
                    dao.deleteLaminationData();
                    dao.deleteAnnealedData();
                    dao.deleteLDGUData();
                    startActivity(new Intent(AddressActivity.this, OrderListActivity.class));
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, OrderListActivity.class));
        finish();
    }
}