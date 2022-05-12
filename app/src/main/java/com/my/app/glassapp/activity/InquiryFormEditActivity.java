package com.my.app.glassapp.activity;

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
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.my.app.glassapp.PathUtil;
import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.R;
import com.my.app.glassapp.database.DBDao;
import com.my.app.glassapp.database.DBRoom;
import com.my.app.glassapp.databinding.ActivityInquiryFormEditBinding;
import com.my.app.glassapp.model.AnnealedTable;
import com.my.app.glassapp.model.DGUTable;
import com.my.app.glassapp.model.LaminatedDGUTable;
import com.my.app.glassapp.model.LaminationTable;
import com.my.app.glassapp.model.SGUTable;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class InquiryFormEditActivity extends AppCompatActivity {

    public static final int SELECT_PICTURE = 101;

    ActivityInquiryFormEditBinding binding;
    String[] glassTypeCategories = {"SGU", "DGU", "Lamination", "Annealed", "LaminatedDGU"};
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
    private int sguTableDataPosition;
    private String glassType;
    private List<SGUTable> sgudata;
    private List<DGUTable> dgudata;
    private List<LaminationTable> laminationdata;
    private List<AnnealedTable> annealeddata;
    private List<LaminatedDGUTable> laminatedDgudata;
    Bitmap bmpImage = null;
    String imgepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityInquiryFormEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sguTableDataPosition = getIntent().getIntExtra("TablePosition", 1);
        glassType = getIntent().getStringExtra("GlassType");

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

        if (glassType.equals("SGU")) {
            binding.spGlassType.setEnabled(false);
            binding.spGlassType.setClickable(false);
            binding.tvGap.setVisibility(View.GONE);
            binding.cvGap.setVisibility(View.GONE);
            binding.tvGlass1.setVisibility(View.GONE);
            binding.tvGlass2.setVisibility(View.GONE);
            binding.cvGlass1.setVisibility(View.GONE);
            binding.cvGlass2.setVisibility(View.GONE);
            binding.cvThickness.setVisibility(View.VISIBLE);
            binding.cvMaterialDetails.setVisibility(View.VISIBLE);
            binding.tvThickness.setVisibility(View.VISIBLE);
            binding.tvMaterialDetails.setVisibility(View.VISIBLE);
            binding.cvPvb.setVisibility(View.GONE);
            binding.spPvb.setVisibility(View.GONE);
            binding.tvPvb.setVisibility(View.GONE);
        } else if (glassType.equals("DGU")) {
            binding.spGlassType.setEnabled(false);
            binding.spGlassType.setClickable(false);
            binding.tvGap.setVisibility(View.VISIBLE);
            binding.cvGap.setVisibility(View.VISIBLE);
            binding.tvGlass1.setVisibility(View.VISIBLE);
            binding.tvGlass2.setVisibility(View.VISIBLE);
            binding.cvGlass1.setVisibility(View.VISIBLE);
            binding.cvGlass2.setVisibility(View.VISIBLE);
            binding.cvThickness.setVisibility(View.GONE);
            binding.cvMaterialDetails.setVisibility(View.GONE);
            binding.tvThickness.setVisibility(View.GONE);
            binding.tvMaterialDetails.setVisibility(View.GONE);
            binding.cvPvb.setVisibility(View.GONE);
            binding.spPvb.setVisibility(View.GONE);
            binding.tvPvb.setVisibility(View.GONE);
        } else if (glassType.equals("Lamination")) {
            binding.spGlassType.setEnabled(false);
            binding.spGlassType.setClickable(false);
            binding.cvPvb.setVisibility(View.VISIBLE);
            binding.spPvb.setVisibility(View.VISIBLE);
            binding.tvPvb.setVisibility(View.VISIBLE);
            binding.cvGap.setVisibility(View.GONE);
            binding.spGap.setVisibility(View.GONE);
            binding.tvGap.setVisibility(View.GONE);
        } else if (glassType.equals("Annealed")) {
            binding.spGlassType.setEnabled(false);
            binding.spGlassType.setClickable(false);
            binding.tvGap.setVisibility(View.GONE);
            binding.cvGap.setVisibility(View.GONE);
            binding.tvGlass1.setVisibility(View.GONE);
            binding.tvGlass2.setVisibility(View.GONE);
            binding.cvGlass1.setVisibility(View.GONE);
            binding.cvGlass2.setVisibility(View.GONE);
            binding.cvThickness.setVisibility(View.VISIBLE);
            binding.cvMaterialDetails.setVisibility(View.VISIBLE);
            binding.tvThickness.setVisibility(View.VISIBLE);
            binding.tvMaterialDetails.setVisibility(View.VISIBLE);
            binding.cvPvb.setVisibility(View.GONE);
            binding.spPvb.setVisibility(View.GONE);
            binding.tvPvb.setVisibility(View.GONE);
        } else if (glassType.equals("LaminatedDGU")) {
            binding.spGlassType.setEnabled(false);
            binding.spGlassType.setClickable(false);
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

        setGlassTypeSpinnerAdapter();
        setGapSpinnerAdapter();
        setPVBSpinnerAdapter();

        standardId = binding.radioGroup.getCheckedRadioButtonId();

        dao = DBRoom.getInstance(this).dao();
        if (sguTableDataPosition != 0) {
            if (glassType.equals("SGU")) {
                sgudata = dao.getSGUData();
                for (int i = 0; i < sgudata.size(); i++) {
                    if (sgudata.get(i).getSgu_id() == sguTableDataPosition) {
                        //Standard
                        if (sgudata.get(i).getSgu_standard().equals("inch")) {
                            binding.radioGroup.check(R.id.radioInch);
                        } else if (sgudata.get(i).getSgu_standard().equals("mm")) {
                            binding.radioGroup.check(R.id.radioMm);
                        }
                        //Thickness
                        binding.etThickness.setText(sgudata.get(i).getSgu_thickness());
                        //Material Details
                        binding.etMaterialDetail.setText(sgudata.get(i).getSgu_materialDetails());
                        //Glass Width
                        binding.etWidth.setText(sgudata.get(i).getSgu_glassWidth());
                        //Glass Height
                        binding.etHeight.setText(sgudata.get(i).getSgu_glassHeight());
                        //Quantity
                        binding.etQuantity.setText(sgudata.get(i).getSgu_quantity());
                        //Notes
                        binding.etNote.setText(sgudata.get(i).getSgu_note());
                        //Image
                        if (sgudata.get(i).getSgu_image() != null) {
                            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                            binding.ivUploadPhoto.setVisibility(View.GONE);
                            bmpImage = DataConverter.convertByteArray2Image(sgudata.get(i).getSgu_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        } else {
                            binding.ivUploadPhoto1.setVisibility(View.GONE);
                            binding.ivUploadPhoto.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (glassType.equals("DGU")) {
                dgudata = dao.getDGUData();
                for (int i = 0; i < dgudata.size(); i++) {
                    if (dgudata.get(i).getDgu_id() == sguTableDataPosition) {
                        //Standard
                        if (dgudata.get(i).getDgu_standard().equals("inch")) {
                            binding.radioGroup.check(R.id.radioInch);
                        } else if (dgudata.get(i).getDgu_standard().equals("mm")) {
                            binding.radioGroup.check(R.id.radioMm);
                        }
                        //Thickness
                        binding.etGlass1.setText(dgudata.get(i).getDgu_glass_1());
                        //Material Details
                        binding.etGlass2.setText(dgudata.get(i).getDgu_glass_2());
                        //Gap
                        if (dgudata.get(i).getDgu_gap().equals("0")) {
                            binding.spGap.setSelection(0);
                        } else if (dgudata.get(i).getDgu_gap().equals("1")) {
                            binding.spGap.setSelection(1);
                        } else if (dgudata.get(i).getDgu_gap().equals("2")) {
                            binding.spGap.setSelection(2);
                        } else if (dgudata.get(i).getDgu_gap().equals("3")) {
                            binding.spGap.setSelection(3);
                        } else if (dgudata.get(i).getDgu_gap().equals("4")) {
                            binding.spGap.setSelection(4);
                        } else if (dgudata.get(i).getDgu_gap().equals("5")) {
                            binding.spGap.setSelection(5);
                        } else if (dgudata.get(i).getDgu_gap().equals("6")) {
                            binding.spGap.setSelection(6);
                        } else if (dgudata.get(i).getDgu_gap().equals("7")) {
                            binding.spGap.setSelection(7);
                        } else if (dgudata.get(i).getDgu_gap().equals("8")) {
                            binding.spGap.setSelection(8);
                        } else if (dgudata.get(i).getDgu_gap().equals("9")) {
                            binding.spGap.setSelection(9);
                        }
                        //Glass Width
                        binding.etWidth.setText(dgudata.get(i).getDgu_glassWidth());
                        //Glass Height
                        binding.etHeight.setText(dgudata.get(i).getDgu_glassHeight());
                        //Quantity
                        binding.etQuantity.setText(dgudata.get(i).getDgu_quantity());
                        //Notes
                        binding.etNote.setText(dgudata.get(i).getDgu_note());
                        //Image
                        /*if (dgudata.get(i).getDgu_image() != null) {
                            bmpImage = DataConverter.convertByteArray2Image(dgudata.get(i).getDgu_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        }*/
                        if (dgudata.get(i).getDgu_image() != null) {
                            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                            binding.ivUploadPhoto.setVisibility(View.GONE);
                            bmpImage = DataConverter.convertByteArray2Image(dgudata.get(i).getDgu_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        } else {
                            binding.ivUploadPhoto1.setVisibility(View.GONE);
                            binding.ivUploadPhoto.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (glassType.equals("Lamination")) {
                laminationdata = dao.getLaminationData();
                for (int i = 0; i < laminationdata.size(); i++) {
                    if (laminationdata.get(i).getLamination_id() == sguTableDataPosition) {
                        //Standard
                        if (laminationdata.get(i).getLamination_standard().equals("inch")) {
                            binding.radioGroup.check(R.id.radioInch);
                        } else if (laminationdata.get(i).getLamination_standard().equals("mm")) {
                            binding.radioGroup.check(R.id.radioMm);
                        }
                        //Thickness
                        binding.etThickness.setText(laminationdata.get(i).getLamination_thickness());
                        //Material Details
                        binding.etMaterialDetail.setText(laminationdata.get(i).getLamination_materialDetails());
                        //PVB
                        if (laminationdata.get(i).getLamination_pvb().equals("0")) {
                            binding.spPvb.setSelection(0);
                        } else if (laminationdata.get(i).getLamination_pvb().equals("1")) {
                            binding.spPvb.setSelection(1);
                        } else if (laminationdata.get(i).getLamination_pvb().equals("2")) {
                            binding.spPvb.setSelection(2);
                        } else if (laminationdata.get(i).getLamination_pvb().equals("3")) {
                            binding.spPvb.setSelection(3);
                        }
                        //Glass Width
                        binding.etWidth.setText(laminationdata.get(i).getLamination_glassWidth());
                        //Glass Height
                        binding.etHeight.setText(laminationdata.get(i).getLamination_glassHeight());
                        //Quantity
                        binding.etQuantity.setText(laminationdata.get(i).getLamination_quantity());
                        //Notes
                        binding.etNote.setText(laminationdata.get(i).getLamination_note());
                        //Image
                        if (laminationdata.get(i).getLamination_image() != null) {
                            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                            binding.ivUploadPhoto.setVisibility(View.GONE);
                            bmpImage = DataConverter.convertByteArray2Image(laminationdata.get(i).getLamination_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        } else {
                            binding.ivUploadPhoto1.setVisibility(View.GONE);
                            binding.ivUploadPhoto.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (glassType.equals("Annealed")) {
                annealeddata = dao.getAnnealedData();
                for (int i = 0; i < annealeddata.size(); i++) {
                    if (annealeddata.get(i).getAnnealed_id() == sguTableDataPosition) {
                        //Standard
                        if (annealeddata.get(i).getAnnealed_standard().equals("inch")) {
                            binding.radioGroup.check(R.id.radioInch);
                        } else if (annealeddata.get(i).getAnnealed_standard().equals("mm")) {
                            binding.radioGroup.check(R.id.radioMm);
                        }
                        //Thickness
                        binding.etThickness.setText(annealeddata.get(i).getAnnealed_thickness());
                        //Material Details
                        binding.etMaterialDetail.setText(annealeddata.get(i).getAnnealed_materialDetails());
                        //Glass Width
                        binding.etWidth.setText(annealeddata.get(i).getAnnealed_glassWidth());
                        //Glass Height
                        binding.etHeight.setText(annealeddata.get(i).getAnnealed_glassHeight());
                        //Quantity
                        binding.etQuantity.setText(annealeddata.get(i).getAnnealed_quantity());
                        //Notes
                        binding.etNote.setText(annealeddata.get(i).getAnnealed_note());
                        //Image
                        if (annealeddata.get(i).getAnnealed_image() != null) {
                            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                            binding.ivUploadPhoto.setVisibility(View.GONE);
                            bmpImage = DataConverter.convertByteArray2Image(annealeddata.get(i).getAnnealed_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        } else {
                            binding.ivUploadPhoto1.setVisibility(View.GONE);
                            binding.ivUploadPhoto.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } else if (glassType.equals("LaminatedDGU")) {
                laminatedDgudata = dao.getLDGUData();
                for (int i = 0; i < laminatedDgudata.size(); i++) {
                    if (laminatedDgudata.get(i).getLdgu_id() == sguTableDataPosition) {
                        //Standard
                        if (laminatedDgudata.get(i).getLdgu_standard().equals("inch")) {
                            binding.radioGroup.check(R.id.radioInch);
                        } else if (laminatedDgudata.get(i).getLdgu_standard().equals("mm")) {
                            binding.radioGroup.check(R.id.radioMm);
                        }
                        //Thickness
                        binding.etGlass1.setText(laminatedDgudata.get(i).getLdgu_glass_1());
                        //Material Details
                        binding.etGlass2.setText(laminatedDgudata.get(i).getLdgu_glass_2());
                        binding.etGlass3.setText(laminatedDgudata.get(i).getLdgu_glass_3());
                        //Gap
                        if (laminatedDgudata.get(i).getLdgu_gap().equals("0")) {
                            binding.spGap.setSelection(0);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("1")) {
                            binding.spGap.setSelection(1);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("2")) {
                            binding.spGap.setSelection(2);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("3")) {
                            binding.spGap.setSelection(3);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("4")) {
                            binding.spGap.setSelection(4);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("5")) {
                            binding.spGap.setSelection(5);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("6")) {
                            binding.spGap.setSelection(6);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("7")) {
                            binding.spGap.setSelection(7);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("8")) {
                            binding.spGap.setSelection(8);
                        } else if (laminatedDgudata.get(i).getLdgu_gap().equals("9")) {
                            binding.spGap.setSelection(9);
                        }
                        //Glass Width
                        binding.etWidth.setText(laminatedDgudata.get(i).getLdgu_glassWidth());
                        //Glass Height
                        binding.etHeight.setText(laminatedDgudata.get(i).getLdgu_glassHeight());
                        //Quantity
                        binding.etQuantity.setText(laminatedDgudata.get(i).getLdgu_quantity());
                        //Notes
                        binding.etNote.setText(laminatedDgudata.get(i).getLdgu_note());
                        //Image
                        /*if (laminatedDgudata.get(i).getDgu_image() != null) {
                            bmpImage = DataConverter.convertByteArray2Image(laminatedDgudata.get(i).getDgu_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        }*/
                        if (laminatedDgudata.get(i).getLdgu_image() != null) {
                            binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                            binding.ivUploadPhoto.setVisibility(View.GONE);
                            bmpImage = DataConverter.convertByteArray2Image(laminatedDgudata.get(i).getLdgu_image());
                            binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                        } else {
                            binding.ivUploadPhoto1.setVisibility(View.GONE);
                            binding.ivUploadPhoto.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

        binding.ivBack.setOnClickListener(view -> {
            onBackPressed();
        });

        binding.cvCancel.setOnClickListener(view -> {
            finish();
        });

        binding.imgLogout.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPref", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("login", false);
            myEdit.commit();
            startActivity(new Intent(InquiryFormEditActivity.this, MainActivity.class));
            finish();
        });

        binding.ivUploadPhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        });

        binding.ivUploadPhoto1.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            if (bmpImage != null) {
//                Intent intent = new Intent(this, ViewImageActivity.class);
//                intent.putExtra("img", DataConverter.convertImage2ByteArray(bmpImage));
//                startActivity(intent);
            }
        });

        binding.cvSubmit.setOnClickListener(view -> {
//            if (bmpImage != null) {
            if (standardId == R.id.radioInch) {
                selectedStandard = "inch";
            } else if (standardId == R.id.radioMm) {
                selectedStandard = "mm";
            }
            {
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

                if (glassType.equals("SGU")) {
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
                    if (sguTableDataPosition != 0) {
                        if (bmpImage == null) {
                            dao.updateSGUTable1(sguTableDataPosition, selectedStandard, thicknessText, materialDetailsText,
                                    glassWidthText, glassHeightText, quantityText, noteText);
                        } else {
                            dao.updateSGUTable(sguTableDataPosition, selectedStandard, thicknessText, materialDetailsText,
                                    glassWidthText, glassHeightText, quantityText, noteText,imgepath, DataConverter.convertImage2ByteArray(bmpImage));
                        }
                    } else {
                        dao.insertSGUData(sguData);
                    }
                } else if (glassType.equals("DGU")) {
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
//                dao.insertDGUData(sguData);
                    if (sguTableDataPosition != 0) {
                        if (bmpImage == null) {
                            dao.updateDGUTable1(sguTableDataPosition, selectedStandard, glass1, glass2, String.valueOf(gapPosition),
                                    glassWidthText, glassHeightText, quantityText, noteText);
                        } else {
                            dao.updateDGUTable(sguTableDataPosition, selectedStandard, glass1, glass2, String.valueOf(gapPosition),
                                    glassWidthText, glassHeightText, quantityText, noteText,imgepath, DataConverter.convertImage2ByteArray(bmpImage));
                        }
                    } else {
                        dao.insertDGUData(dguData);
                    }

                }
                else if (glassType.equals("Lamination")) {
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
                    if (sguTableDataPosition != 0) {
                        if (bmpImage == null) {
                            dao.updateLaminationTable1(sguTableDataPosition, selectedStandard, thicknessText,
                                    String.valueOf(pvbPosition), materialDetailsText, glassWidthText,
                                    glassHeightText, quantityText, noteText);
                        } else {
                            dao.updateLaminationTable(sguTableDataPosition, selectedStandard, thicknessText,
                                    String.valueOf(pvbPosition), materialDetailsText, glassWidthText,
                                    glassHeightText, quantityText, noteText, imgepath,
                                    DataConverter.convertImage2ByteArray(bmpImage));
                        }
                    } else {
                        dao.insertLaminationData(laminationData);
                    }
                } else if (glassType.equals("Annealed")) {
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
                    if (sguTableDataPosition != 0) {
                        if (bmpImage == null) {
                            dao.updateAnnealedTable1(sguTableDataPosition, selectedStandard, thicknessText, materialDetailsText,
                                    glassWidthText, glassHeightText, quantityText, noteText);
                        } else {
                            dao.updateAnnealedTable(sguTableDataPosition, selectedStandard, thicknessText, materialDetailsText,
                                    glassWidthText, glassHeightText, quantityText, noteText,imgepath, DataConverter.convertImage2ByteArray(bmpImage));
                        }
                    } else {
                        dao.insertAnnealedData(annealedData);
                    }
                } else if (glassType.equals("LaminatedDGU")) {
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
//                dao.insertDGUData(sguData);
                    if (sguTableDataPosition != 0) {
                        if (bmpImage == null) {
                            dao.updateLDGUTable1(sguTableDataPosition, selectedStandard, glass1, glass2, glass3, String.valueOf(gapPosition),
                                    glassWidthText, glassHeightText, quantityText, noteText);
                        } else {
                            dao.updateLDGUTable(sguTableDataPosition, selectedStandard, glass1, glass2, glass3, String.valueOf(gapPosition),
                                    glassWidthText, glassHeightText, quantityText, noteText, imgepath, DataConverter.convertImage2ByteArray(bmpImage));
                        }
                    } else {
                        dao.insertLaminatedDGUData(lDguData);
                    }

                }
            }
//            } else {
//                Toast.makeText(InquiryFormEditActivity.this, "Select Photo", Toast.LENGTH_SHORT).show();
//            }
        });
    }

    public void setGlassTypeSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, glassTypeCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spGlassType.setAdapter(dataAdapter);
        if (glassType.equals("SGU")) {
            binding.spGlassType.setSelection(0);
        } else if (glassType.equals("DGU")) {
            binding.spGlassType.setSelection(1);
        } else if (glassType.equals("Lamination")) {
            binding.spGlassType.setSelection(2);
        } else if (glassType.equals("Annealed")) {
            binding.spGlassType.setSelection(3);
        } else if (glassType.equals("LaminatedDGU")) {
            binding.spGlassType.setSelection(4);
        }
    }

    public void setGapSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, gapCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spGap.setAdapter(dataAdapter);
    }

    public void setPVBSpinnerAdapter() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_dropdown_item, pvbCategories);
        dataAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        binding.spPvb.setAdapter(dataAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
//                File finalFile = new File(getRealPathFromURI(uri));
                try {
                    imgepath = PathUtil.getPath(this,uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                try {
                    bmpImage = getBitmapFromUri(uri);
                    if (bmpImage != null) {
                        binding.ivUploadPhoto.setVisibility(View.GONE);
                        binding.ivUploadPhoto1.setVisibility(View.VISIBLE);
                        binding.ivUploadPhoto1.setImageBitmap(bmpImage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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