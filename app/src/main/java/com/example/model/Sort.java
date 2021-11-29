package com.example.model;

import java.io.Serializable;

public enum Sort implements Serializable {
    EDIT_OLD("ORDER BY dt ASC"),
    EDIT_NEW("ORDER BY dt DESC"),
    ORDER_OLD("ORDER BY id ASC"),
    ORDER_NEW("ORDER BY id DESC"),
    TITLE_LO("ORDER BY title ASC"),
    TITLE_HI("ORDER BY title DESC");

    private final String query;

    Sort(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
