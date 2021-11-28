package com.example.model;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

public class App extends Application {
    public static App instance;//null
    private NotepadDb notepadDb;

    @Override
    public void onCreate() {
        Log.d("MyLog","onCreate App");
        super.onCreate();
        instance = this;
        Log.d("MyLog","after!!!");

        notepadDb = Room.databaseBuilder(
                this,
                NotepadDb.class,
                "Notepad").allowMainThreadQueries().build();
    }

    public static App getInstance() {
        return instance;
    }

    public NotepadDb getNotepadDb() {
        return notepadDb;
    }
}
