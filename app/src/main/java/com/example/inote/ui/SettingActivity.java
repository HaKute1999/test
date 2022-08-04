package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.view.ShareUtils;

public class SettingActivity extends BaseActivity {
  TextView tv_dark,tv_light;
  RadioButton dark_on,light_on;
  RelativeLayout rlSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_setting);
        onBack();
        tv_dark = findViewById(R.id.tv_dark);
        tv_light = findViewById(R.id.tv_light);
        light_on = findViewById(R.id.light_on);
        dark_on = findViewById(R.id.dark_on);
        rlSync = findViewById(R.id.rlSync);
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) ==false){
            light_on.setChecked(true);
        }else dark_on.setChecked(true);
        light_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ShareUtils.setBool(ShareUtils.CONFIG_DARK,false);
                    dark_on.setChecked(false);
                }
            }
        });
        dark_on.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    ShareUtils.setBool(ShareUtils.CONFIG_DARK,true);
                    light_on.setChecked(false);
                }
            }
        });
        tv_dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (light_on.isChecked()){
                    ShareUtils.setBool(ShareUtils.CONFIG_DARK,true);
                    light_on.setChecked(false);
                    dark_on.setChecked(true);
                }
            }
        });
        tv_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dark_on.isChecked()){
                    ShareUtils.setBool(ShareUtils.CONFIG_DARK,false);
                    light_on.setChecked(true);
                    dark_on.setChecked(false);
                }
            }
        });
        rlSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inte = new Intent(SettingActivity.this,SyncActivity.class);
                startActivity(inte);
            }
        });
    }
}