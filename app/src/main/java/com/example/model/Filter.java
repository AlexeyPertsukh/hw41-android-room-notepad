package com.example.model;

import java.io.Serializable;

public enum Filter implements Serializable {
    NONE("dt > '1900-01-01'"),
    MONTH("dt > date('now','-1 month')"),
    WEEK("dt > date('now','-7 day')"),
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
