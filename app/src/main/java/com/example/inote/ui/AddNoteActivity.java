package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.view.ConfigUtils;

public class AddNoteActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener {
    RelativeLayout ivMore;
    RelativeLayout menuChooserContainer;
    View viewBackground;
    EditText edtTitle,text_note_view;
    int idNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ivMore = findViewById(R.id.ivMore);
        menuChooserContainer = findViewById(R.id.menuChooserContainer);
        viewBackground = findViewById(R.id.viewBackground);
        edtTitle = findViewById(R.id.edtTitle);
        text_note_view = findViewById(R.id.text_note_view);
        Intent intent = getIntent();
        idNote = intent.getIntExtra("idNote",0);
        if (idNote !=0){
            edtTitle.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getTitle());
            text_note_view.setText(AppDatabase.noteDB.getNoteDAO().getItemNote(idNote).getValue());
        }
        findViewById(R.id.tvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        ivMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                viewBackground.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
                YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        menuChooserContainer.setVisibility(View.VISIBLE);
                    }
                }).playOn(findViewById(R.id.menuChooserContainer));
            }
        });

        viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(AddNoteActivity.this);
                viewBackground.setVisibility(View.GONE);

                YoYo.with(Techniques.SlideOutDown).duration(500L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        menuChooserContainer.setVisibility(View.GONE);
                    }
                }).playOn(findViewById(R.id.menuChooserContainer));

            }
        });
    }

    @Override
    public void onBackPressed() {
        AppDatabase.noteDB.getNoteDAO().updateItem(edtTitle.getText().toString(),text_note_view.getText().toString(),idNote);
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

    @Override
    public void onClick(View view) {

    }
}