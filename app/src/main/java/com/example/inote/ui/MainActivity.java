package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.adapter.FolderAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rl_gomain;
    ImageView ivAddFolder;
    ImageView ivAddNote;
    TextView size_list1;
    TextView size_list2;
    TextView sizeDelete;
    RecyclerView listFolder;
    List<Folder> listData;
    FolderAdapter folderAdapter;
 private static final String DB_NAME = "notes.db";
    AppDatabase noteDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            if (window != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        noteDb = AppDatabase.getInstance(this,DB_NAME);
        setupListFolder();

    }
    private void initView(){
        rl_gomain = findViewById(R.id.rl_gomain);
        ivAddFolder = findViewById(R.id.ivAddFolder);
        ivAddNote = findViewById(R.id.ivAddNote);
        size_list1 = findViewById(R.id.size_list1);
        size_list2 = findViewById(R.id.size_list2);
        sizeDelete = findViewById(R.id.sizeDelete);
        listFolder = findViewById(R.id.listFolder);
        rl_gomain.setOnClickListener(this);
        ivAddFolder.setOnClickListener(this);
        ivAddNote.setOnClickListener(this);
    }
    private void setupListFolder(){
        listData  =new ArrayList<>();
        listData = noteDb.getFolderDAO().getAllFolder();
        listFolder.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(listData);
        listFolder.setAdapter(folderAdapter);
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
        if (id == R.id.ivAddFolder){
            showDialog(MainActivity.this);
        }
        if (id == R.id.ivAddNote){
            Intent i = new Intent(MainActivity.this,AddNoteActivity.class);
            startActivity(i);
        }
    }
    public void showDialog(Activity activity){
        final Dialog dialog = new Dialog(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_create_folder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvCancelFolder = dialog.findViewById(R.id.tvCancelFolder);
        EditText edtFolder = dialog.findViewById(R.id.edtFolder);

        tvCancelFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tvOkFolder = dialog.findViewById(R.id.tvOkFolder);
        tvOkFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFolder.getText() == null || edtFolder.getText().toString() == "") {
                    dialog.dismiss();
                    return;
                }else {
                    noteDb.getFolderDAO().insert(new Folder("",edtFolder.getText().toString()));
                    setupListFolder();
                }
                dialog.dismiss();

            }
        });

        dialog.show();

    }
}