package com.example.inote.ui;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.inote.R;
import com.example.inote.adapter.RecentAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.models.Recent;
import com.example.inote.view.ConfigUtils;
import com.example.inote.view.IUpdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecentActivity extends BaseActivity  implements IUpdate {
    List<Recent> recentList;
    RecentAdapter recentAdapter;
    RecyclerView rv_note_delete;
    TextView tvNoteSize, tvDeleteAll;
    EditText edit_result;
    ImageView close_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();

        setContentView(R.layout.activity_delete);
        onBack();
        rv_note_delete = findViewById(R.id.rv_note_delete);
        tvNoteSize = findViewById(R.id.tvNoteSize);
        edit_result = findViewById(R.id.edit_result);
        close_search = findViewById(R.id.close_search);
        tvDeleteAll = findViewById(R.id.tvDeleteAll);
        tvNoteSize.setText(AppDatabase.noteDB.getRecentDao().getAllRecents().size()+" "+ getString(R.string.notes));
        setRecentList();
        ConfigUtils.getConFigDark1(getApplicationContext(),ids(R.id.tvNote),ids(R.id.tvNoteSize),ids(R.id.tvMain),ids(R.id.edit_result),ids(R.id.ll_note),ids(R.id.viewX),ids(R.id.search_ree));
        ConfigUtils.darkBlack(ids(R.id.homeNote));
        ConfigUtils.darkBlack(ids(R.id.rl_top));
        ConfigUtils.darkBlack(ids(R.id.bottom));
        close_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_result.clearFocus();
                edit_result.setText("");
                ConfigUtils.hideKeyboard(RecentActivity.this);
                close_search.setVisibility(View.GONE);
                setRecentList();

            }
        });
        edit_result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                close_search.setVisibility(View.VISIBLE);
                recentAdapter.setFilter(filter(charSequence.toString(), recentList));
//                tvEmptyNote.setVisibility(filter(charSequence.toString(), re).size() == 0 ? View.VISIBLE : View.GONE);
//                if (filter(charSequence.toString(), recentList).size() > 0) {
//                    pinHeader.setVisibility(View.VISIBLE);
//                    expansionLayout.setVisibility(View.VISIBLE);
//                } else {
//                    pinHeader.setVisibility(View.GONE);
//                    expansionLayout.setVisibility(View.GONE);
//                }
//                tvNoteSize.setText((filte(charSequence.toString(),noteList).size()+ filte(charSequence.toString(),noteListPin).size()) +" "+getString(R.string.notes));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tvDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createdelete();
            }
        });
    }
    private ArrayList<Recent> filter(String charSequence, List<Recent> notes) {
        String newText = charSequence.toLowerCase();
        ArrayList<Recent> newList = new ArrayList<>();
        for (Recent note : notes) {
            String channelName = note.getTitle().toLowerCase();
            if (channelName.contains(newText)) {
                newList.add(note);
            }
        }
        return newList;
    }
   private void setRecentList(){
       recentList = AppDatabase.noteDB.getRecentDao().getAllRecents();
       if (recentList.size() > 0){
           tvDeleteAll.setVisibility(View.VISIBLE);
       }else tvDeleteAll.setVisibility(View.GONE);
       tvNoteSize.setText(AppDatabase.noteDB.getRecentDao().getAllRecents().size()+ " "+ getString(R.string.notes));

       recentAdapter = new RecentAdapter(getApplicationContext(),recentList,this);
       rv_note_delete.setLayoutManager(new LinearLayoutManager(this));
       rv_note_delete.setAdapter(recentAdapter);
   }
    @Override
    public void onFinish() {
        setRecentList();
        tvNoteSize.setText(AppDatabase.noteDB.getRecentDao().getAllRecents().size()+ " "+ getString(R.string.notes));


    }
    private void createdelete() {
        final Dialog dialog = new Dialog(this, androidx.appcompat.R.style.Theme_AppCompat_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_delete);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView tvA_cancel = dialog.findViewById(R.id.tvA_cancel);
        TextView tv_rename = dialog.findViewById(R.id.tv_rename);
        LinearLayout dialog_show = dialog.findViewById(R.id.dialog_show);
        String str = getResources().getString(R.string.are_you_sure_delete);
        tv_rename.setText(str);
        tvA_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        TextView tv_ok = dialog.findViewById(R.id.tv_ok);
        ConfigUtils.getConFigDark(dialog.getContext(),tv_rename,tvA_cancel,tv_ok,dialog_show);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppDatabase.noteDB.getRecentDao().deleteAllRecent();
                setRecentList();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}