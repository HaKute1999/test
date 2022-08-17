package com.example.inote.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.inote.models.Note;
import com.example.inote.models.Recent;
import java.util.List;
@Dao
public interface RecentDao {
    @Insert
    public void insert(Recent... items);

    @Query("DELETE FROM recent WHERE id = :id")
    public void deleteItemRecent(int id);

    @Query("SELECT * FROM recent")
    public List<Recent> getAllRecents();

    @Query("SELECT * FROM recent WHERE idFolder = :idFolder")
    public List<Recent> getAllRecentFolder(int idFolder);

    @Query("DELETE FROM recent WHERE idFolder = :idFolder")
    public void deleteAllRecentByFolder(int idFolder);

    @Query("SELECT * FROM recent WHERE id = :id")
    public Recent getItemRecent(int id);

    @Query("DELETE FROM recent")
    public void deleteAllRecent();
}
