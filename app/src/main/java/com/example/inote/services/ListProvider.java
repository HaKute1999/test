package com.example.inote.services;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.ui.NoteWidget;
import com.example.inote.view.ConfigUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private static List<Note> items=new ArrayList<>();
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
        row.setTextViewText(R.id.tvValueNote, items.get(position).getValue().get(0));
        if (items.get(position).getListImage().size() > 0) {
            Bitmap bitmap = null;
            if (items.get(position).getListImage().get(0).contains("storage")){
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

                        bitmap =  ImageDecoder.decodeBitmap(ImageDecoder.createSource(ctxt.getContentResolver(), Uri.fromFile(new File(items.get(position).getListImage().get(0)))));

                    } else {
                        bitmap =  MediaStore.Images.Media.getBitmap(ctxt.getContentResolver(), Uri.fromFile(new File(items.get(position).getListImage().get(0))));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                bitmap = ConfigUtils.convertBase64toImage1(items.get(position).getListImage().get(0));

            }


            row.setImageViewBitmap(R.id.image_note2,bitmap);


        }

        Intent i=new Intent();
        Bundle extras=new Bundle();

        extras.putString(NoteWidget.EXTRA_WORD, String.valueOf(items.get(position).getId()));
        i.putExtras(extras);
        row.setOnClickFillInIntent(R.id.root_note, i);

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
        items = AppDatabase.noteDB.getNoteDAO().getAllNotes();
        Log.d("cdcdccd", items.size() +"");
        // no-op
    }
}