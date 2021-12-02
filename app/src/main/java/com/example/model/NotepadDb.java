package com.example.model;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.model.Note;
import com.example.model.NoteDao;

@Database(entities = {Note.class},version = 2)
public abstract class NotepadDb extends RoomDatabase {
    public abstract NoteDao noteDao();

}
