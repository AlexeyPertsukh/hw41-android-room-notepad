package com.example.notepad;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.constants.IConst;
import com.example.model.Note;
import com.example.model.NoteDao;
import com.example.util.DataTimeUtil;
import com.example.util.IBasicDialog;
import com.example.util.IToast;

import java.io.Serializable;


public class NoteDetailFragment extends Fragment implements IConst, IToast, IBasicDialog {

    private EditText etTitle;
    private EditText etMemo;
    private TextView tvDateTime;

    private Note inputNote;
    private NoteDao dao;

    private IGeneralMenu iGeneralMenu;
    private IChangeFragment iChangeFragment;


    public NoteDetailFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iGeneralMenu = (IGeneralMenu) context;
        iChangeFragment = (IChangeFragment) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);
        initView(view);
        initListeners();

        if(savedInstanceState != null) {
            loadFromBundle(savedInstanceState);
        } else if(getArguments() != null) {
            loadFromArguments();
        }

        setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        etTitle = view.findViewById(R.id.etTitle);
        etMemo = view.findViewById(R.id.etMemo);
        tvDateTime = view.findViewById(R.id.tvDateTime);
    }

    private void initListeners() {
        
    }

    private void loadFromBundle(Bundle bundle) {
        inputNote = (Note) bundle.getSerializable(KEY_ONE_NOTE);
        dao = (NoteDao) bundle.getSerializable(KEY_DAO);
        Select selectTitle = (Select) bundle.getSerializable(KEY_SELECT_TITLE);
        Select selectMemo = (Select) bundle.getSerializable(KEY_SELECT_MEMO);
        addNoteToView(inputNote);
        setSelectInEt(etTitle, selectTitle);
        setSelectInEt(etMemo, selectMemo);

        if(selectTitle.isEdit() || selectMemo.isEdit()) {
            openKeyboard();
        }

    }

    private void loadFromArguments() {
        inputNote = (Note) getArguments().getSerializable(KEY_ONE_NOTE);
        dao = (NoteDao) getArguments().getSerializable(KEY_DAO);
        addNoteToView(inputNote);
    }

    private void addNoteToView(Note note) {
        etTitle.setText(note.getTitle());
        etMemo.setText(note.getMemo());
        tvDateTime.setText(note.getDt());
    }

    private void updateInputNoteFromView() {
        inputNote.setTitle(etTitle.getText().toString());
        inputNote.setMemo(etMemo.getText().toString());
        String updateDt = DataTimeUtil.getStringCurrentDateTime();
        inputNote.setDt(updateDt);
    }

    private Note createNoteFromView() {
        String title = etTitle.getText().toString();
        String memo = etMemo.getText().toString();
        String dt = tvDateTime.getText().toString();
        return new Note(title, memo, dt);
    }

    private Note createNoteFromViewWithCurrentDt() {
        Note note = createNoteFromView();
        note.setDt(DataTimeUtil.getStringCurrentDateTime());
        return note;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_note_fragment_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_back) {
            iGeneralMenu.back();
        } else if(id == R.id.menu_save_note) {
            saveNote();
        } else if(id == R.id.menu_delete_note) {
            showConfirmDeleteDialog();
        } else if(id == R.id.menu_git) {
            iGeneralMenu.gitOpen();
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote() {
        if (inputNote.isNull()) {
            Note note = createNoteFromViewWithCurrentDt();
            dao.add(note);
        } else {
            updateInputNoteFromView();
            dao.update(inputNote);
        }
        iChangeFragment.showNotesFragment();
    }

    public void showConfirmDeleteDialog() {
        String message = "Do you want to delete the note?";
        String title = "Delete";
        showBasicDialog(getContext(), title, message, this::deleteNote);
    }

    private void deleteNote(DialogInterface dialogInterface, int i) {
        if(!inputNote.isNull()) {
            dao.remove(inputNote);
        }
        iChangeFragment.showNotesFragment();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_DAO, dao);
        outState.putSerializable(KEY_ONE_NOTE, inputNote);
        outState.putSerializable(KEY_NOTE_FROM_VIEW, createNoteFromView());

        Select selectTitle = new Select(etTitle.getSelectionStart(), etTitle.getSelectionEnd());
        Select selectMemo = new Select(etMemo.getSelectionStart(), etMemo.getSelectionEnd());

        outState.putSerializable(KEY_SELECT_TITLE, selectTitle);
        outState.putSerializable(KEY_SELECT_MEMO, selectMemo);
    }

    private class Select implements Serializable {
        public int start;
        public int end;

        public Select(int start, int end) {
            this.start = start;
            this.end = end;
        }

        public boolean isEdit() {
            return start > 0 || end > 0;
        }
    }

    private void setSelectInEt(EditText et, Select select) {
        et.setSelection(select.start, select.end);
    }

    public void openKeyboard() {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}