package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.sqlite.db.SimpleSQLiteQuery;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.constants.IConst;
import com.example.model.App;
import com.example.model.Filter;
import com.example.model.MySharedPreferences;
import com.example.model.Note;
import com.example.model.NoteDao;
import com.example.model.NotepadDb;
import com.example.model.Sort;
import com.example.util.IBasicDialog;
import com.example.util.ILog;
import com.example.util.IToast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements IToast, ILog, IBasicDialog, IGeneralMenu, IConst, IChangeFragment
        , IInfo, IDao, RadioButtonDialogFragment.CallbackRadioButtonDialog {

    private static final String SORT_DIALOG_TITLE = "SORT";
    private static final String FILTER_DIALOG_TITLE = "FILTER";

    private static final Sort DEFAULT_SORT = Sort.EDIT_OLD;
    private static final Filter DEFAULT_FILTER = Filter.NONE;

    private NoteDao noteDao;
    private NotesFragment notesFragment;
    private NoteDetailFragment noteDetailFragment;

    private MySharedPreferences mySharedPreferences;
    private Sort sort;
    private Filter filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printLog("MainActivity - onCreate " + this.toString());
//        changeStatusBarColor();

        NotepadDb notepadDb = App.getInstance().getNotepadDb();
        noteDao = notepadDb.noteDao();
        initViews();
        initFragments();
        initSharedPreferences();
        readSettingsFromShared();

        if (savedInstanceState == null) {
            showNotesFragment();
        }
    }

    private void changeStatusBarColor() {
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));
    }

    private void initViews() {
    }

    private void initFragments() {
        notesFragment = new NotesFragment();
        noteDetailFragment = new NoteDetailFragment();
//        filterDialog = new RadioButtonDialogFragment();
//        filterDialog.setOnCheckListener(this::checkRadioButtonFilter);
//        sortDialog = new RadioButtonDialogFragment();
//        sortDialog.setOnCheckListener(this::checkRadioButtonSort);
    }

    private void initSharedPreferences() {
        mySharedPreferences = MySharedPreferences.getInstance(getApplicationContext());
    }

    private void readSettingsFromShared() {
        sort = mySharedPreferences.getSort(KEY_SHARED_SORT, DEFAULT_SORT);
        filter = mySharedPreferences.getFilter(KEY_SHARED_FILTER, DEFAULT_FILTER);
    }

    @Override
    public void showNotesFragment() {
        ArrayList<Note> notes = readNotes(filter, sort);
        Bundle args = new Bundle();
        args.putParcelableArrayList(KEY_NOTES, notes);
        notesFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, notesFragment)
                .commit();
    }

    @Override
    public void showNoteDetailFragment(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ONE_NOTE, note);
        noteDetailFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fcMain, noteDetailFragment)
                .commit();
    }

    @Override
    public void showSortFragment() {
        final String title = SORT_DIALOG_TITLE;
        final int numCheck = sort.ordinal();
        final Sort[] enums = Sort.values();

        final String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].getDescription();
        }

        final RadioButtonDialogFragment dialog = RadioButtonDialogFragment.getInstance(title, title, names, numCheck);
        dialog.show(getSupportFragmentManager(), title);
    }

    @Override
    public void showFilterDialog() {
        final String title = FILTER_DIALOG_TITLE;
        final int numCheck = filter.ordinal();
        final Filter[] enums = Filter.values();

        final String[] names = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            names[i] = enums[i].getDescription();
        }

        final RadioButtonDialogFragment dialog = RadioButtonDialogFragment.getInstance(title, title, names, numCheck);
        dialog.show(getSupportFragmentManager(), title);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_back) {
            back();
        } else if (id == R.id.menu_git) {
            gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }


    private void showEndDialog() {
        String message = "Do you want to exit the program?";
        String title = "Exit";
        showBasicDialog(this, title, message, (v, d) -> finish());
    }

    @Override
    public void gitOpen() {
        Intent Browse = new Intent(Intent.ACTION_VIEW, Uri.parse(GIT_URL));
        startActivity(Browse);
    }

    @Override
    public void back() {
        if (notesFragment.isVisible()) {
            showEndDialog();
        } else {
            showNotesFragment();
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
            outState.putString(KEY_RECOVERY, "recovery");
    }

    public void setSort(Sort sort) {
        this.sort = sort;
        mySharedPreferences.putSort(KEY_SHARED_SORT, sort);
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
        mySharedPreferences.putFilter(KEY_SHARED_FILTER, filter);
    }

    @Override
    public boolean isFiltered() {
        return filter != Filter.NONE;
    }

    private ArrayList<Note> readNotes(Filter filter, Sort sort) {
        String query = String.format("SELECT * FROM note %s %s", filter.getQuery(), sort.getQuery());
        printLog(query);

        SimpleSQLiteQuery simpleSQLiteQuery = new SimpleSQLiteQuery(query);
        return new ArrayList<>(noteDao.get(simpleSQLiteQuery));
    }

    @Override
    public NoteDao getNoteDao() {
        return noteDao;
    }



    @Override
    public void resultRadioButtonDialog(String type, int num) {
        if(type.equalsIgnoreCase(SORT_DIALOG_TITLE)) {
            checkRadioButtonSort(num);
        } else if(type.equalsIgnoreCase(FILTER_DIALOG_TITLE)) {
            checkRadioButtonFilter(num);
        }
    }

    private void checkRadioButtonSort(int numEnum) {
        Sort sort = Sort.values()[numEnum];
        setSort(sort);
        updateNotes();
    }

    private void checkRadioButtonFilter(int numEnum) {
        Filter filter = Filter.values()[numEnum];
        setFilter(filter);
        updateNotes();
    }

    private void updateNotes() {
        ArrayList<Note> notes = readNotes(filter, sort);
        NotesFragment fragment = (NotesFragment) getSupportFragmentManager().findFragmentById(R.id.fcMain);
        fragment.updateNotes(notes);
    }


}