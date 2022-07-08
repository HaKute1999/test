package com.example.inote.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inote.models.Note;

import java.util.List;
@Dao
public interface NotesDao {
    @Insert
    public void insert(Note... items);
    @Update
    public void update(Note... items);
    @Delete
    public void delete(Note item);
    @Query("SELECT * FROM note")
    public List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE id = :id")
    public Note getItemNoteById(Long id);

}
