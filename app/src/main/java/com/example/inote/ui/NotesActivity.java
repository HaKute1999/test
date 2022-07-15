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
    private static final String DB_NAME = "notes.db";
    NoteAdapter noteAdapter;
    List<Note> noteList;
    RecyclerView rlNote;
    AppDatabase noteDb;
    ImageView ivCreateNote ;
    TextView tvMain,tvNoteSize ;
    int idFolder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        rlNote = findViewById(R.id.rv_note);
        tvMain = findViewById(R.id.tvMain);
        tvNoteSize = findViewById(R.id.tvNoteSize);
        ivCreateNote = findViewById(R.id.ivCreateNote);
        onBack();
        Intent intent = getIntent();
         idFolder = intent.getIntExtra("idFolder",0);

        noteDb = AppDatabase.getInstance(this,DB_NAME);
        if (idFolder !=0){
            tvMain.setText(noteDb.getFolderDAO().getItemFolder(idFolder).getTitle()+"");
        }
        tvNoteSize.setText(noteDb.getNoteDAO().getAllNotes().size()+ " "+getString(R.string.notes));
        if (noteDb.getNoteDAO().getAllNotes().size() == 0){
            String string = getResources().getString(R.string.thank_you);
            Note note = new Note(0, true, null, null, null, null, "yuyty", 0, System.currentTimeMillis(), string, 0, getString(R.string.thanks_all_app) + "\n\n" +getString(R.string.find_all_app) + " \n\nDefault Note", null);
            noteDb.getNoteDAO().insert(note);
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
            noteList = noteDb.getNoteDAO().getAllNoteFolder(idFolder);
        }else {
            noteList = noteDb.getNoteDAO().getAllNotes();

        }
        noteAdapter = new NoteAdapter(getApplicationContext(),noteList);
        rlNote.setLayoutManager(new LinearLayoutManager(this));

        rlNote.setAdapter(noteAdapter);

    }


}