package com.example.notepad;

import com.example.model.Filter;

public interface IFilter {
    void setFilter(Filter filter);

    boolean isFiltered();
}
