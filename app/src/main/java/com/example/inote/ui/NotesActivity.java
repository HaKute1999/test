package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.adapter.NoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.util.ArrayList;
import java.util.List;

public class NotesActivity extends BaseActivity implements IUpdate {
    NoteAdapter noteAdapter, noteAdapterPin;
    List<Note> noteList, noteListPin;
    RecyclerView rlNote, rv_note_pin;
    ImageView ivCreateNote, close_search;
    TextView tvMain, tvNoteSize, tvEmptyNote, tv_note;
    ExpansionHeader pinHeader;
    ExpansionLayout expansionLayout;
    EditText edit_result;
    int idFolder;
    String type;
    LinearLayout ll_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        rlNote = findViewById(R.id.rv_note);
        tv_note = findViewById(R.id.tvNote);
        ll_note = findViewById(R.id.ll_note);
        pinHeader = findViewById(R.id.pinHeader);
        expansionLayout = findViewById(R.id.pinExpansion);
        rv_note_pin = findViewById(R.id.rv_note_pin);
        tvMain = findViewById(R.id.tvMain);
        tvNoteSize = findViewById(R.id.tvNoteSize);
        ivCreateNote = findViewById(R.id.ivCreateNote);
        tvEmptyNote = findViewById(R.id.tvEmptyNote);
        edit_result = findViewById(R.id.edit_result);
        close_search = findViewById(R.id.close_search);
        onBack();
        Intent intent = getIntent();
        idFolder = intent.getIntExtra("idFolder", 0);
        type = intent.getStringExtra("rl_go_pin");
        if (type != null && type.contains("pin")) {
            tv_note.setVisibility(View.GONE);
            ll_note.setVisibility(View.GONE);
            pinHeader.setVisibility(View.VISIBLE);
            expansionLayout.setVisibility(View.VISIBLE);
            tvMain.setText(getString(R.string.pinned));
//             tvNoteSize.setText(AppDatabase.noteDB.getNoteDAO().getAllNotePin(true).size()+ " "+getString(R.string.notes));

        }
        ivCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NotesActivity.this, AddNoteActivity.class);
                intent1.putExtra("idFolder", idFolder);
                startActivity(intent1);

            }
        });
        edit_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                close_search.setVisibility(View.VISIBLE);
                noteAdapter.setFilter(filte(charSequence.toString(), noteList));
                noteAdapterPin.setFilter(filte(charSequence.toString(), noteListPin));
                tvEmptyNote.setVisibility(filte(charSequence.toString(), noteList).size() == 0 ? View.VISIBLE : View.GONE);
                if (filte(charSequence.toString(), noteListPin).size() > 0) {
                    pinHeader.setVisibility(View.VISIBLE);
                    expansionLayout.setVisibility(View.VISIBLE);
                } else {
                    pinHeader.setVisibility(View.GONE);
                    expansionLayout.setVisibility(View.GONE);
                }
//                tvNoteSize.setText((filte(charSequence.toString(),noteList).size()+ filte(charSequence.toString(),noteListPin).size()) +" "+getString(R.string.notes));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initListNote();

        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_result.clearFocus();
                edit_result.setText("");
                ConfigUtils.hideKeyboard(NotesActivity.this);
                close_search.setVisibility(View.GONE);
                initListNote();

            }
        });
    }

    private ArrayList<Note> filte(String charSequence, List<Note> notes) {
        String newText = charSequence.toLowerCase();
        ArrayList<Note> newList = new ArrayList<>();
        for (Note note : notes) {
            String channelName = note.getTitle().toLowerCase();
            if (channelName.contains(newText)) {
                newList.add(note);
            }
        }
        return newList;
    }

    private void initListNote() {
        if (idFolder != 0) {
            tvMain.setText(AppDatabase.noteDB.getFolderDAO().getItemFolder(idFolder).getTitle() + "");
            tvNoteSize.setText(AppDatabase.noteDB.getNoteDAO().getAllNoteFolder(idFolder).size() + " " + getString(R.string.notes));
            noteList = AppDatabase.noteDB.getNoteDAO().getNotePin(false, idFolder);
            noteListPin = AppDatabase.noteDB.getNoteDAO().getNotePin(true, idFolder);

        } else {
            tvNoteSize.setText(AppDatabase.noteDB.getNoteDAO().getAllNotes().size() + " " + getString(R.string.notes));
            noteList = AppDatabase.noteDB.getNoteDAO().getAllNotePin(false);
            noteListPin = AppDatabase.noteDB.getNoteDAO().getAllNotePin(true);

        }
        if (noteListPin.size() > 0) {
            pinHeader.setVisibility(View.VISIBLE);
            expansionLayout.setVisibility(View.VISIBLE);
        } else {
            pinHeader.setVisibility(View.GONE);
            expansionLayout.setVisibility(View.GONE);
        }
        tvEmptyNote.setVisibility(noteList.size() == 0 ? View.VISIBLE : View.GONE);
        noteAdapter = new NoteAdapter(getApplicationContext(), noteList, this);
        rlNote.setLayoutManager(new LinearLayoutManager(this));

        rlNote.setAdapter(noteAdapter);
        noteAdapterPin = new NoteAdapter(getApplicationContext(), noteListPin, this);
        rv_note_pin.setLayoutManager(new LinearLayoutManager(this));

        rv_note_pin.setAdapter(noteAdapterPin);


    }

    @Override
    protected void onResume() {
        initListNote();

        super.onResume();

    }

    @Override
    public void onFinish() {
        initListNote();

    }
}