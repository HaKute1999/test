package com.example.inote.services;

import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.inote.R;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;

import java.util.ArrayList;
import java.util.List;

public class ListProvider implements RemoteViewsService.RemoteViewsFactory {
    private List<Note> listItemList;
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
            listItemList.addAll(AppDatabase.noteDB.getNoteDAO().getAllNotes());
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
         RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.item_note);
        Note listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.tvTitleNote, listItem.getTitle());
        remoteView.setTextViewText(R.id.tvValueNote, listItem.getValue());
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }
}
