package com.example.inote.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.inote.dao.FolderDao;
import com.example.inote.dao.NotesDao;
import com.example.inote.models.Folder;
import com.example.inote.models.Note;

import java.io.File;

@Database(entities = {Note.class, Folder.class}, version = 2)
    public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase noteDB;
    private static final String DB_NAME = "notes.db";

    public abstract NotesDao getNoteDAO();
    public abstract FolderDao getFolderDAO();
    public static AppDatabase getInstance(Context context, String dbName) {
        if (null == noteDB) {
           noteDB = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
            noteDB.getOpenHelper().getWritableDatabase();
        }
        return noteDB;
    }
        public static boolean doesDatabaseExist(Context context, String dbName) {
            File dbFile = context.getDatabasePath(dbName);
            String path = dbFile.getAbsolutePath();
            if (dbFile.getAbsoluteFile().exists()){
                return true;
            }else return false;
        }

        /* Will close DB if it is open */

}
