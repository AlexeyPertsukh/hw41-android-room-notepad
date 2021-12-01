package com.example.notepad;

import android.annotation.SuppressLint;
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
import com.example.model.Filter;
import com.example.model.Note;
import com.example.model.NoteAdapter;
import com.example.model.Sort;
import com.example.util.ILog;
import com.example.util.IToast;

import java.util.ArrayList;


public class NotesFragment extends Fragment implements IConst, IToast, ILog, RadioButtonDialogFragment.CallbackRadioButtonDialog {
    private static final String SORT_DIALOG_TITLE = "SORT";
    private static final String FILTER_DIALOG_TITLE = "FILTER";

    private RecyclerView rvNotes;
    private NoteAdapter noteAdapter;
    private ArrayList<Note> notes;

    private MenuItem miFilter;

    private IGeneralMenu iGeneralMenu;
    private IChangeFragment iChangeFragment;
    private IMain iMain;


    public NotesFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iGeneralMenu = (IGeneralMenu) context;
        iChangeFragment = (IChangeFragment) context;
        iMain = (IMain) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printLog("NotesFragment - onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        printLog("NotesFragment - onCreateView");

        if (getArguments() != null) {
            readArguments();
        }

        initViews(view);
        initRvUsers();
        initAdapter();
        initListeners();

        setHasOptionsMenu(true);
        return view;
    }

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
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_NOTES, notes);
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
        if (iMain.isFiltered()) {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_green);
        } else {
            miFilter.setIcon(R.drawable.ic_baseline_filter_list_64_white);
        }
    }

    private void filter() {
        showFilterDialog();
    }

    private void sort() {
        showSortDialog();
    }

    private void addNote() {
        Note note =  Note.getInstanceNull();
        iChangeFragment.showNoteDetailFragment(note);
    }



    @Override
    public void resultRadioButtonDialog(String type, int num) {
        if (type.equalsIgnoreCase(SORT_DIALOG_TITLE)) {
            checkRadioButtonSort(num);
        } else if (type.equalsIgnoreCase(FILTER_DIALOG_TITLE)) {
            checkRadioButtonFilter(num);
        }
    }

    public void showSortDialog() {
        final String title = SORT_DIALOG_TITLE;
        final int numCheck = iMain.getSort().ordinal();
        final Sort[] enums = Sort.values();

        final String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].getDescription();
        }

        final RadioButtonDialogFragment dialog = RadioButtonDialogFragment.getInstance(title, title, names, numCheck);
        dialog.show(getChildFragmentManager(), title);
    }

    public void showFilterDialog() {
        final String title = FILTER_DIALOG_TITLE;
        final int numCheck = iMain.getFilter().ordinal();
        final Filter[] enums = Filter.values();

        final String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].getDescription();
        }

        final RadioButtonDialogFragment dialog = RadioButtonDialogFragment.getInstance(title, title, names, numCheck);
        dialog.show(getChildFragmentManager(), title);
    }

    private void checkRadioButtonSort(int numEnum) {
        Sort sort = Sort.values()[numEnum];
        iMain.setSort(sort);
        updateNotes();
    }

    private void checkRadioButtonFilter(int numEnum) {
        Filter filter = Filter.values()[numEnum];
        iMain.setFilter(filter);
        updateNotes();
    }

    private void updateNotes() {
        Filter filter = iMain.getFilter();
        Sort sort = iMain.getSort();
        ArrayList<Note> notes = iMain.readNotes(filter, sort);
        this.notes.clear();
        this.notes.addAll(notes);
        noteAdapter.notifyDataSetChanged();
        changeFilterIcon();
    }


}