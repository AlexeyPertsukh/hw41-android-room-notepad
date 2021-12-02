package com.example.model;

import com.example.constants.IColor;

public class NoteNull extends Note implements IColor{

    private static NoteNull instance;

    private NoteNull() {
    }

    protected static NoteNull getInstance() {
        if(instance == null) {
            instance = new NoteNull();
        }
        return instance;
    }

    @Override
    public boolean isNull() {
        return true;
    }

    @Override
    public String getTitle() {
        return EMPTY_STRING;
    }

    @Override
    public String getMemo() {
        return EMPTY_STRING;
    }

    @Override
    public String getDt() {
        return EMPTY_STRING;
    }

    @Override
    public int getColor() {
        return COLOR_NONE;
    }
}
