package com.example.inote.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.inote.R;
import com.example.inote.dao.FolderDao;
import com.example.inote.dao.Note1Dao;
import com.example.inote.dao.NotesDao;
import com.example.inote.dao.RecentDao;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;
import com.example.inote.models.Note1;
import com.example.inote.models.Recent;
import com.example.inote.view.Converters;

import java.io.File;

@Database(entities = {Note.class, Folder.class, Recent.class, Note1.class}, version = 2)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public static AppDatabase noteDB;
    private static final String DB_NAME = "notes.db";

    public abstract NotesDao getNoteDAO();
    public abstract RecentDao getRecentDao();

    public abstract FolderDao getFolderDAO();
    public abstract Note1Dao getNote1Dao();

    public static AppDatabase getInstance(Context context, String dbName) {
        if (null == noteDB) {
            noteDB = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
            noteDB.getOpenHelper().getWritableDatabase();
        }
        return noteDB;
    }

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        String path = dbFile.getAbsolutePath();
        if (dbFile.getAbsoluteFile().exists()) {
            return true;
        } else return false;
    }

    /* Will close DB if it is open */

}
