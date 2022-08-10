package com.example.inote.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.inote.models.CheckItem;
import com.example.inote.models.Note;
import com.example.inote.models.NoteStyle;

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

    @Query("SELECT * FROM note WHERE idFolder = :idFolder")
    public List<Note> getAllNoteFolder(int idFolder);

    @Query("DELETE FROM note WHERE idFolder = :idFolder")
    public void deleteAllNoteByFolder(int idFolder);

    @Query("SELECT * FROM note WHERE id = :id")
    public Note getItemNote(int id);

    @Query("UPDATE note SET title=:title, value =:value WHERE id = :id")
    void updateItem(String title, List<String> value, int id);

    @Query("UPDATE note SET protectionType=:protectionType WHERE id = :id")
    void updateprotectionType(int protectionType, int id);

    @Query("UPDATE note SET isPinned=:pin WHERE id = :id")
    void updatePinned(boolean pin, int id);

    @Query("DELETE FROM note WHERE id = :id")
    public void deleteItemNote(int id);

    @Query("SELECT * FROM note WHERE isPinned = :ispin AND idFolder = :idFolder")
    public List<Note> getNotePin(boolean ispin, int idFolder);

    @Query("SELECT * FROM note WHERE isPinned = :ispin")
    public List<Note> getAllNotePin(boolean ispin);

    @Query("UPDATE note SET idFolder=:idFolder WHERE id = :id")
    void updateFolder(int idFolder, int id);

    @Query("UPDATE note SET listImage=:listPath WHERE id = :id")
    void updateListImage(List<String> listPath, int id);

    @Query("UPDATE note SET valueChecklist=:valueChecklist WHERE id = :id")
    void updateListCheckList(List<CheckItem> valueChecklist, int id);

    @Query("SELECT * FROM note ORDER BY LOWER(title) ASC")
    List<Note> getNoteSortByAscLastName();
    @Query("SELECT * FROM note ORDER BY LOWER(title) DESC")
    List<Note> getNoteSortByDescLastName();
    @Query("SELECT * FROM note ORDER BY timeEdit DESC")
    List<Note> getNoteSortByDescTime();

    @Query("SELECT * FROM note WHERE noteStyle = :noteStyle AND id = :id")
    public List<Note> getNotePin(NoteStyle noteStyle, int id);

    @Query("UPDATE note SET noteStyle=:noteStyle WHERE id = :id")
    void updateListImage(NoteStyle noteStyle, int id);
}
