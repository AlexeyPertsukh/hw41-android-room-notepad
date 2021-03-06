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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.constants.IConst;
import com.example.model.MyColor;
import com.example.model.MySharedPreferences;
import com.example.model.Note;
import com.example.model.NoteDao;
import com.example.util.DataTimeUtil;
import com.example.util.IBasicDialog;
import com.example.util.IToast;

import java.io.Serializable;


public class NoteDetailFragment extends Fragment implements IConst, IToast, IBasicDialog, ColorDialogFragment.CallbackColorDialog {

    private static final String COLOR_DIALOG_FRAGMENT_TAG = "color_dialog_tag";


    private EditText etTitle;
    private EditText etMemo;
    private TextView tvDateTime;
    private LinearLayout llNoteDetail;

    private Note inputNote;
    private NoteDao dao;

    private int colorCode;

    private IGeneralMenu iGeneralMenu;
    private IChangeFragment iChangeFragment;
    private IMain iMain;

    public NoteDetailFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_detail, container, false);
        initViews(view);
        initListeners();
        dao = iMain.getNoteDao();

        if(savedInstanceState != null) {
            loadFromBundle(savedInstanceState);
        } else if(getArguments() != null) {
            loadFromArguments();
        }

        setHasOptionsMenu(true);
        return view;
    }

    private void initViews(View view) {
        etTitle = view.findViewById(R.id.etTitle);
        etMemo = view.findViewById(R.id.etMemo);
        tvDateTime = view.findViewById(R.id.tvDateTime);
        llNoteDetail = view.findViewById(R.id.llNoteDetail);
    }

    private void initListeners() {
        
    }

    private void loadFromBundle(Bundle bundle) {
        inputNote = bundle.getParcelable(KEY_ONE_NOTE);
        Note editNote = bundle.getParcelable(KEY_NOTE_FROM_VIEW);
        Select selectTitle = (Select) bundle.getSerializable(KEY_SELECT_TITLE);
        Select selectMemo = (Select) bundle.getSerializable(KEY_SELECT_MEMO);
        addNoteToView(editNote);
        setSelectInEt(etTitle, selectTitle);
        setSelectInEt(etMemo, selectMemo);
        this.colorCode = bundle.getInt(KEY_COLOR);
        setColorCode(colorCode);

        if(selectTitle.isEdit() || selectMemo.isEdit()) {
            openKeyboard();
        }
    }

    private void loadFromArguments() {
        inputNote = getArguments().getParcelable(KEY_ONE_NOTE);
        addNoteToView(inputNote);

        int colorCode = inputNote.getColor();
        if( colorCode == CODE_COLOR_NONE) {
            setColorCode(readColorCodeFromShared());
        } else {
            setColorCode(colorCode);
        }
    }

    private void addNoteToView(Note note) {
        etTitle.setText(note.getTitle());
        etMemo.setText(note.getMemo());
        tvDateTime.setText(note.getDt());
    }

    private void updateInputNoteFromView() {
        inputNote.setTitle(etTitle.getText().toString());
        inputNote.setMemo(etMemo.getText().toString());
        inputNote.setColor(colorCode);
        String updateDt = DataTimeUtil.getStringCurrentDateTime();
        inputNote.setDt(updateDt);
    }

    private Note createNoteFromView() {
        String title = etTitle.getText().toString();
        String memo = etMemo.getText().toString();
        String dt = tvDateTime.getText().toString();
        return new Note(title, memo, dt, colorCode);
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
        } else if(id == R.id.menu_color) {
            showColorDialog();
        }  else if(id == R.id.menu_git) {
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
        saveColorCodeToShared();
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
        outState.putParcelable(KEY_ONE_NOTE, inputNote);
        outState.putParcelable(KEY_NOTE_FROM_VIEW, createNoteFromView());

        Select selectTitle = new Select(etTitle.getSelectionStart(), etTitle.getSelectionEnd());
        Select selectMemo = new Select(etMemo.getSelectionStart(), etMemo.getSelectionEnd());

        outState.putSerializable(KEY_SELECT_TITLE, selectTitle);
        outState.putSerializable(KEY_SELECT_MEMO, selectMemo);
        outState.putSerializable(KEY_SELECT_MEMO, selectMemo);
        outState.putInt(KEY_COLOR, colorCode);
    }

    private static class Select implements Serializable {
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

    public void showColorDialog() {
        final ColorDialogFragment dialog = new ColorDialogFragment();
        dialog.show(getChildFragmentManager(), COLOR_DIALOG_FRAGMENT_TAG);
    }


    private void setColorCode(int colorCode) {
        this.colorCode = colorCode;
        llNoteDetail.setBackgroundColor(colorCode);
    }

    private int readColorCodeFromShared() {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(getContext());
        return mySharedPreferences.getInt(KEY_SHARED_COLOR_CODE, COLOR_DEFAULT.getCode());
    }

    private void saveColorCodeToShared() {
        MySharedPreferences mySharedPreferences = MySharedPreferences.getInstance(getContext());
        mySharedPreferences.putInt(KEY_SHARED_COLOR_CODE, colorCode);
    }


    @Override
    public void resultColorDialog(MyColor color) {
        setColorCode(color.getCode());
    }



}