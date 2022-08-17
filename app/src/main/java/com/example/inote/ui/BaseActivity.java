package com.example.inote.ui;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.view.ShareUtils;

import java.util.List;

public class BaseActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 102;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ShareUtils(getApplicationContext());

    }

    public void setFullScreen() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                if (ShareUtils.getBool(ShareUtils.CONFIG_DARK)){
                    window.setStatusBarColor(getColor(R.color.black));
                }else {
                    window.setStatusBarColor(getColor(R.color.color_main));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
    }

    public void onBack() {
        findViewById(R.id.tvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void requestStoragePermission(Activity activity, int requestcode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        }
        ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);
    }

    public boolean checkReadExternalStoragePermission(Activity activity, int requestcode) {
        ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((activity),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestcode);
            return false;
        } else return true;
    }

    public boolean permission(Activity activity) {
        requestStoragePermission(activity, REQUEST_CODE);
        return checkReadExternalStoragePermission(activity, REQUEST_CODE);
    }
    public View ids(int id){
       return findViewById(id);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
