package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.adapter.NoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;

import java.util.List;

public class NotesActivity extends BaseActivity {
    NoteAdapter noteAdapter;
    List<Note> noteList;
    RecyclerView rlNote;
    ImageView ivCreateNote ;
    TextView tvMain,tvNoteSize,tvEmptyNote ;
    int idFolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        rlNote = findViewById(R.id.rv_note);
        tvMain = findViewById(R.id.tvMain);
        tvNoteSize = findViewById(R.id.tvNoteSize);
        ivCreateNote = findViewById(R.id.ivCreateNote);
        tvEmptyNote = findViewById(R.id.tvEmptyNote);
        onBack();
        Intent intent = getIntent();
         idFolder = intent.getIntExtra("idFolder",0);

        if (idFolder !=0){
            tvMain.setText(AppDatabase.noteDB.getFolderDAO().getItemFolder(idFolder).getTitle()+"");
            tvNoteSize.setText(AppDatabase.noteDB.getNoteDAO().getAllNoteFolder(idFolder).size()+ " "+getString(R.string.notes));

        }else {
            tvNoteSize.setText(AppDatabase.noteDB.getNoteDAO().getAllNotes().size()+ " "+getString(R.string.notes));

        }

        ivCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        initListNote();
    }

    private void initListNote() {
        if (idFolder != 0){
            noteList = AppDatabase.noteDB.getNoteDAO().getAllNoteFolder(idFolder);
        }else {
            noteList = AppDatabase.noteDB.getNoteDAO().getAllNotes();
        }
        tvEmptyNote.setVisibility(noteList.size() ==0 ?View.VISIBLE  : View.GONE);
        noteAdapter = new NoteAdapter(getApplicationContext(),noteList);
        rlNote.setLayoutManager(new LinearLayoutManager(this));

        rlNote.setAdapter(noteAdapter);

    }

    @Override
    protected void onResume() {
        initListNote();

        super.onResume();

    }
}