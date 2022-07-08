package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl_gomain;
 private static final String DB_NAME = "notes.db";
    AppDatabase noteDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl_gomain = findViewById(R.id.rl_gomain);
        rl_gomain.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        noteDb = AppDatabase.getInstance(this,DB_NAME);


    }

    @Override
    protected void onResume() {
        AppDatabase.doesDatabaseExist(this,DB_NAME);
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_gomain){
            Intent i = new Intent(MainActivity.this,NotesActivity.class);
            startActivity(i);
        }
    }
}