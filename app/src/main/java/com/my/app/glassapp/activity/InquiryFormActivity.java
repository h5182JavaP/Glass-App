package com.my.app.glassapp.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.my.app.glassapp.PathUtil;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InquiryFormActivity extends AppCompatActivity {

    public static final int SELECT_PICTURE = 101;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

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
    public static Bitmap bmpImage;
    String imgepath;
    boolean cameraFlag = false;
//    private SharedPreferences sharedPreferencesDgu;
//    private SharedPreferences sharedPreferencesLamination;

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
            onBackPressed();
        });

        binding.ivUploadPhoto.setOnClickListener(view -> {
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.btm_sheet_dialog);
            dialog.setCanceledOnTouchOutside(true);

            CardView cvViewImage = (CardView) dialog.findViewById(R.id.cv_view_imag);
            CardView cvCamera = (CardView) dialog.findViewById(R.id.cv_camera);
            CardView cvGallery = (CardView) dialog.findViewById(R.id.cv_gallery);
            cvViewImage.setVisibility(View.GONE);

            cvCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                    dialog.dismiss();
                }
            });
            cvGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);

                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        binding.ivUploadPhoto1.setOnClickListener(view -> {
            final BottomSheetDialog dialog = new BottomSheetDialog(this);
            dialog.setContentView(R.layout.btm_sheet_dialog);
            dialog.setCanceledOnTouchOutside(true);

            CardView cvViewImage = (CardView) dialog.findViewById(R.id.cv_view_imag);
            CardView cvCamera = (CardView) dialog.findViewById(R.id.cv_camera);
            CardView cvGallery = (CardView) dialog.findViewById(R.id.cv_gallery);

            cvViewImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bmpImage != null) {
                        Intent intent = new Intent(InquiryFormActivity.this, ViewImageActivity.class);
//                        intent.putExtra("img", bmpImage);
//                            intent.putExtra("img", DataConverter.convertImage2ByteArray(bmpImage));
                        startActivity(intent);
                    }
                    dialog.dismiss();
                }
            });
            cvCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    } else {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                    dialog.dismiss();
                }
            });
            cvGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), SELECT_PICTURE);

                    dialog.dismiss();
                }
            });
            dialog.show();
        });

        binding.imgLogout.setOnClickListener(view -> {

            final Dialog dialog = new Dialog(InquiryFormActivity.this);
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
                    startActivity(new Intent(InquiryFormActivity.this, MainActivity.class));
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


        binding.cvSubmit.setOnClickListener(view -> {
//            if (bmpImage != null) {
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
                if (TextUtils.isEmpty(binding.etThickness.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter thickness", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etMaterialDetail.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etWidth.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass width", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etHeight.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass height", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etQuantity.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
                    SGUTable sguData = new SGUTable();
                    sguData.setSgu_standard(selectedStandard);
                    sguData.setSgu_thickness(thicknessText);
                    sguData.setSgu_materialDetails(materialDetailsText);
                    sguData.setSgu_glassWidth(glassWidthText);
                    sguData.setSgu_glassHeight(glassHeightText);
                    sguData.setSgu_quantity(quantityText);
                    sguData.setSgu_note(noteText);
                    if (bmpImage != null) {
//                        sguData.setSgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                        if (cameraFlag) {
                            File sd = getCacheDir();
                            File folder = new File(sd, "/myfolder/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdirs();
                                }
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                            String timeStamp = dateFormat.format(new Date());
                            String imageFileName = "picture_" + timeStamp + ".jpg";

                            File fileName = new File(folder, imageFileName);
                            imgepath = fileName.getAbsolutePath();

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        sguData.setSgu_path(imgepath);
                    }

                    dao.insertSGUData(sguData);
                    startActivity(new Intent(this, OrderListActivity.class));
                    finish();
                }
            } else if (glassTypePosition == 1) {
                if (TextUtils.isEmpty(binding.etGlass1.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 1 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGlass2.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 2 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etWidth.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass width", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etHeight.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass height", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etQuantity.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
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
//                        dguData.setDgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                        if (cameraFlag) {
                            File sd = getCacheDir();
                            File folder = new File(sd, "/myfolder/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdirs();
                                }
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                            String timeStamp = dateFormat.format(new Date());
                            String imageFileName = "picture_" + timeStamp + ".jpg";

                            File fileName = new File(folder, imageFileName);
                            imgepath = fileName.getAbsolutePath();

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        dguData.setDgu_path(imgepath);
                    }

                    dao.insertDGUData(dguData);
                    startActivity(new Intent(this, OrderListActivity.class));
                    finish();
                }
            } else if (glassTypePosition == 2) {
                if (TextUtils.isEmpty(binding.etGlass1.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 1 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGlass2.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 2 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etWidth.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass width", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etHeight.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass height", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etQuantity.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
                    LaminationTable laminationData = new LaminationTable();
                    laminationData.setLamination_standard(selectedStandard);
                    laminationData.setLamination_glass_1(glass1);
                    laminationData.setLamination_glass_2(glass2);
                    laminationData.setLamination_pvb(String.valueOf(pvbPosition));
                    laminationData.setLamination_glassWidth(glassWidthText);
                    laminationData.setLamination_glassHeight(glassHeightText);
                    laminationData.setLamination_quantity(quantityText);
                    laminationData.setLamination_note(noteText);
                    if (bmpImage != null) {
//                        laminationData.setLamination_image(DataConverter.convertImage2ByteArray(bmpImage));
                        if (cameraFlag) {
                            File sd = getCacheDir();
                            File folder = new File(sd, "/myfolder/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdirs();
                                }
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                            String timeStamp = dateFormat.format(new Date());
                            String imageFileName = "picture_" + timeStamp + ".jpg";

                            File fileName = new File(folder, imageFileName);
                            imgepath = fileName.getAbsolutePath();

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        laminationData.setLamination_path(imgepath);
                    }

                    dao.insertLaminationData(laminationData);
                    startActivity(new Intent(this, OrderListActivity.class));
                    finish();
                }
            } else if (glassTypePosition == 3) {
                if (TextUtils.isEmpty(binding.etThickness.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter thickness", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etMaterialDetail.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etWidth.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass width", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etHeight.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass height", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etQuantity.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
                    AnnealedTable annealedData = new AnnealedTable();
                    annealedData.setAnnealed_standard(selectedStandard);
                    annealedData.setAnnealed_thickness(thicknessText);
                    annealedData.setAnnealed_materialDetails(materialDetailsText);
                    annealedData.setAnnealed_glassWidth(glassWidthText);
                    annealedData.setAnnealed_glassHeight(glassHeightText);
                    annealedData.setAnnealed_quantity(quantityText);
                    annealedData.setAnnealed_note(noteText);
                    if (bmpImage != null) {
//                        annealedData.setAnnealed_image(DataConverter.convertImage2ByteArray(bmpImage));
                        if (cameraFlag) {
                            File sd = getCacheDir();
                            File folder = new File(sd, "/myfolder/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdirs();
                                }
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                            String timeStamp = dateFormat.format(new Date());
                            String imageFileName = "picture_" + timeStamp + ".jpg";

                            File fileName = new File(folder, imageFileName);
                            imgepath = fileName.getAbsolutePath();

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        annealedData.setAnnealed_path(imgepath);
                    }

                    dao.insertAnnealedData(annealedData);
                    startActivity(new Intent(this, OrderListActivity.class));
                    finish();
                }
            } else if (glassTypePosition == 4) {
                if (TextUtils.isEmpty(binding.etGlass1.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 1 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGlass2.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 2 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etGlass3.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass 3 details", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etWidth.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass width", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etHeight.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter glass height", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.etQuantity.getText().toString())) {
                    Toast.makeText(InquiryFormActivity.this, "Please enter quantity", Toast.LENGTH_SHORT).show();
                } else {
                    LaminatedDGUTable lDguData = new LaminatedDGUTable();
                    lDguData.setLdgu_standard(selectedStandard);
                    lDguData.setLdgu_glass_1(glass1);
                    lDguData.setLdgu_glass_2(glass2);
                    lDguData.setLdgu_glass_3(glass3);
                    lDguData.setLdgu_gap(String.valueOf(gapPosition));
                    lDguData.setLdgu_pvb(String.valueOf(pvbPosition));
                    lDguData.setLdgu_glassWidth(glassWidthText);
                    lDguData.setLdgu_glassHeight(glassHeightText);
                    lDguData.setLdgu_quantity(quantityText);
                    lDguData.setLdgu_note(noteText);
                    if (bmpImage != null) {
//                        lDguData.setLdgu_image(DataConverter.convertImage2ByteArray(bmpImage));
                        if (cameraFlag) {
                            File sd = getCacheDir();
                            File folder = new File(sd, "/myfolder/");
                            if (!folder.exists()) {
                                if (!folder.mkdir()) {
                                    Log.e("ERROR", "Cannot create a directory!");
                                } else {
                                    folder.mkdirs();
                                }
                            }

                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss");
                            String timeStamp = dateFormat.format(new Date());
                            String imageFileName = "picture_" + timeStamp + ".jpg";

                            File fileName = new File(folder, imageFileName);
                            imgepath = fileName.getAbsolutePath();

                            try {
                                FileOutputStream outputStream = new FileOutputStream(String.valueOf(fileName));
                                bmpImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                                outputStream.close();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        lDguData.setLdgu_path(imgepath);
                    }
                    dao.insertLaminatedDGUData(lDguData);
                    startActivity(new Intent(this, OrderListActivity.class));
                    finish();
                }
            }
//            }
//            else {
//                Toast.makeText(InquiryFormActivity.this, "Select Photo", Toast.LENGTH_SHORT).show();
//            }
            SharedPreferences sharedPreferencesSgu = getSharedPreferences("FormDataSguPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditSgu = sharedPreferencesSgu.edit();
            SharedPreferences sharedPreferencesDgu = getSharedPreferences("FormDataDguPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditSguDgu = sharedPreferencesDgu.edit();
            SharedPreferences sharedPreferencesLamination = getSharedPreferences("FormDataLaminationPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditLamination = sharedPreferencesLamination.edit();
            SharedPreferences sharedPreferencesAnneald = getSharedPreferences("FormDataAnnealedPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditAnneald = sharedPreferencesAnneald.edit();
            SharedPreferences sharedPreferencesLDgu = getSharedPreferences("FormDataLDguPref", MODE_PRIVATE);
            SharedPreferences.Editor myEditLDgu = sharedPreferencesLDgu.edit();

            if (glassTypePosition == 0) {
                myEditSgu.putString("glassType", "SGU");
                myEditSgu.putString("standard", selectedStandard);
                myEditSgu.putString("thikness", thicknessText);
                myEditSgu.putString("material", materialDetailsText);
                myEditSgu.putString("width", glassWidthText);
                myEditSgu.putString("height", glassHeightText);
                myEditSgu.putString("quantity", quantityText);
                myEditSgu.putString("note", noteText);
                myEditSgu.putString("image", imgepath);
                myEditSgu.commit();
            } else if (glassTypePosition == 1) {
                myEditSguDgu.putString("glassType", "DGU");
                myEditSguDgu.putString("standard", selectedStandard);
                myEditSguDgu.putString("glass1", glass1);
                myEditSguDgu.putString("glass2", glass2);
                myEditSguDgu.putString("gapPosition", String.valueOf(gapPosition));
                myEditSguDgu.putString("width", glassWidthText);
                myEditSguDgu.putString("height", glassHeightText);
                myEditSguDgu.putString("note", noteText);
                myEditSguDgu.putString("quantity", quantityText);
                myEditSguDgu.putString("image", imgepath);
                myEditSguDgu.commit();
            } else if (glassTypePosition == 2) {
                myEditLamination.putString("glassType", "Lamination");
                myEditLamination.putString("standard", selectedStandard);
                myEditLamination.putString("glass1", glass1);
                myEditLamination.putString("glass2", glass2);
                myEditLamination.putString("pvbPosition", String.valueOf(pvbPosition));
                myEditLamination.putString("width", glassWidthText);
                myEditLamination.putString("height", glassHeightText);
                myEditLamination.putString("note", noteText);
                myEditLamination.putString("quantity", quantityText);
                myEditLamination.putString("image", imgepath);
                myEditLamination.commit();
            } else if (glassTypePosition == 3) {
                myEditAnneald.putString("glassType", "Annealed");
                myEditAnneald.putString("standard", selectedStandard);
                myEditAnneald.putString("thikness", thicknessText);
                myEditAnneald.putString("material", materialDetailsText);
                myEditAnneald.putString("width", glassWidthText);
                myEditAnneald.putString("height", glassHeightText);
                myEditAnneald.putString("quantity", quantityText);
                myEditAnneald.putString("note", noteText);
                myEditAnneald.putString("image", imgepath);
                myEditAnneald.commit();
            } else if (glassTypePosition == 4) {
                myEditLDgu.putString("glassType", "LaminatedDGU");
                myEditLDgu.putString("standard", selectedStandard);
//              myEditLDguit.putString("thikness", thicknessText);
//              myEditLDguit.putString("material", materialDetailsText);
                myEditLDgu.putString("width", glassWidthText);
                myEditLDgu.putString("height", glassHeightText);
                myEditLDgu.putString("quantity", quantityText);
                myEditLDgu.putString("note", noteText);
                myEditLDgu.putString("glass1", glass1);
                myEditLDgu.putString("glass2", glass2);
                myEditLDgu.putString("glass3", glass3);
                myEditLDgu.putString("gapPosition", String.valueOf(gapPosition));
                myEditLDgu.putString("pvbPosition", String.valueOf(pvbPosition));
                myEditLDgu.putString("image", imgepath);
                myEditLDgu.commit();
            }

           /* myEditSgu.putString("standard", selectedStandard);
            myEditSgu.putString("thikness", thicknessText);
            myEditSgu.putString("material", materialDetailsText);
            myEditSgu.putString("width", glassWidthText);
            myEditSgu.putString("height", glassHeightText);
            myEditSgu.putString("quantity", quantityText);
            myEditSgu.putString("note", noteText);
            myEditSgu.putString("glass1", glass1);
            myEditSgu.putString("glass2", glass2);
            myEditSgu.putString("glass3", glass3);
            myEditSgu.putString("gapPosition", String.valueOf(gapPosition));
            myEditSgu.putString("pvbPosition", String.valueOf(pvbPosition));
            myEditSgu.commit();*/
        });

//        getAllData();
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

                    binding.tvGap.setVisibility(View.GONE);
                    binding.cvGap.setVisibility(View.GONE);
                    binding.spGap.setVisibility(View.GONE);
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

                    binding.cvPvb.setVisibility(View.VISIBLE);
                    binding.spPvb.setVisibility(View.VISIBLE);
                    binding.tvPvb.setVisibility(View.VISIBLE);
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
                    binding.spGap.setVisibility(View.VISIBLE);
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
                    binding.cvPvb.setVisibility(View.VISIBLE);
                    binding.spPvb.setVisibility(View.VISIBLE);
                    binding.tvPvb.setVisibility(View.VISIBLE);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                binding.ivUploadPhoto.setVisibility(View.GONE);
                binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
                try {
                    imgepath = PathUtil.getPath(this, uri);
//                    imgepath =uri.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    bmpImage = getBitmapFromUri(uri);
                    binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            binding.ivUploadPhoto.setVisibility(View.GONE);
            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
            bmpImage = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            try {
                imgepath = PathUtil.getPath(this, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
//                bmpImage = getBitmapFromUri(uri);
                cameraFlag = true;
                binding.ivUploadPhoto1.setImageBitmap(bmpImage);
            } catch (Exception e) {
                e.printStackTrace();
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