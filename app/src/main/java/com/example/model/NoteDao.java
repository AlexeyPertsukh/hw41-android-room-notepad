package com.example.model;

import static android.text.TextUtils.TruncateAt.END;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.model.Note;

import java.io.Serializable;
import java.util.List;

@Dao
public interface NoteDao {

    @RawQuery
    List<Note> get(SupportSQLiteQuery query);

    @Query("SELECT * FROM note WHERE id = :id")
    Note getById(long id);

    @Insert
    void add(Note... note);

    @Update
    void update(Note... note);

    @Delete
    void remove(Note... note);

}
