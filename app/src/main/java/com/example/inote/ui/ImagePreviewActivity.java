package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.inote.R;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.ZoomImageView;

import java.io.File;

public class ImagePreviewActivity extends BaseActivity {
    ZoomImageView zoomImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        setContentView(R.layout.activity_image_preview);
        zoomImageView = findViewById(R.id.iv_zoom);
        onBack();
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        if (path.contains("storage")){
            File file = new File(path);
            Uri uri = Uri.fromFile(file);
            Glide.with(this)
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(zoomImageView);
        }else {
            ConfigUtils.convertBase64toImage(zoomImageView,path);

        }
        ConfigUtils.darkBlack(ids(R.id.rl_top));
        ConfigUtils.darkBlack(ids(R.id.homeNote));

    }
}