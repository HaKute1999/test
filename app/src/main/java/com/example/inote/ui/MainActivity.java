package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;
import com.example.inote.view.ShareUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, IUpdate {
    RelativeLayout rl_gomain, rlDeleteNote, rl_gosetting, rl_go_pin,root_final;
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
        setFullScreen();
        setContentView(R.layout.activity_main);
        initView();
        ConfigUtils.listImageCache.clear();
        ConfigUtils.listValueCache.clear();

        new ShareUtils(this);
        if (!ShareUtils.checkExist(ShareUtils.CONFIG_DARK)){
            ShareUtils.setBool(ShareUtils.CONFIG_DARK,false);
        }
        noteDb = AppDatabase.getInstance(this, DB_NAME);
        if (noteDb.getNoteDAO().getAllNotes().size() == 0) {
            String string = getResources().getString(R.string.thank_you);
            ConfigUtils.listValueCache.add(getString(R.string.thanks_all_app) + "\n\n" + getString(R.string.find_all_app) + " \n\nDefault Note");
            ConfigUtils.listValueCache.add("");
            ConfigUtils.listValueCache.add("");
            Note note = new Note(0, false, new ArrayList<>(), 0, System.currentTimeMillis(), string, 0, ConfigUtils.listValueCache, new ArrayList<>());
            noteDb.getNoteDAO().insert(note);
        }
        setupListFolder();


    }

    private void initView() {
        rl_gomain = findViewById(R.id.rl_gomain);
        rl_go_pin = findViewById(R.id.rl_go_pin);
        rlDeleteNote = findViewById(R.id.rlDeleteNote);
        rl_gosetting = findViewById(R.id.rl_go_st);
        ivAddFolder = findViewById(R.id.ivAddFolder);
        ivAddNote = findViewById(R.id.ivAddNote);
        size_list1 = findViewById(R.id.size_list1);
        size_list2 = findViewById(R.id.size_list2);
        sizeDelete = findViewById(R.id.sizeDelete);
        listFolder = findViewById(R.id.listFolder);
        root_final = findViewById(R.id.root_final);
        rl_gomain.setOnClickListener(this);
        rl_go_pin.setOnClickListener(this);
        rl_gosetting.setOnClickListener(this);
        rlDeleteNote.setOnClickListener(this);
        ivAddFolder.setOnClickListener(this);
        ivAddNote.setOnClickListener(this);
        ConfigUtils.listImageCache.clear();

    }

    private void setupListFolder() {
        size_list2.setText(noteDb.getNoteDAO().getAllNotePin(true).size() + "");
        size_list1.setText(noteDb.getNoteDAO().getAllNotes().size() + "");
        sizeDelete.setText(noteDb.getRecentDao().getAllRecents().size()+"");
        listData = new ArrayList<>();
        listData = noteDb.getFolderDAO().getAllFolder();
        listFolder.setLayoutManager(new LinearLayoutManager(this));
        folderAdapter = new FolderAdapter(listData, this);
        listFolder.setAdapter(folderAdapter);
    }

    @Override
    protected void onResume() {
        AppDatabase.doesDatabaseExist(this, DB_NAME);
        setupListFolder();
        ConfigUtils.getConFigDark(getApplicationContext(),ids(R.id.view_main),ids(R.id.tv_rc),ids(R.id.tv_fv_exit),ids(R.id.tvRcDelete),ids(R.id.tv_stt),ids(R.id.ll_exit_menu),ids(R.id.viewExit),ids(R.id.viewExit2),ids(R.id.viewDelete),ids(R.id.viewExit3),ids(R.id.rl_pro2));

        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) ==true){
            root_final.setBackgroundColor(Color.BLACK);
        }else  root_final.setBackgroundColor(getResources().getColor(R.color.color_main));
;
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 102:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                    Intent intent = new Intent(MainActivity.this, MyProjectActivity.class);
////                    startActivity(intent);
//                    Log.e("value", "Permission Granted, .");
//                } else {
//                    Log.e("value", "Permission Denied, .");
//                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.rl_gomain) {
            Intent i = new Intent(MainActivity.this, NotesActivity.class);
            startActivity(i);
        }
        if (id == R.id.rl_go_pin) {
            Intent i = new Intent(MainActivity.this, NotesActivity.class);
            i.putExtra("rl_go_pin", "pin");
            startActivity(i);
        }
        if (id == R.id.ivAddFolder) {
            showDialog(MainActivity.this);
        }
        if (id == R.id.ivAddNote) {
            Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
            startActivity(i);
        }
        if (id == R.id.rlDeleteNote) {
            Intent i = new Intent(MainActivity.this, DeleteActivity.class);
            startActivity(i);
        }
        if (id == R.id.rl_go_st) {
            Intent i = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(i);
        }
    }

    public void showDialog(Activity activity) {
        final Dialog dialog = new Dialog(activity, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_create_folder);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvCancelFolder = dialog.findViewById(R.id.tvCancelFolder);
        TextView tvTitleFolder = dialog.findViewById(R.id.tvTitleFolder);
        TextView tvTitleFolder2 = dialog.findViewById(R.id.tvTitleFolder2);
        RelativeLayout root_dl = dialog.findViewById(R.id.root_dl);
        EditText edtFolder = dialog.findViewById(R.id.edtFolder);

        tvCancelFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tvOkFolder = dialog.findViewById(R.id.tvOkFolder);
        ConfigUtils.getConFigDark(getApplicationContext(),edtFolder,tvTitleFolder,tvTitleFolder2,tvCancelFolder,tvOkFolder,root_dl);

        tvOkFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtFolder.getText() == null || edtFolder.getText().toString() == "") {
                    dialog.dismiss();
                    return;
                } else {
                    noteDb.getFolderDAO().insert(new Folder("", edtFolder.getText().toString()));
                    setupListFolder();

                }
                dialog.dismiss();

            }
        });

        dialog.show();

    }

    @Override
    public void onFinish() {
        setupListFolder();
    }

}