package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.adapter.RecentAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Recent;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;

import java.util.List;

public class DeleteActivity extends BaseActivity  implements IUpdate {
    List<Recent> recentList;
    RecentAdapter recentAdapter;
    RecyclerView rv_note_delete;
    TextView tvNoteSize;
    EditText edit_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        setContentView(R.layout.activity_delete);
        onBack();
        rv_note_delete = findViewById(R.id.rv_note_delete);
        tvNoteSize = findViewById(R.id.tvNoteSize);
        edit_result = findViewById(R.id.edit_result);
        tvNoteSize.setText(AppDatabase.noteDB.getRecentDao().getAllRecents().size()+ getString(R.string.notes));
        setRecentList();
        ConfigUtils.getConFigDark1(getApplicationContext(),ids(R.id.tvNote),ids(R.id.tvNoteSize),ids(R.id.tvMain),ids(R.id.edit_result),ids(R.id.ll_note),ids(R.id.viewX),ids(R.id.search_ree));
        ConfigUtils.darkBlack(ids(R.id.homeNote));
        ConfigUtils.darkBlack(ids(R.id.rl_top));
        ConfigUtils.darkBlack(ids(R.id.bottom));
    }
   private void setRecentList(){
       recentList = AppDatabase.noteDB.getRecentDao().getAllRecents();
       recentAdapter = new RecentAdapter(getApplicationContext(),recentList,this);
       rv_note_delete.setLayoutManager(new LinearLayoutManager(this));
       rv_note_delete.setAdapter(recentAdapter);
   }
    @Override
    public void onFinish() {
        setRecentList();
        tvNoteSize.setText(AppDatabase.noteDB.getRecentDao().getAllRecents().size()+ getString(R.string.notes));


    }
}