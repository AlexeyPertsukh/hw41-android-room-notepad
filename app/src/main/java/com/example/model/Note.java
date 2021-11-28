package com.example.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

@Entity
public class Note implements Parcelable, INull {
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

    @Ignore
    public Note(long id, String title, String memo, String dt) {
        this.id = id;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(memo);
        dest.writeString(dt);
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel source) {
            long id = source.readLong();
            String title = source.readString();
            String memo = source.readString();
            String dt = source.readString();
            return new Note(id, title, memo, dt);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };


}
