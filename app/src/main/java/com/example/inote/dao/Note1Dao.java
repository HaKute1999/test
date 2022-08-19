package com.example.inote.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inote.models.Note;
import com.example.inote.models.Note1;

import java.util.List;

@Dao
public interface Note1Dao {
    @Insert
    public void insert(Note1... items);

    @Update
    public void update(Note1... items);

    @Delete
    public void delete(Note1 item);

    @Query("SELECT * FROM note1 ORDER BY timeEdit DESC")
    public List<Note1> getAllNotes();
}
