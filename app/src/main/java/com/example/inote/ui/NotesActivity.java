package com.example.inote.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.inote.R;
import com.example.inote.adapter.NoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;
import com.example.inote.view.ShareUtils;
import com.github.florent37.expansionpanel.ExpansionHeader;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class NotesActivity extends BaseActivity implements IUpdate {
    NoteAdapter noteAdapter, noteAdapterPin;
    List<Note> noteList, noteListPin;
    RecyclerView rlNote, rv_note_pin;
    ImageView ivCreateNote, close_search;
    TextView tvMain, tvNoteSize, tvEmptyNote, tv_note,tvSortAZ,tvSortZA,tvSortDate;
    ExpansionHeader pinHeader;
    ExpansionLayout expansionLayout;
    EditText edit_result;
    int idFolder;
    String type;
    LinearLayout ll_note;
    LinearLayout ll_menu;
    RelativeLayout homeChooserContainer,homeNote;
    View viewBackgroundHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_notes);
        rlNote = findViewById(R.id.rv_note);
        tv_note = findViewById(R.id.tvNote);
        tvSortAZ = findViewById(R.id.tvSortAZ);
        tvSortZA = findViewById(R.id.tvSortZA);
        tvSortDate = findViewById(R.id.tvSortDate);
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
        homeNote = findViewById(R.id.homeNote);
        ll_menu = findViewById(R.id.ll_menu);
        viewBackgroundHome = findViewById(R.id.viewBackgroundHome);
        homeChooserContainer = findViewById(R.id.homeChooserContainer);
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
                noteAdapter.setFilter(filter(charSequence.toString(), noteList));
                noteAdapterPin.setFilter(filter(charSequence.toString(), noteListPin));
                tvEmptyNote.setVisibility(filter(charSequence.toString(), noteList).size() == 0 ? View.VISIBLE : View.GONE);
                if (filter(charSequence.toString(), noteListPin).size() > 0) {
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
        ll_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigUtils.hideKeyboard(NotesActivity.this);
                viewBackgroundHome.setVisibility(View.VISIBLE);
//                YoYo.with(Techniques.SlideInUp).duration(100L).playOn(viewBackground);
                YoYo.with(Techniques.SlideInDown).duration(200L).onEnd(new YoYo.AnimatorCallback() {
                    @Override
                    public final void call(Animator animator) {
                        homeChooserContainer.setVisibility(View.VISIBLE);
                    }
                }).playOn(findViewById(R.id.menu_config_note));
            }
        });
        viewBackgroundHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBackgroundHome.setVisibility(View.GONE);
                homeChooserContainer.setVisibility(View.GONE);
            }
        });
        tvSortAZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList = AppDatabase.noteDB.getNoteDAO().getNoteSortByAscLastName();
                viewBackgroundHome.setVisibility(View.GONE);
                homeChooserContainer.setVisibility(View.GONE);
                AppDatabase.noteDB.getNoteDAO().update();
                noteAdapter = new NoteAdapter(getApplicationContext(), noteList, NotesActivity.this);
                rlNote.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

                rlNote.setAdapter(noteAdapter);

            }
        });
        tvSortZA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList = AppDatabase.noteDB.getNoteDAO().getNoteSortByDescLastName();
                viewBackgroundHome.setVisibility(View.GONE);
                homeChooserContainer.setVisibility(View.GONE);
                AppDatabase.noteDB.getNoteDAO().update();
                noteAdapter = new NoteAdapter(getApplicationContext(), noteList, NotesActivity.this);
                rlNote.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

                rlNote.setAdapter(noteAdapter);

            }
        });
        tvSortDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteList = AppDatabase.noteDB.getNoteDAO().getNoteSortByDescTime();
                viewBackgroundHome.setVisibility(View.GONE);
                homeChooserContainer.setVisibility(View.GONE);
                AppDatabase.noteDB.getNoteDAO().update();
                noteAdapter = new NoteAdapter(getApplicationContext(), noteList, NotesActivity.this);
                rlNote.setLayoutManager(new LinearLayoutManager(NotesActivity.this));

                rlNote.setAdapter(noteAdapter);

            }
        });
        ConfigUtils.getConFigDark(getApplicationContext(),ids(R.id.tvMain),ids(R.id.tvNote),ids(R.id.tvNoteSize),ids(R.id.tvPinHome),ids(R.id.tvEmptyNote),ids(R.id.tvNoteShow),ids(R.id.ll_note),
                ids(R.id.ln_pin),ids(R.id.viewX),ids(R.id.search_ree),ids(R.id.tvSortAZ),ids(R.id.tvSortZA),ids(R.id.tvSortDate),ids(R.id.tvCancelHome),ids(R.id.ln_more),ids(R.id.viewhome1),ids(R.id.viewhome2));
        ConfigUtils.darkTextViewRadius(ids(R.id.tvCancelHome));
        ConfigUtils.darkBlack(ids(R.id.rl_top));
        ConfigUtils.darkBlack(ids(R.id.bottom));
    }

    private ArrayList<Note> filter(String charSequence, List<Note> notes) {
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
        if (ShareUtils.getBool(ShareUtils.CONFIG_DARK) ==true){
            homeNote.setBackgroundColor(Color.BLACK);
            edit_result.setTextColor(Color.WHITE);
        }else{
            homeNote.setBackgroundColor(getResources().getColor(R.color.color_main));
            edit_result.setTextColor(Color.BLACK);

        }
        Intent intent2 = new Intent(this, NoteWidget.class);
        intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());

        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(getApplication(), NoteWidget.class));
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent2);
        ;
        super.onResume();

    }

    @Override
    public void onFinish() {
        initListNote();
        Intent intent2 = new Intent(this, NoteWidget.class);
        intent2.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplication());

        int[] ids = appWidgetManager
                .getAppWidgetIds(new ComponentName(getApplication(), NoteWidget.class));
        intent2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent2);

    }
}