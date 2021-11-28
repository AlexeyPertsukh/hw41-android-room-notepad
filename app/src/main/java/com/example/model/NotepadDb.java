package com.example.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.model.Note;
import com.example.model.NoteDao;

@Database(entities = {Note.class},version = 1)
public abstract class NotepadDb extends RoomDatabase {
    public abstract NoteDao noteDao();

}
