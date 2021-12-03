package com.example.notepad;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.constants.IConst;
import com.example.model.MyColor;
import com.example.util.ILog;

//Вызывать из фрагмента, ParentFragment должен реализовывать интерфейс Callback
public class ColorDialogFragment extends DialogFragment implements IConst, ILog {

    private TextView tvColor1;
    private TextView tvColor2;
    private TextView tvColor3;
    private TextView tvColor4;
    private TextView tvColor5;
    private TextView tvColor6;
    private TextView tvColor7;
    private TextView tvColor8;
    private TextView tvColor9;
    private TextView tvColor10;
    private TextView tvColor11;
    private TextView tvColor12;
    private TextView tvColor13;
    private TextView tvColor14;
    private TextView tvColor15;
    private TextView tvColor16;

    private CallbackColorDialog callbackColorDialog;

    public ColorDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        printLog("RadioButtonDialogFragment - onCreateDialog");
        try {
            callbackColorDialog = (CallbackColorDialog) getParentFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement Callback interface");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment_color, null);

        initViews(view);

        TextView[] textViews= {
                tvColor1,
                tvColor2,
                tvColor3,
                tvColor4,
                tvColor5,
                tvColor6,
                tvColor7,
                tvColor8,
                tvColor9,
                tvColor10,
                tvColor11,
                tvColor12,
                tvColor13,
                tvColor14,
                tvColor15,
                tvColor16,
                        };

        for (int i = 0; i < textViews.length; i++) {
            TextView tv = textViews[i];
            MyColor color = MyColor.get(i);
            tv.setTag(color);
            Drawable background = getResources().getDrawable(R.drawable.shape_tv_color);
            background.setColorFilter(new PorterDuffColorFilter(color.getCode(), PorterDuff.Mode.MULTIPLY));
            tv.setBackground(background);
            tv.setOnClickListener(this::clickColor);
        }

        return builder
                .setView(view)
                .setCancelable(false)
                .setNegativeButton("Cancel", this::clickCancel)
                .create();
    }

    private void initViews(View view) {
        tvColor1 = view.findViewById(R.id.tvColor1);
        tvColor2 = view.findViewById(R.id.tvColor2);
        tvColor3 = view.findViewById(R.id.tvColor3);
        tvColor4 = view.findViewById(R.id.tvColor4);
        tvColor5 = view.findViewById(R.id.tvColor5);
        tvColor6 = view.findViewById(R.id.tvColor6);
        tvColor7 = view.findViewById(R.id.tvColor7);
        tvColor8 = view.findViewById(R.id.tvColor8);
        tvColor9 = view.findViewById(R.id.tvColor9);
        tvColor10 = view.findViewById(R.id.tvColor10);
        tvColor11 = view.findViewById(R.id.tvColor11);
        tvColor12 = view.findViewById(R.id.tvColor12);
        tvColor13 = view.findViewById(R.id.tvColor13);
        tvColor14 = view.findViewById(R.id.tvColor14);
        tvColor15 = view.findViewById(R.id.tvColor15);
        tvColor16 = view.findViewById(R.id.tvColor16);
    }

    private void clickColor(View view) {
        MyColor color = (MyColor) view.getTag();
        callbackColorDialog.resultColorDialog(color);
        dismiss();
    }

    private void clickCancel(DialogInterface dialogInterface, int i) {

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public interface CallbackColorDialog {
        void resultColorDialog(MyColor color);
    }


}
