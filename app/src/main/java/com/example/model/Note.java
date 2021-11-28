package com.example.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Note implements Serializable, INull {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String memo;
    private String dt;

    public Note() {
    }

    @Ignore
    public Note(String title, String memo, String dt) {
        this.title = title;
        this.memo = memo;
        this.dt = dt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public static NoteNull getInstanceNull() {
        return NoteNull.getInstance();
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
