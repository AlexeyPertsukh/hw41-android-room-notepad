package com.example.notepad;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.constants.IConst;
import com.example.model.Note;
import com.example.model.NoteAdapter;
import com.example.util.IToast;

import java.util.ArrayList;


public class NotesFragment extends Fragment implements IConst, IToast {

    private RecyclerView rvNotes;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> notes;

    private MenuItem miFilter;

    private IGeneralMenu iGeneralMenu;
    private IChangeFragment iChangeFragment;
    private IFilter iFilter;

    public NotesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iGeneralMenu = (IGeneralMenu) context;
        iChangeFragment = (IChangeFragment) context;
        iFilter = (IFilter) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        if (getArguments() != null) {
            if(getArguments() != null) {
                readArguments();
                initViews(view);
                initRvUsers();
                initAdapter();
                initListeners();
            }
        }

        setHasOptionsMenu(true);
        return view;
    }

    @SuppressWarnings("unchecked")
    private void readArguments() {
        assert getArguments() != null;
        notes = getArguments().getParcelableArrayList(KEY_NOTES);
    }

    private void initViews(View view) {
        rvNotes = view.findViewById(R.id.rvNotes);
    }

    private void initRvUsers() {
        rvNotes.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvNotes.setLayoutManager(layoutManager);
        rvNotes.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
    }

    private void initAdapter() {
        noteAdapter = new NoteAdapter(notes);
        rvNotes.setAdapter(noteAdapter);
    }

    private void initListeners() {
        noteAdapter.setOnClickItem(this::clickNote);
    }

    private void clickNote(Note note) {
        iChangeFragment.showNoteDetailFragment(note);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_notes_fragment, menu);
        miFilter = menu.getItem(1); //Filter, чтобы менять иконку
        changeFilterIcon();
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_add_note) {
            addNote();
        } else if(id == R.id.menu_sort) {
            sort();
        } else if(id == R.id.menu_filter) {
            filter();
        } else if(id == R.id.menu_git) {
            iGeneralMenu.gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeFilterIcon() {
        if (iFilter.isFiltered()) {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_green);
        } else {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_white);
        }
    }

    private void filter() {
        iChangeFragment.showFilterFragment();
    }

    private void sort() {
        iChangeFragment.showSortFragment();
    }

    private void addNote() {
        Note note =  Note.getInstanceNull();
        iChangeFragment.showNoteDetailFragment(note);
    }



}