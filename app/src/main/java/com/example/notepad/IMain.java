package com.example.notepad;

import com.example.model.Filter;
import com.example.model.Note;
import com.example.model.NoteDao;
import com.example.model.Sort;

import java.util.ArrayList;

public interface IMain {
    NoteDao getNoteDao();

    void setSort(Sort sort);

    Sort getSort();

    void setFilter(Filter filter);

    Filter getFilter();

    ArrayList<Note> readNotes(Filter filter, Sort sort);

    boolean isFiltered();
}
