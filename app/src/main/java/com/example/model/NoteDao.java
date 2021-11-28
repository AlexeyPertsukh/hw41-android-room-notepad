package com.example.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.model.Note;

import java.io.Serializable;
import java.util.List;

@Dao
public interface NoteDao extends Serializable {

    @Query("SELECT * FROM note ORDER BY dt")
    List<Note> getAll();

    @Query("SELECT * FROM note WHERE dt > date('now','-1 month') ORDER BY dt")
    List<Note> getMonth();

    @Query("SELECT * FROM note WHERE dt > date('now','-7 day') ORDER BY dt")
    List<Note> getWeek();

    @Query("SELECT * FROM note WHERE date(dt) = date('now') ORDER BY dt")
    List<Note> getToday();

    @Query("SELECT * FROM note ORDER BY :querySort")
    List<Note> get(String querySort);


    @Query("SELECT * FROM note WHERE id = :id")
    Note getById(long id);

    @Insert
    void add(Note... note);

    @Update
    void update(Note... note);

    @Delete
    void remove(Note... note);

}
