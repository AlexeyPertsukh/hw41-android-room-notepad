package com.example.model;

import java.io.Serializable;

public enum Filter implements Serializable {
    NONE(""),
    MONTH("WHERE dt > date('now','-1 month')"),
    WEEK("WHERE dt > date('now','-7 day')"),
    TODAY("WHERE date(dt) = date('now')"),
    ;

    private final String query;

    Filter(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }
}
