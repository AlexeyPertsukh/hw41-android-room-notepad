package com.example.notepad;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.constants.IConst;
import com.example.model.Sort;
import com.example.util.IToast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SortFragment extends Fragment implements Serializable, IConst, IToast {

    private RadioButton rbSortOrderNew;
    private RadioButton rbSortOrderOld;
    private RadioButton rbSortEditNew;
    private RadioButton rbSortEditOld;
    private RadioButton rbSortTitleHi;
    private RadioButton rbSortTitleLo;

    private Map<RadioButton, Sort> mapRbSort;
    private Map<Sort, RadioButton> mapSortRb;

    private IChangeFragment iChangeFragment;
    private ISort iSort;

    private Sort sort;

    public SortFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iChangeFragment = (IChangeFragment) context;
        iSort = (ISort) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapRbSort = new HashMap<>();
        mapSortRb = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        initViews(view);
        initMaps();
        if(getArguments() != null) {
            readArguments();
            setRbCheck();
        }
        initListeners();
        return view;
    }

    private void setRbCheck() {
        RadioButton rb = mapSortRb.get(sort);
        assert rb != null;
        rb.setChecked(true);
    }

    private void readArguments() {
        sort = (Sort) getArguments().getSerializable(KEY_SORT);
    }

    private void initMaps() {
        mapRbSort.clear();
        mapRbSort.put(rbSortOrderNew, Sort.ORDER_NEW);
        mapRbSort.put(rbSortOrderOld, Sort.ORDER_OLD);
        mapRbSort.put(rbSortEditNew, Sort.EDIT_NEW);
        mapRbSort.put(rbSortEditOld, Sort.EDIT_OLD);
        mapRbSort.put(rbSortTitleHi, Sort.TITLE_HI);
        mapRbSort.put(rbSortTitleLo, Sort.TITLE_LO);

        mapSortRb.clear();
        mapSortRb.put(Sort.ORDER_NEW, rbSortOrderNew);
        mapSortRb.put(Sort.ORDER_OLD, rbSortOrderOld);
        mapSortRb.put(Sort.EDIT_NEW, rbSortEditNew);
        mapSortRb.put(Sort.EDIT_OLD, rbSortEditOld);
        mapSortRb.put(Sort.TITLE_HI, rbSortTitleHi);
        mapSortRb.put(Sort.TITLE_LO, rbSortTitleLo);
    }

    private void initListeners() {
        rbSortOrderNew.setOnCheckedChangeListener(this::onRbCheck);
        rbSortOrderOld.setOnCheckedChangeListener(this::onRbCheck);
        rbSortEditNew.setOnCheckedChangeListener(this::onRbCheck);
        rbSortEditOld.setOnCheckedChangeListener(this::onRbCheck);
        rbSortTitleHi.setOnCheckedChangeListener(this::onRbCheck);
        rbSortTitleLo.setOnCheckedChangeListener(this::onRbCheck);
    }

    private void onRbCheck(CompoundButton compoundButton, boolean b) {
        if (!b) {
            return;
        }
        sort = mapRbSort.get(compoundButton);
        iSort.setSort(sort);
        iChangeFragment.showNotesFragment();
        String message = "Sort by " + compoundButton.getText().toString().toLowerCase(Locale.ROOT);
        shortToast(getContext(), message);
    }

    private void initViews(View view) {
        rbSortOrderNew = view.findViewById(R.id.rbSortOrderNew);
        rbSortOrderOld = view.findViewById(R.id.rbSortOrderOld);
        rbSortEditNew = view.findViewById(R.id.rbSortEditNew);
        rbSortEditOld = view.findViewById(R.id.rbSortEditOld);
        rbSortTitleHi = view.findViewById(R.id.rbSortTitleHi);
        rbSortTitleLo = view.findViewById(R.id.rbSortTitleLo);
    }
}