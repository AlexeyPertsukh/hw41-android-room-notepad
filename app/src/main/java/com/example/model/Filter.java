package com.example.model;

import java.io.Serializable;

public enum Filter implements Serializable {
    NONE("", "None"),
    MONTH("WHERE dt > date('now','-1 month')", "Month"),
    WEEK("WHERE dt > date('now','-7 day')","Week"),
    TODAY("WHERE date(dt) = date('now')","Today"),
    ;

    private final String query;
    private final String description;

    Filter(String query, String description) {
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
