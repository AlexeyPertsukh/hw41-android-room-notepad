package com.example.model;

import java.io.Serializable;

public enum Sort implements Serializable {
    EDIT_OLD("ORDER BY dt ASC", "Edit date: old first"),
    EDIT_NEW("ORDER BY dt DESC", "Edit date: new first"),
    ORDER_OLD("ORDER BY id ASC", "Create order: old first"),
    ORDER_NEW("ORDER BY id DESC","Create order: new first"),
    TITLE_LO("ORDER BY title ASC", "Title: A - Z"),
    TITLE_HI("ORDER BY title DESC", "Title: Z - A");

    private final String query;
    private final String description;

    Sort(String query, String description) {
        this.query = query;
        this.description = description;
    }

    public String getQuery() {
        return query;
    }

    public String getDescription() {
        return description;
    }
}
