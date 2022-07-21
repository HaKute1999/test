package com.example.inote.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.inote.R;
import com.example.inote.adapter.FolderAdapter;
import com.example.inote.adapter.ImageNoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;
import com.example.inote.view.ShareUtils;
import com.example.inote.view.drawingview.ICopy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNoteActivity extends BaseActivity implements TextWatcher, View.OnClickListener, ICopy {
    RelativeLayout ivMore;
    RelativeLayout menuChooserContainer,layoutLock,imageChooserContainer;
    View viewBackground;
    EditText edtTitle,text_note_view,text_note_view2,text_note_view3;
    TextView tvTime,tvViewNote,tvChoosePhoto,tvTakePhoto,tvDone;
    int idNote;
    int idFolder;
    ImageView ivDraw,ivPhoto,ivCreate,ivChecklist;
    RecyclerView rv_image,checklist_list;
    ImageNoteAdapter imageNoteAdapter;
    List<String> listImage ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initView();
        Intent intent = getIntent();
        idNote = intent.getIntExtra("idNote",0);
        idFolder = intent.getIntExtra("idFolder",0);

        if (idNote !=0){
            if (AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getProtectionType() == 1){
                layoutLock.setVisibility(View.VISIBLE);
            }
            edtTitle.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTitle());
            text_note_view.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue());
            tvTime.setText(ConfigUtils.formatDateTIme(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTimeEdit()));
        }else {
            tvTime.setVisibility(View.GONE);
        }
        findViewById(R.id.tvBack).setOnClickListener(view -> onBackPressed());
        ivMore.setOnClickListener(view -> {
            ConfigUtils.hideKeyboard(AddNoteActivity.this);
            viewBackground.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
            YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public final void call(Animator animator) {
                    menuChooserContainer.setVisibility(View.VISIBLE);
                }
            }).playOn(findViewById(R.id.menuChooserContainer));
        });

        viewBackground.setOnClickListener(view -> {
            ConfigUtils.hideKeyboard(AddNoteActivity.this);
            viewBackground.setVisibility(View.GONE);
            imageChooserContainer.setVisibility(View.GONE);

            YoYo.with(Techniques.SlideOutDown).duration(300L).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public final void call(Animator animator) {
                    menuChooserContainer.setVisibility(View.GONE);
                }
            }).playOn(findViewById(R.id.menuChooserContainer));


        });
        tvViewNote.setOnClickListener(view -> {
            showDialogConfirmDialog();
        });
        ivDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(AddNoteActivity.this,DrawNoteActivity.class);
                intent1.putExtra("idNote",idNote);
                startActivity(intent1);
            }
        });
        ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                viewBackground.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
                YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        imageChooserContainer.setVisibility(View.VISIBLE);
                    }
                }).playOn(findViewById(R.id.imageChooserContainer));
            }
        });
        ivChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChecklist();
            }
        });
        tvChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (permission(AddNoteActivity.this)){
                   viewBackground.setVisibility(View.GONE);
                   imageChooserContainer.setVisibility(View.GONE);
                   Intent intent = new Intent(Intent.ACTION_PICK,
                           android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                   startActivityForResult(Intent.createChooser(intent, "select Image"),
                           PICK_IMAGE);
               };

            }
        });
        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (permission(AddNoteActivity.this)){
                    viewBackground.setVisibility(View.GONE);
                    imageChooserContainer.setVisibility(View.GONE);
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }

            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }
    static final int REQUEST_IMAGE_CAPTURE = 2;

    private void dialogChecklist() {
        final Dialog dialog = new Dialog(AddNoteActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_new_checklist_item);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
        tvA_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });


        dialog.show();
    }
    private void initView() {
        ivMore = findViewById(R.id.ivMore);
        ivPhoto = findViewById(R.id.ivPhoto);
        ivCreate = findViewById(R.id.ivCreate);
        ivChecklist = findViewById(R.id.ivChecklist);
        menuChooserContainer = findViewById(R.id.menuChooserContainer);
        imageChooserContainer = findViewById(R.id.imageChooserContainer);
        layoutLock = findViewById(R.id.layoutLock);
        viewBackground = findViewById(R.id.viewBackground);
        edtTitle = findViewById(R.id.edtTitle);
        text_note_view = findViewById(R.id.text_note_view);
        text_note_view2 = findViewById(R.id.text_note_view2);
        text_note_view3 = findViewById(R.id.text_note_view3);
        tvViewNote = findViewById(R.id.tvViewNote);
        tvTime = findViewById(R.id.tvTime);
        ivDraw = findViewById(R.id.ivDraw);
        tvChoosePhoto = findViewById(R.id.tvChoosePhoto);
        tvTakePhoto = findViewById(R.id.tvTakePhoto);
        checklist_list = findViewById(R.id.checklist_list);
        rv_image = findViewById(R.id.rv_image);
        tvDone = findViewById(R.id.tvDone);
        listImage  =new ArrayList<>();

    }

    public static final int PICK_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri selectedImageUri = null;
        if (requestCode == PICK_IMAGE  ) {
             if (data != null){
                 selectedImageUri = data.getData();

                 String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                 File imageFile = new File(selectedImagePath);

                 String des = ConfigUtils.copyFile(getApplicationContext(),imageFile,this);
             }

        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE){
            if (data.getExtras() != null){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                selectedImageUri =  ConfigUtils.getImageUri(getApplicationContext(),imageBitmap);
                String selectedImagePath = getRealPathFromURIForGallery(selectedImageUri);
                File imageFile = new File(selectedImagePath);
                String des = ConfigUtils.copyFile(getApplicationContext(),imageFile,this);
            }

        }

    }




    public String getRealPathFromURIForGallery(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    private void setupListImage(List<String> listImage){
        listImage = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getListImage();
        if (listImage.size() ==0){
            text_note_view3.setVisibility(View.GONE);
        }else {
            text_note_view3.setVisibility(View.VISIBLE);
        }
        rv_image.setLayoutManager(new GridLayoutManager(this,2));
        imageNoteAdapter = new ImageNoteAdapter(this,listImage,this);
        rv_image.setAdapter(imageNoteAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (idNote !=0){
            setupListImage(listImage);
        }else {
            rv_image.setLayoutManager(new GridLayoutManager(this,2));
            imageNoteAdapter = new ImageNoteAdapter(this,ConfigUtils.listImageCache,this);
            rv_image.setAdapter(imageNoteAdapter);

        }
    }

    @Override
    public void onFinish(List<String> data) {
        if (idNote !=0){
            AppDatabase.noteDB.getNoteDAO().updateListImage(data,idNote);
            setupListImage(listImage);
        }else {
            ConfigUtils.listImageCache.clear();
            ConfigUtils.listImageCache.addAll(data);
            rv_image.setLayoutManager(new GridLayoutManager(this,2));
            imageNoteAdapter = new ImageNoteAdapter(this,ConfigUtils.listImageCache,this);
            rv_image.setAdapter(imageNoteAdapter);

        }
    }

    @Override
    public void onProgress(String path) {
        if (idNote != 0){
            List<String> listImage = AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getListImage();
            listImage.add(path);
            AppDatabase.noteDB.getNoteDAO().updateListImage(listImage,idNote);
        }else {
            ConfigUtils.listImageCache.add(path);
        }

    }

    @Override
    public void onBackPressed() {
        if (idNote !=0){
            AppDatabase.noteDB.getNoteDAO().updateItem(edtTitle.getText().toString(),text_note_view.getText().toString(),idNote);
        }else if(edtTitle.getText().toString().length() != 0|| text_note_view.getText().toString().length() !=0){
            AppDatabase.noteDB.getNoteDAO().insert(new Note(idFolder,false,ConfigUtils.listImageCache,null,null,null,null,0,System.currentTimeMillis(),
                    edtTitle.getText().toString(),0,text_note_view.getText().toString(),""));
            ConfigUtils.listImageCache.clear();
        }else {
            ConfigUtils.listImageCache.clear();
        }

        super.onBackPressed();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    private void showDialogConfirmDialog() {

        final Dialog dialog = new Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_pass);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        EditText tv_rename = dialog.findViewById(R.id.edtFolder2);
        TextView tvCancelFolder2 = dialog.findViewById(R.id.tvCancelFolder2);

        tvCancelFolder2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_delete_note = dialog.findViewById(R.id.tvOkFolder2);
        tv_delete_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_rename.getText().toString().contains(ShareUtils.getStr(ShareUtils.PASSCODE,""))){
                    layoutLock.setVisibility(View.GONE);
                    dialog.dismiss();

                }else {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.pass_not_connect), Toast.LENGTH_LONG).show();

                }
            }
        });

        dialog.show();

    }
    @Override
    public void onClick(View view) {

    }
}