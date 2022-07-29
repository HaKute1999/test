package com.example.inote.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.inote.R;
import com.example.inote.adapter.RecentAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Recent;
import com.example.inote.view.IUpdate;

import java.util.List;

public class DeleteActivity extends BaseActivity  implements IUpdate {
    List<Recent> recentList;
    RecentAdapter recentAdapter;
    RecyclerView rv_note_delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        onBack();
        rv_note_delete = findViewById(R.id.rv_note_delete);
        setRecentList();
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

    }
}