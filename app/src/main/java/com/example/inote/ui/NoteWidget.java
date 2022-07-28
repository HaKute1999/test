package com.example.inote.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inote.R;
import com.example.inote.adapter.NoteAdapter;
import com.example.inote.database.AppDatabase;
import com.example.inote.models.Note;
import com.example.inote.services.WidgetService;
import com.example.inote.view.IUpdate;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class NoteWidget extends AppWidgetProvider {
    public static String EXTRA_WORD=
            "com.commonsware.android.appwidget.lorem.WORD";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.note_widget);
        // Instruct the widget manager to update the widget
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(
                svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter( R.id.list,
                svcIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
//            Intent svcIntent=new Intent(context, WidgetService.class);
//
//            svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
//            svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
//
//            RemoteViews widget=new RemoteViews(context.getPackageName(),
//                    R.layout.note_widget);
//
//            widget.setRemoteAdapter(appWidgetId, R.id.list,
//                    svcIntent);

//            Intent clickIntent=new Intent(context, MainActivity.class);
//            PendingIntent clickPI=PendingIntent
//                    .getActivity(context, 0,
//                            clickIntent,
//                            PendingIntent.FLAG_UPDATE_CURRENT);
//
//            widget.setPendingIntentTemplate(R.id., clickPI);

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}