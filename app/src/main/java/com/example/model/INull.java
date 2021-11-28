package com.example.model;

public interface INull {
    String EMPTY_STRING = "";

    boolean isNull();

    default String getValueOrEmptyStringIfNull(String value) {
        return (value == null) ? EMPTY_STRING : value;
    }


}
