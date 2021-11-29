package com.example.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.constants.IConst;
import com.example.util.ILog;

import java.io.Serializable;

//Activity должен реализовывать интерфейс Callback
public class RadioButtonDialogFragment extends DialogFragment implements IConst, ILog {
    private String[] names;
    private String title;
    private String type;
    private int checkNum;

    private CallbackRadioButtonDialog callbackRadioButtonDialog;

    public RadioButtonDialogFragment() {
    }

    public static RadioButtonDialogFragment getInstance(String type, String title, String[] names, int checkNum) {
        RadioButtonDialogFragment dialog = new RadioButtonDialogFragment();
        Bundle args = new Bundle();
        args.putStringArray(KEY_DIALOG_NAMES, names);
        args.putString(KEY_DIALOG_TITLE, title);
        args.putInt(KEY_DIALOG_CHECK_NUM, checkNum);
        args.putString(KEY_DIALOG_TYPE, type);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbackRadioButtonDialog = (CallbackRadioButtonDialog) context;
        printLog("RadioButtonDialogFragment - onAttach");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        printLog("RadioButtonDialogFragment - onCreateDialog");
        if (getArguments() != null) {
            names = getArguments().getStringArray(KEY_DIALOG_NAMES);
            title = getArguments().getString(KEY_DIALOG_TITLE);
            checkNum = getArguments().getInt(KEY_DIALOG_CHECK_NUM);
            type = getArguments().getString(KEY_DIALOG_TYPE);
        }

//        printLog("OnCheckListener = "+onCheckListener.toString());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_radiobutton, null);
        RadioGroup rg = view.findViewById(R.id.rgDialog);

        TextView tvTitle = view.findViewById(R.id.tvDialogTitle);
        tvTitle.setText(title);

        for (int i = 0; i < names.length; i++) {
            ContextThemeWrapper context = new ContextThemeWrapper(getContext(), R.style.StyleRadioButtonText);
            RadioButton rb = new RadioButton(context);
            rb.setText(names[i]);
            rb.setTag(i);

            if (checkNum == i) {
                rb.setChecked(true);
            }
            rb.setOnCheckedChangeListener(this::checkRadioButton);
            rg.addView(rb);
        }

        return builder
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("Cancel", this::clickCancel)
                .create();
    }

    private void checkRadioButton(CompoundButton compoundButton, boolean b) {
        if (!b) {
            return;
        }

        int num = (Integer)compoundButton.getTag();
        callbackRadioButtonDialog.resultRadioButtonDialog(type, num);
        dismiss();
    }


    private void clickCancel(DialogInterface dialogInterface, int i) {

    }

   @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public interface CallbackRadioButtonDialog {
        void resultRadioButtonDialog(String type, int num);
    }


}
