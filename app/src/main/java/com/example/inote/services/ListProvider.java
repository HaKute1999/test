package com.example.inote.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.ui.NoteWidget;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static final List<Note> items=new ArrayList<>();
    private Context ctxt=null;
    private int appWidgetId;

    public ListProvider(Context ctxt, Intent intent) {
        this.ctxt=ctxt;
        appWidgetId=intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        items.clear();
        items.addAll(AppDatabase.noteDB.getNoteDAO().getAllNotes());
    }

    @Override
    public void onCreate() {
        // no-op
    }

    @Override
    public void onDestroy() {
        // no-op
    }

    @Override
    public int getCount() {
        return(items.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row=new RemoteViews(ctxt.getPackageName(),
                R.layout.item_note_widget);

        row.setTextViewText(R.id.tvTitleNote, items.get(position).getTitle());
        row.setTextViewText(R.id.tvValueNote, items.get(position).getValue());
        if (items.get(position).getListImage().size() > 0) {
            Bitmap bitmap = null;
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                    bitmap =  ImageDecoder.decodeBitmap(ImageDecoder.createSource(ctxt.getContentResolver(), Uri.fromFile(new File(items.get(position).getListImage().get(0)))));

            } else {
                    bitmap =  MediaStore.Images.Media.getBitmap(ctxt.getContentResolver(), Uri.fromFile(new File(items.get(position).getListImage().get(0))));
            }
            } catch (IOException e) {
            e.printStackTrace();
        }
            row.setImageViewBitmap(R.id.image_note2,bitmap);


        }

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(NoteWidget.EXTRA_WORD, items.get(position).getTitle());
        i.putExtras(extras);
        row.setOnClickFillInIntent(android.R.id.text1, i);

        return(row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return(null);
    }

    @Override
    public int getViewTypeCount() {
        return(1);
    }

    @Override
    public long getItemId(int position) {
        return(position);
    }

    @Override
    public boolean hasStableIds() {
        return(true);
    }

    @Override
    public void onDataSetChanged() {
        // no-op
    }
}