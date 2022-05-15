package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    private boolean imgBitmap;
    private Bitmap bmpImg;
    ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imgBitmap = getIntent().getBooleanExtra("img1", false);
        if (imgBitmap)
            this.bmpImg = InquiryFormEditActivity.bmpImage;
        else
            this.bmpImg = InquiryFormActivity.bmpImage;
//        bmpImg = DataConverter.convertByteArray2Image(imgBitmap);

        binding.ivImage.setImageBitmap(bmpImg);
    }
}