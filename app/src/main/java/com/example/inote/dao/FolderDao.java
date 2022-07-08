package com.example.inote.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inote.models.Folder;

import java.util.List;

@Dao
public interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Folder... items);
    @Update
    public void update(Folder... items);
    @Delete
    public void delete(Folder item);
    @Query("SELECT * FROM folder")
    public List<Folder> getAllFolder();
}
