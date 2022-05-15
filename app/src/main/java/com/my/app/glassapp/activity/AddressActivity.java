package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.my.app.glassapp.R;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AddressActivity extends AppCompatActivity {

    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    ActivityAddressBinding binding;
    private List<SGUTable> sguData;
    private List<DGUTable> dguData;
    private List<LaminationTable> laminationData;
    private List<AnnealedTable> annealedData;
    private List<LaminatedDGUTable> lDGUData;
    private DBDao dao;
    HashMap<String, RequestBody> params;
    private String accessToken;
    private InquiryViewModel viewModel;
    //    File fileSgu;
    List<String> imageKeyList = new ArrayList<>();
    List<File> imageFileList = new ArrayList<>();
    int j = 0;

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
//            for (int j = 0; j < 1; j++) {
            if (!sguData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + j + "][glass_type]");
                params.put("order[" + j + "][glass_type]", createPartFromString("SGU"));
                for (int i = 0; i < sguData.size(); i++) {
                    params.put("order[" + j + "][standard]", createPartFromString(sguData.get(i).getSgu_standard()));
                    params.put("order[" + j + "][thickness]", createPartFromString(sguData.get(i).getSgu_thickness()));
                    params.put("order[" + j + "][material_details]", createPartFromString(sguData.get(i).getSgu_materialDetails()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[width]", createPartFromString(sguData.get(i).getSgu_glassWidth()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[height]", createPartFromString(sguData.get(i).getSgu_glassHeight()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[quantity]", createPartFromString(sguData.get(i).getSgu_quantity()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[notes]", createPartFromString(sguData.get(i).getSgu_note()));
                    if (sguData.get(i).getSgu_path() != null) {
//                            if (fileSgu != null) {
                        imageFileList.add(new File(sguData.get(i).getSgu_path()));
                        imageKeyList.add("order[" + j + "][order_items][" + i + "][image]");
//                            }
                    } else {
                    }
                }
                j++;
            }
            if (!dguData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + 1 + "][glass_type]");
                params.put("order[" + j + "][glass_type]", createPartFromString("DGU"));
                for (int i = 0; i < dguData.size(); i++) {
                    params.put("order[" + j + "][standard]", createPartFromString(dguData.get(i).getDgu_standard()));
                    params.put("order[" + j + "][glass1]", createPartFromString(dguData.get(i).getDgu_glass_1()));
                    params.put("order[" + j + "][glass2]", createPartFromString(dguData.get(i).getDgu_glass_2()));

                    if (dguData.get(i).getDgu_gap().equals("0")) {
                        params.put("order[" + j + "][gap]", createPartFromString("06"));
                    } else if (dguData.get(i).getDgu_gap().equals("1")) {
                        params.put("order[" + j + "][gap]", createPartFromString("08"));
                    } else if (dguData.get(i).getDgu_gap().equals("2")) {
                        params.put("order[" + j + "][gap]", createPartFromString("10"));
                    } else if (dguData.get(i).getDgu_gap().equals("3")) {
                        params.put("order[" + j + "][gap]", createPartFromString("12"));
                    } else if (dguData.get(i).getDgu_gap().equals("4")) {
                        params.put("order[" + j + "][gap]", createPartFromString("14"));
                    } else if (dguData.get(i).getDgu_gap().equals("5")) {
                        params.put("order[" + j + "][gap]", createPartFromString("15"));
                    } else if (dguData.get(i).getDgu_gap().equals("6")) {
                        params.put("order[" + j + "][gap]", createPartFromString("16"));
                    } else if (dguData.get(i).getDgu_gap().equals("7")) {
                        params.put("order[" + j + "][gap]", createPartFromString("18"));
                    } else if (dguData.get(i).getDgu_gap().equals("8")) {
                        params.put("order[" + j + "][gap]", createPartFromString("20"));
                    } else if (dguData.get(i).getDgu_gap().equals("9")) {
                        params.put("order[" + j + "][gap]", createPartFromString("22"));
                    }

                    params.put("order[" + j + "][order_items][" + i + "]" + "[width]", createPartFromString(dguData.get(i).getDgu_glassWidth()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[height]", createPartFromString(dguData.get(i).getDgu_glassHeight()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[quantity]", createPartFromString(dguData.get(i).getDgu_quantity()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[notes]", createPartFromString(dguData.get(i).getDgu_note()));

                    if (dguData.get(i).getDgu_path() != null) {
//                            if (fileSgu != null) {
                        imageFileList.add(new File(dguData.get(i).getDgu_path()));
                        imageKeyList.add("order[" + j + "][order_items][" + i + "][image]");
//                            }
                    } else {
                    }
                }
                j++;
            }
            if (!laminationData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + j + "][glass_type]");
                params.put("order[" + j + "][glass_type]", createPartFromString("Lamination Glass"));
                for (int i = 0; i < laminationData.size(); i++) {
                    params.put("order[" + j + "][standard]", createPartFromString(laminationData.get(i).getLamination_standard()));
                    params.put("order[" + j + "][thickness]", createPartFromString(laminationData.get(i).getLamination_glass_1()));
                    params.put("order[" + j + "][material_details]", createPartFromString(laminationData.get(i).getLamination_glass_2()));
                    if (laminationData.get(i).getLamination_pvb().equals("0"))
                        params.put("order[" + j + "][PVB]", createPartFromString("0.38"));
                    else if (laminationData.get(i).getLamination_pvb().equals("1"))
                        params.put("order[" + j + "][PVB]", createPartFromString("0.16"));
                    else if (laminationData.get(i).getLamination_pvb().equals("2"))
                        params.put("order[" + j + "][PVB]", createPartFromString("1.14"));
                    else if (laminationData.get(i).getLamination_pvb().equals("3"))
                        params.put("order[" + j + "][PVB]", createPartFromString("1.52"));

                    params.put("order[" + j + "][order_items][" + i + "]" + "[width]", createPartFromString(laminationData.get(i).getLamination_glassWidth()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[height]", createPartFromString(laminationData.get(i).getLamination_glassHeight()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[quantity]", createPartFromString(laminationData.get(i).getLamination_quantity()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[notes]", createPartFromString(laminationData.get(i).getLamination_note()));
                    if (laminationData.get(i).getLamination_path() != null) {
//                            if (fileSgu != null){
                        imageFileList.add(new File(laminationData.get(i).getLamination_path()));
                        imageKeyList.add("order[" + j + "][order_items][" + i + "][image]");
//                        }
                    } else {
                    }

                }
                j++;
            }
            if (!annealedData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + j + "][glass_type]");
                params.put("order[" + j + "][glass_type]", createPartFromString("Annealed Glass"));
//                inquirySaveTable.setData("SGU");
                for (int i = 0; i < annealedData.size(); i++) {
                    params.put("order[" + j + "][standard]", createPartFromString(annealedData.get(i).getAnnealed_standard()));
                    params.put("order[" + j + "][thickness]", createPartFromString(annealedData.get(i).getAnnealed_thickness()));
                    params.put("order[" + j + "][material_details]", createPartFromString(annealedData.get(i).getAnnealed_materialDetails()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[width]", createPartFromString(annealedData.get(i).getAnnealed_glassWidth()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[height]", createPartFromString(annealedData.get(i).getAnnealed_glassHeight()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[quantity]", createPartFromString(annealedData.get(i).getAnnealed_quantity()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[notes]", createPartFromString(annealedData.get(i).getAnnealed_note()));
                    if (annealedData.get(i).getAnnealed_path() != null) {
//                            if (fileSgu != null) {
                        imageFileList.add(new File(annealedData.get(i).getAnnealed_path()));
                        imageKeyList.add("order[" + j + "][order_items][" + i + "][image]");
//                            }
                    } else {
                    }
                }
                j++;
            }
            if (!lDGUData.isEmpty()) {
                Log.i("TAG", "saveOrder: " + "order[" + j + "][glass_type]");
                params.put("order[" + j + "][glass_type]", createPartFromString("Laminated DGU Glass"));
                for (int i = 0; i < lDGUData.size(); i++) {
                    params.put("order[" + j + "][standard]", createPartFromString(lDGUData.get(i).getLdgu_standard()));
                    params.put("order[" + j + "][glass1]", createPartFromString(lDGUData.get(i).getLdgu_glass_1()));
                    params.put("order[" + j + "][glass2]", createPartFromString(lDGUData.get(i).getLdgu_glass_2()));
                    params.put("order[" + j + "][glass3]", createPartFromString(lDGUData.get(i).getLdgu_glass_3()));

                    if (lDGUData.get(i).getLdgu_gap().equals("0")) {
                        params.put("order[" + j + "][gap]", createPartFromString("06"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("1")) {
                        params.put("order[" + j + "][gap]", createPartFromString("08"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("2")) {
                        params.put("order[" + j + "][gap]", createPartFromString("10"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("3")) {
                        params.put("order[" + j + "][gap]", createPartFromString("12"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("4")) {
                        params.put("order[" + j + "][gap]", createPartFromString("14"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("5")) {
                        params.put("order[" + j + "][gap]", createPartFromString("15"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("6")) {
                        params.put("order[" + j + "][gap]", createPartFromString("16"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("7")) {
                        params.put("order[" + j + "][gap]", createPartFromString("18"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("8")) {
                        params.put("order[" + j + "][gap]", createPartFromString("20"));
                    } else if (lDGUData.get(i).getLdgu_gap().equals("9")) {
                        params.put("order[" + j + "][gap]", createPartFromString("22"));
                    }

                    if (lDGUData.get(i).getLdgu_pvb().equals("0"))
                        params.put("order[" + j + "][PVB]", createPartFromString("0.38"));
                    else if (lDGUData.get(i).getLdgu_pvb().equals("1"))
                        params.put("order[" + j + "][PVB]", createPartFromString("0.16"));
                    else if (lDGUData.get(i).getLdgu_pvb().equals("2"))
                        params.put("order[" + j + "][PVB]", createPartFromString("1.14"));
                    else if (lDGUData.get(i).getLdgu_pvb().equals("3"))
                        params.put("order[" + j + "][PVB]", createPartFromString("1.52"));
//                    params.put("order[" + j + "][gap]", createPartFromString(lDGUData.get(i).getLdgu_gap()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[width]", createPartFromString(lDGUData.get(i).getLdgu_glassWidth()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[height]", createPartFromString(lDGUData.get(i).getLdgu_glassHeight()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[quantity]", createPartFromString(lDGUData.get(i).getLdgu_quantity()));
                    params.put("order[" + j + "][order_items][" + i + "]" + "[notes]", createPartFromString(lDGUData.get(i).getLdgu_note()));
                    if (lDGUData.get(i).getLdgu_path() != null) {
//                            if (fileSgu != null) {
                        imageFileList.add(new File(lDGUData.get(i).getLdgu_path()));
                        imageKeyList.add("order[" + j + "][order_items][" + i + "][image]");
//                            }
                    } else {
                    }
                }
                j++;
            }
//            }
            Log.i("TAG", "onCreate: " + params.size());
            if (TextUtils.isEmpty(binding.etStreetNo.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Please enter Street No", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etAddress.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Please enter Address", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etState.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Please enter state", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etCity.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Please enter city", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(binding.etPincode.getText().toString())) {
                Toast.makeText(AddressActivity.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.etPincode.getText().toString().length() < 6) {
                Toast.makeText(AddressActivity.this, "Please enter valid pincode", Toast.LENGTH_SHORT).show();
            } else {
                showLoading();
                SharedPreferences sh = getSharedPreferences("TokenPref", MODE_APPEND);
                if (sh != null)
                    accessToken = sh.getString("token", "");


                MultipartBody.Part[] surveyImagesParts = new MultipartBody.Part[imageKeyList.size()];

                RequestBody requestFile;
                for (int i = 0; i < imageKeyList.size(); i++) {
                    requestFile = RequestBody.create(MediaType.parse("*/*"), imageFileList.get(i));
                    surveyImagesParts[i] = MultipartBody.Part.createFormData(imageKeyList.get(i),
                            imageFileList.get(i).getName(),
                            requestFile);

                }
//                if (fileSgu != null) {

//                    MultipartBody.Part fphoto = MultipartBody.Part.createFormData("order[0][order_items][0][image]", fileSgu.getName(), requestFile);
//                }
                viewModel.apiCall(params, "Bearer " + accessToken, surveyImagesParts);
            }
        });

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.ivLogout.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(AddressActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.dialog_logout);

            TextView tvYes = (TextView) dialog.findViewById(R.id.tv_yes);
            TextView tvNo = (TextView) dialog.findViewById(R.id.tv_no);

            tvYes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putBoolean("login", false);
                    myEdit.commit();
                    startActivity(new Intent(AddressActivity.this, MainActivity.class));
                    finish();
                }
            });
            tvNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();

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
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            startActivity(new Intent(this, OrderListActivity.class));
            finish();
        }

    }
}