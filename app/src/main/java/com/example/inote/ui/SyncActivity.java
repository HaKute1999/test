package com.example.inote.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.Converters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SyncActivity extends AppCompatActivity {
     RelativeLayout rlSync,rlImport;
     List<Note> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);
        rlSync = findViewById(R.id.rlSync);
        rlImport = findViewById(R.id.rlImport);
        rlSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes = ConfigUtils.convertImageBase64();
               String result = new Gson().toJson(notes);
               ConfigUtils.wirteFile(getApplicationContext(),result);
            }
        });
        rlImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStorageFile();
            }
        });
    }
    public final void getStorageFile() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        intent.putExtra("CONTENT_TYPE", "*/*");
        startActivityForResult(Intent.createChooser(intent, "View Default File Manager"), 4228);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri;
        if (requestCode ==4228){
            uri = data.getData();
            try {
                InputStream in = getContentResolver().openInputStream(uri);
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                String content = total.toString();
                Type listType = new TypeToken<ArrayList<Note>>(){}.getType();

                List<Note> list = new Gson().fromJson(content,listType );
                for (Note note: list){
                    AppDatabase.noteDB.getNoteDAO().insert(new Note(note.getIdFolder(),note.isPinned(),note.getListImage(),note.getProtectionType(),note.getTimeEdit(),note.getTitle(),note.getType(),note.getValue(),note.getValueChecklist()));
                }
                 Toast.makeText(getApplicationContext(),getResources().getString(R.string.importing_successful),Toast.LENGTH_SHORT).show();
            }catch (Exception e) {

            }

        }
    }
}