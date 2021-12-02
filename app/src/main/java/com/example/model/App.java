package com.example.model;


import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                "Notepad").allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public NotepadDb getNotepadDb() {
        return notepadDb;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            final int COLOR_WHITE = 16777215;
            @SuppressLint("DefaultLocale")
            String query = String.format("ALTER TABLE note ADD COLUMN color INT NOT NULL DEFAULT %d", COLOR_WHITE);
            database.execSQL(query);
        }
    };
}
