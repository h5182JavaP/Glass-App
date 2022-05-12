package com.my.app.glassapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import com.my.app.glassapp.PathUtil;
import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.R;
import com.my.app.glassapp.database.DBDao;
import com.my.app.glassapp.database.DBRoom;
import com.my.app.glassapp.databinding.ActivityInquiryFormBinding;
import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URISyntaxException;

public class InquiryFormActivity extends AppCompatActivity {

    public static final int SELECT_PICTURE = 101;

    ActivityInquiryFormBinding binding;
    String[] glassTypeCategories = {"SGU", "DGU", "Lamination", "Annealed", "Laminated DGU glass"};
    String[] gapCategories = {"06", "08", "10", "12", "14", "15", "16", "18", "20", "22"};
    String[] pvbCategories = {"0.38", "0.16", "1.14", "1.52"};

    int glassTypePosition = 0;
    int gapPosition = 0;
    int pvbPosition = 0;
    int standardId = 0;
    String thicknessText;
    String materialDetailsText;
    String glassWidthText;
    String glassHeightText;
    String quantityText;
    String noteText;
    String glass1;
    String glass2;
    String glass3;
    //    String gap;
    private DBDao dao;
    String selectedStandard = "inch";
    Bitmap bmpImage;
    String imgepath;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityInquiryFormBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setGlassTypeSpinnerAdapter();
        setGapSpinnerAdapter();
        setPVBSpinnerAdapter();

        standardId = binding.radioGroup.getCheckedRadioButtonId();
        binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                standardId = i;
                if (standardId == R.id.radioInch) {
                    selectedStandard = "inch";
                } else if (standardId == R.id.radioMm) {
                    selectedStandard = "mm";
                }
            }
        });

        dao = DBRoom.getInstance(this).dao();

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.cvCancel.setOnClickListener(view -> {
            finish();
        });

        binding.ivUploadPhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);
        });

        binding.ivUploadPhoto1.setOnClickListener(view -> {
        /*    Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,
                    "Select Picture"), SELECT_PICTURE);*/
            if (bmpImage != null) {
//                Intent intent = new Intent(this, ViewImageActivity.class);
//                intent.putExtra("img", DataConverter.convertImage2ByteArray(bmpImage));
//                startActivity(intent);
            }
        });

        binding.imgLogout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("login", false);
            myEdit.commit();
            startActivity(new Intent(InquiryFormActivity.this, MainActivity.class));
            finish();
        });


        binding.cvSubmit.setOnClickListener(view -> {
//            if (bmpImage != null) {
            startActivity(new Intent(this, OrderListActivity.class));
            finish();
            thicknessText = binding.etThickness.getText().toString();
            materialDetailsText = binding.etMaterialDetail.getText().toString();
            glassWidthText = binding.etWidth.getText().toString();
            glassHeightText = binding.etHeight.getText().toString();
            quantityText = binding.etQuantity.getText().toString();
            noteText = binding.etNote.getText().toString();
            glass1 = binding.etGlass1.getText().toString();
            glass2 = binding.etGlass2.getText().toString();
            glass3 = binding.etGlass3.getText().toString();
//            gap = binding.etGlass2.getText().toString();
            Log.i("TAG", "onCreate: " + "Glass Type Position=> " + glassTypePosition +
                    "  Standard=> " + standardId + "  Thickness=> " + thicknessText + "  Material=> " + materialDetailsText +
                    "  width=> " + glassWidthText + "  Height=> " + glassWidthText +
                    "  Quantity=> " + quantityText + "  Note=> " + noteText);

            if (glassTypePosition == 0) {
                SGUTable sguData = new SGUTable();
                sguData.setSgu_standard(selectedStandard);
                sguData.setSgu_thickness(thicknessText);
                sguData.setSgu_materialDetails(materialDetailsText);
                sguData.setSgu_glassWidth(glassWidthText);
                sguData.setSgu_glassHeight(glassHeightText);
                sguData.setSgu_quantity(quantityText);
                sguData.setSgu_note(noteText);
                if (bmpImage != null) {
                    sguData.setSgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                    sguData.setSgu_path(imgepath);
                }

                dao.insertSGUData(sguData);
            }
            else if (glassTypePosition == 1) {
                DGUTable dguData = new DGUTable();
                dguData.setDgu_standard(selectedStandard);
                dguData.setDgu_glass_1(glass1);
                dguData.setDgu_glass_2(glass2);
                dguData.setDgu_gap(String.valueOf(gapPosition));
                dguData.setDgu_glassWidth(glassWidthText);
                dguData.setDgu_glassHeight(glassHeightText);
                dguData.setDgu_quantity(quantityText);
                dguData.setDgu_note(noteText);
                if (bmpImage != null) {
                    dguData.setDgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                    dguData.setDgu_path(imgepath);
                }

                dao.insertDGUData(dguData);
            }
            else if (glassTypePosition == 2) {
                LaminationTable laminationData = new LaminationTable();
                laminationData.setLamination_standard(selectedStandard);
                laminationData.setLamination_thickness(thicknessText);
                laminationData.setLamination_materialDetails(materialDetailsText);
                laminationData.setLamination_pvb(String.valueOf(pvbPosition));
                laminationData.setLamination_glassWidth(glassWidthText);
                laminationData.setLamination_glassHeight(glassHeightText);
                laminationData.setLamination_quantity(quantityText);
                laminationData.setLamination_note(noteText);
                if (bmpImage != null) {
                    laminationData.setLamination_image(DataConverter.convertImage2ByteArray(bmpImage));
                    laminationData.setLamination_path(imgepath);
                }

                dao.insertLaminationData(laminationData);
            }
            else if (glassTypePosition == 3) {
                AnnealedTable annealedData = new AnnealedTable();
                annealedData.setAnnealed_standard(selectedStandard);
                annealedData.setAnnealed_thickness(thicknessText);
                annealedData.setAnnealed_materialDetails(materialDetailsText);
                annealedData.setAnnealed_glassWidth(glassWidthText);
                annealedData.setAnnealed_glassHeight(glassHeightText);
                annealedData.setAnnealed_quantity(quantityText);
                annealedData.setAnnealed_note(noteText);
                if (bmpImage != null) {
                    annealedData.setAnnealed_image(DataConverter.convertImage2ByteArray(bmpImage));
                    annealedData.setAnnealed_path(imgepath);
                }

                dao.insertAnnealedData(annealedData);
            }
            else if (glassTypePosition == 4) {
                LaminatedDGUTable lDguData = new LaminatedDGUTable();
                lDguData.setLdgu_standard(selectedStandard);
                lDguData.setLdgu_glass_1(glass1);
                lDguData.setLdgu_glass_2(glass2);
                lDguData.setLdgu_glass_3(glass3);
                lDguData.setLdgu_gap(String.valueOf(gapPosition));
                lDguData.setLdgu_glassWidth(glassWidthText);
                lDguData.setLdgu_glassHeight(glassHeightText);
                lDguData.setLdgu_quantity(quantityText);
                lDguData.setLdgu_note(noteText);
                if (bmpImage != null) {
                    lDguData.setLdgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                    lDguData.setLdgu_path(imgepath);
                }
                dao.insertLaminatedDGUData(lDguData);
            }
//            }
//            else {
//                Toast.makeText(InquiryFormActivity.this, "Select Photo", Toast.LENGTH_SHORT).show();
//            }
            sharedPreferences = getSharedPreferences("FormDataPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();

            if (glassTypePosition == 0) {
                myEdit.putString("glassType", "0");
            } else if (glassTypePosition == 1) {
                myEdit.putString("glassType", "1");
            } else if (glassTypePosition == 2) {
                myEdit.putString("glassType", "2");
            } else if (glassTypePosition == 3) {
                myEdit.putString("glassType", "3");
            } else if (glassTypePosition == 4) {
                myEdit.putString("glassType", "4");
            }
            myEdit.putString("standard", selectedStandard);
            myEdit.putString("thikness", thicknessText);
            myEdit.putString("material", materialDetailsText);
            myEdit.putString("width", glassWidthText);
            myEdit.putString("height", glassHeightText);
            myEdit.putString("quantity", quantityText);
            myEdit.putString("note", noteText);
            myEdit.putString("glass1", glass1);
            myEdit.putString("glass2", glass2);
            myEdit.putString("glass3", glass3);
            myEdit.putString("gapPosition", String.valueOf(gapPosition));
            myEdit.putString("pvbPosition", String.valueOf(pvbPosition));
            myEdit.commit();
        });

        getAllData();
    }

    public void getAllData() {
        SharedPreferences sh = getSharedPreferences("FormDataPref", MODE_APPEND);
        if (sh != null) {
            String glasstype = sh.getString("glassType", "");
            String standard = sh.getString("standard", "");
            String thikness = sh.getString("thikness", "");
            String material = sh.getString("material", "");
            String width = sh.getString("width", "");
            String height = sh.getString("height", "");
            String quantity = sh.getString("quantity", "");
            String note = sh.getString("note", "");
            String shGlass1 = sh.getString("glass1", "");
            String shGlass2 = sh.getString("glass2", "");
            String shGlass3 = sh.getString("glass3", "");
            String shGapPosition = sh.getString("gapPosition", "");
            String shPvbPosition = sh.getString("pvbPosition", "");

            if (standard.equals("inch")) {
                binding.radioInch.setChecked(true);
            } else {
                binding.radioMm.setChecked(true);
            }

            if (glasstype.equals("0"))
                binding.spGlassType.setSelection(0);
            if (glasstype.equals("1"))
                binding.spGlassType.setSelection(1);
            if (glasstype.equals("2"))
                binding.spGlassType.setSelection(2);
            if (glasstype.equals("3"))
                binding.spGlassType.setSelection(3);
            if (glasstype.equals("4"))
                binding.spGlassType.setSelection(4);

            binding.etThickness.setText(thikness);
            binding.etMaterialDetail.setText(material);
            binding.etWidth.setText(width);
            binding.etHeight.setText(height);
            binding.etQuantity.setText(quantity);
            binding.etNote.setText(note);
            binding.etGlass1.setText(shGlass1);
            binding.etGlass2.setText(shGlass2);
            binding.etGlass3.setText(shGlass3);
            if (shGapPosition.equals("0")) {
                binding.spGap.setSelection(0);
            } else if (shGapPosition.equals("1")) {
                binding.spGap.setSelection(1);
            } else if (shGapPosition.equals("2")) {
                binding.spGap.setSelection(2);
            } else if (shGapPosition.equals("3")) {
                binding.spGap.setSelection(3);
            } else if (shGapPosition.equals("4")) {
                binding.spGap.setSelection(4);
            } else if (shGapPosition.equals("5")) {
                binding.spGap.setSelection(5);
            } else if (shGapPosition.equals("6")) {
                binding.spGap.setSelection(6);
            } else if (shGapPosition.equals("7")) {
                binding.spGap.setSelection(7);
            } else if (shGapPosition.equals("8")) {
                binding.spGap.setSelection(8);
            } else if (shGapPosition.equals("9")) {
                binding.spGap.setSelection(9);
            }

            if (shPvbPosition.equals("0")) {
                binding.spPvb.setSelection(0);
            } else if (shPvbPosition.equals("1")) {
                binding.spPvb.setSelection(1);
            } else if (shPvbPosition.equals("2")) {
                binding.spPvb.setSelection(2);
            } else if (shPvbPosition.equals("3")) {
                binding.spPvb.setSelection(3);
            }
        }
    }

    public void setGlassTypeSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, glassTypeCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spGlassType.setAdapter(dataAdapter);

        binding.spGlassType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                glassTypePosition = position;
                if (position == 0) {
                    binding.tvGap.setVisibility(View.GONE);
                    binding.cvGap.setVisibility(View.GONE);
                    binding.tvGlass1.setVisibility(View.GONE);
                    binding.tvGlass2.setVisibility(View.GONE);
                    binding.tvGlass3.setVisibility(View.GONE);
                    binding.cvGlass1.setVisibility(View.GONE);
                    binding.cvGlass2.setVisibility(View.GONE);
                    binding.cvGlass3.setVisibility(View.GONE);
                    binding.cvThickness.setVisibility(View.VISIBLE);
                    binding.cvMaterialDetails.setVisibility(View.VISIBLE);
                    binding.tvThickness.setVisibility(View.VISIBLE);
                    binding.tvMaterialDetails.setVisibility(View.VISIBLE);
                    binding.cvPvb.setVisibility(View.GONE);
                    binding.spPvb.setVisibility(View.GONE);
                    binding.tvPvb.setVisibility(View.GONE);
                } else if (position == 1) {
                    binding.tvGap.setVisibility(View.VISIBLE);
                    binding.cvGap.setVisibility(View.VISIBLE);
                    binding.tvGlass1.setVisibility(View.VISIBLE);
                    binding.tvGlass2.setVisibility(View.VISIBLE);
                    binding.tvGlass3.setVisibility(View.GONE);
                    binding.cvGlass1.setVisibility(View.VISIBLE);
                    binding.cvGlass2.setVisibility(View.VISIBLE);
                    binding.cvGlass3.setVisibility(View.GONE);
                    binding.cvThickness.setVisibility(View.GONE);
                    binding.cvMaterialDetails.setVisibility(View.GONE);
                    binding.tvThickness.setVisibility(View.GONE);
                    binding.tvMaterialDetails.setVisibility(View.GONE);
                    binding.cvPvb.setVisibility(View.GONE);
                    binding.spPvb.setVisibility(View.GONE);
                    binding.tvPvb.setVisibility(View.GONE);
                    binding.spGap.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    binding.cvPvb.setVisibility(View.VISIBLE);
                    binding.spPvb.setVisibility(View.VISIBLE);
                    binding.tvPvb.setVisibility(View.VISIBLE);
                    binding.cvGap.setVisibility(View.GONE);
//                    binding.spGap.setVisibility(View.GONE);
                    binding.tvGap.setVisibility(View.GONE);
                } else if (position == 3) {
                    binding.tvGap.setVisibility(View.GONE);
                    binding.cvGap.setVisibility(View.GONE);
                    binding.tvGlass1.setVisibility(View.GONE);
                    binding.tvGlass2.setVisibility(View.GONE);
                    binding.tvGlass3.setVisibility(View.GONE);
                    binding.cvGlass1.setVisibility(View.GONE);
                    binding.cvGlass2.setVisibility(View.GONE);
                    binding.cvGlass3.setVisibility(View.GONE);
                    binding.cvThickness.setVisibility(View.VISIBLE);
                    binding.cvMaterialDetails.setVisibility(View.VISIBLE);
                    binding.tvThickness.setVisibility(View.VISIBLE);
                    binding.tvMaterialDetails.setVisibility(View.VISIBLE);
                    binding.cvPvb.setVisibility(View.GONE);
                    binding.spPvb.setVisibility(View.GONE);
                    binding.tvPvb.setVisibility(View.GONE);
                } else if (position == 4) {
                    binding.tvGap.setVisibility(View.VISIBLE);
                    binding.cvGap.setVisibility(View.VISIBLE);
                    binding.tvGlass1.setVisibility(View.VISIBLE);
                    binding.tvGlass2.setVisibility(View.VISIBLE);
                    binding.tvGlass3.setVisibility(View.VISIBLE);
                    binding.cvGlass1.setVisibility(View.VISIBLE);
                    binding.cvGlass2.setVisibility(View.VISIBLE);
                    binding.cvGlass3.setVisibility(View.VISIBLE);
                    binding.cvThickness.setVisibility(View.GONE);
                    binding.cvMaterialDetails.setVisibility(View.GONE);
                    binding.tvThickness.setVisibility(View.GONE);
                    binding.tvMaterialDetails.setVisibility(View.GONE);
                    binding.cvPvb.setVisibility(View.GONE);
                    binding.spPvb.setVisibility(View.GONE);
                    binding.tvPvb.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setGapSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, gapCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spGap.setAdapter(dataAdapter);

        binding.spGap.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gapPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void setPVBSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, pvbCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spPvb.setAdapter(dataAdapter);

        binding.spPvb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                pvbPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                binding.ivUploadPhoto.setVisibility(View.GONE);
                binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
//                imgepath =getImgPath(uri);
                try {
                    imgepath = PathUtil.getPath(this,uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    bmpImage = getBitmapFromUri(uri);
                    binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, OrderListActivity.class));
        finish();
    }
}