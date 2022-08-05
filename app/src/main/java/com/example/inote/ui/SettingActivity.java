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
import com.example.inote.view.ConfigUtils;
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConfigUtils.getConFigDark(getApplicationContext(),ids(R.id.tv_display),ids(R.id.tv_dark),ids(R.id.tv_light)
                ,ids(R.id.tvTopCheckList),ids(R.id.tvChange),ids(R.id.tvChange2),ids(R.id.tvSync),
                ids(R.id.tv_share_app),ids(R.id.tv_rate),ids(R.id.tv_rate_us),ids(R.id.tv_policy),ids(R.id.view5x),
                ids(R.id.view9),ids(R.id.llSt1),ids(R.id.llSt2)
                ,ids(R.id.viewChecklist),ids(R.id.viewPass),ids(R.id.viewPass2),ids(R.id.view8),ids(R.id.view10));
        ConfigUtils.darkBlack(ids(R.id.root_setting));

    }
}