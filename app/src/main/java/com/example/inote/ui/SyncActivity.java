package com.example.inote.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.BaseBundle;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inote.BuildConfig;
import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.Converters;
import com.example.inote.view.IUpdate;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SyncActivity extends BaseActivity implements IUpdate {
     RelativeLayout rlSync,rlImport,rlFile;
     List<Note> notes;
     ImageView ivShareBackup;
     File file;
     TextView tv2;
     TextView tv1,numberSync;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_sync);
        onBack();
        rlSync = findViewById(R.id.rlSync);
        rlImport = findViewById(R.id.rlImport);
        rlFile = findViewById(R.id.rlFile);
        ivShareBackup = findViewById(R.id.ivShareBackup);
        tv2 = findViewById(R.id.tv2);
        tv1 = findViewById(R.id.tv1);
        numberSync = findViewById(R.id.numberSync);
        file = findFile();
        numberSync.setText(getString(R.string.number_of_notes_synchronized) + " "+AppDatabase.noteDB.getNoteDAO().getAllNotes().size());
        if (file != null){
            tv1.setText(file.getName());
            tv2.setText(file.getAbsolutePath());
            rlFile.setVisibility(View.VISIBLE);
        }
        rlSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notes = ConfigUtils.convertImageBase64();
               String result = new Gson().toJson(notes);
               ConfigUtils.wirteFile(getApplicationContext(),result,SyncActivity.this);
                rlFile.setVisibility(View.VISIBLE);


            }
        });
        rlImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStorageFile();
            }
        });
        ivShareBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (file != null){
                    shareFile(file);
                }
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
        if (requestCode ==4228 && data!=null){
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
                numberSync.setText(getString(R.string.number_of_notes_synchronized) + " "+AppDatabase.noteDB.getNoteDAO().getAllNotes().size());

                Toast.makeText(getApplicationContext(),getResources().getString(R.string.importing_successful),Toast.LENGTH_SHORT).show();
            }catch (Exception e) {

            }

        }
    }
    private File  findFile(){
        List<File> list = new ArrayList<>();
        long count = 0;

        File file =new File( Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
        for (File child : file.listFiles()){
            if (child.getName().contains("BackUpINoteIos_")){
                if (child.lastModified() > count){
                    count = child.lastModified();
                }
            }
        }
       return find(file,count);
    }
    private  File find (File file,long count){
        for (File child : file.listFiles()){
            if (child.lastModified() == count){
                return child;
            }
        }
        return null;
        };
    private void shareFile(File file) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
//        sb.append("/BackUpINoteIos_");
//        sb.append(System.currentTimeMillis());
//        sb.append(".json");

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType("application/json");
       Uri uri =  FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider", file);

        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                uri);

        //if you need
        //intentShareFile.putExtra(Intent.EXTRA_SUBJECT,"Sharing File Subject);
        //intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File Description");

        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }

    @Override
    public void onFinish() {
        file = findFile();
        tv2.setText("" + file.getPath());
        tv1.setText("" + file.getName());

    }
}