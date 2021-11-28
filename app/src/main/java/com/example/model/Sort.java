package com.example.model;

import java.io.Serializable;

public enum Sort implements Serializable {
    EDIT_OLD("dt ASC"),
    EDIT_NEW("dt DESC"),
    ORDER_OLD("id ASC"),
    ORDER_NEW("id DESC"),
    TITLE_LO("title ASC"),
    TITLE_HI("title DESC");

    private final String query;

    Sort(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
