package com.my.app.glassapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.WindowManager;

import com.my.app.glassapp.utils.DataConverter;
import com.my.app.glassapp.databinding.ActivityViewImageBinding;

public class ViewImageActivity extends AppCompatActivity {

    private byte[] imgBitmap;
    private Bitmap bmpImage;
    ActivityViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityViewImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        imgBitmap = getIntent().getByteArrayExtra("img");
        bmpImage = DataConverter.convertByteArray2Image(imgBitmap);

        binding.ivImage.setImageBitmap(bmpImage);
    }
}