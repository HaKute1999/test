package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.ShareUtils;

public class AddNoteActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    RelativeLayout ivMore;
    RelativeLayout menuChooserContainer,layoutLock;
    View viewBackground;
    EditText edtTitle,text_note_view;
    TextView tvTime,tvViewNote;
    int idNote;
    int idFolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ivMore = findViewById(R.id.ivMore);
        menuChooserContainer = findViewById(R.id.menuChooserContainer);
        layoutLock = findViewById(R.id.layoutLock);
        viewBackground = findViewById(R.id.viewBackground);
        edtTitle = findViewById(R.id.edtTitle);
        text_note_view = findViewById(R.id.text_note_view);
        tvViewNote = findViewById(R.id.tvViewNote);
        tvTime = findViewById(R.id.tvTime);
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

            YoYo.with(Techniques.SlideOutDown).duration(500L).onEnd(new YoYo.AnimatorCallback() {
                @Override
                public final void call(Animator animator) {
                    menuChooserContainer.setVisibility(View.GONE);
                }
            }).playOn(findViewById(R.id.menuChooserContainer));

        });
        tvViewNote.setOnClickListener(view -> {
            showDialogConfirmDialog();
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("cdjcncd",edtTitle.getText().toString());
        if (idNote !=0){
            AppDatabase.noteDB.getNoteDAO().updateItem(edtTitle.getText().toString(),text_note_view.getText().toString(),idNote);
        }else if(edtTitle.getText().toString().length() != 0|| text_note_view.getText().toString().length() !=0){
            AppDatabase.noteDB.getNoteDAO().insert(new Note(idFolder,false,null,null,null,null,null,0,System.currentTimeMillis(),
                    edtTitle.getText().toString(),0,text_note_view.getText().toString(),""));
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