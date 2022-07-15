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

//    @Query("SELECT * FROM note WHERE idFolder = :idFolder")
//    public Note getItemNoteById(int idFolder);

    @Query("SELECT * FROM note WHERE idFolder = :idFolder")
    public List<Note> getAllNoteFolder(int idFolder);
    @Query("DELETE FROM note WHERE idFolder = :idFolder")
    public void deleteAllNoteByFolder(int idFolder);
}
