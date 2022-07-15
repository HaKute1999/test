package com.example.inote.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inote.models.Folder;
import com.example.inote.models.Note;

import java.util.List;

@Dao
public interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Folder... items);
    @Query("UPDATE folder SET title=:name WHERE id = :id")
    public void update(int id,String name);
    @Query("DELETE FROM folder WHERE id = :id")
    public void delete(int id);
    @Query("SELECT * FROM folder")
    public List<Folder> getAllFolder();
    @Query("SELECT COUNT(id) FROM folder")
    int getRowCount();
    @Query("SELECT * FROM folder WHERE id = :id")
    public Folder getItemFolder(int id);

}
