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
import com.example.model.Filter;
import com.example.util.IToast;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class FilterFragment extends Fragment implements IConst, IToast {

    private RadioButton rbFilterNone;
    private RadioButton rbFilterMonth;
    private RadioButton rbFilterWeek;
    private RadioButton rbFilterToday;

    private Map<RadioButton, Filter> mapRbFilter;
    private Map<Filter, RadioButton> mapIntFilter;

    private IChangeFragment iChangeFragment;
    private IFilter iFilter;

    public FilterFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iChangeFragment = (IChangeFragment) context;
        iFilter = (IFilter) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapRbFilter = new HashMap<>();
        mapIntFilter = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, container, false);
        initViews(view);
        initMaps();
        setRbCheck();
        initListeners();
        return view;
    }

    private void initViews(View view) {
        rbFilterNone = view.findViewById(R.id.rbFilterNone);
        rbFilterMonth = view.findViewById(R.id.rbFilterMonth);
        rbFilterWeek = view.findViewById(R.id.rbFilterWeek);
        rbFilterToday = view.findViewById(R.id.rbFilterToday);
    }

    private void initMaps() {
        mapRbFilter.clear();
        mapRbFilter.put(rbFilterNone, Filter.NONE);
        mapRbFilter.put(rbFilterMonth, Filter.MONTH);
        mapRbFilter.put(rbFilterWeek, Filter.WEEK);
        mapRbFilter.put(rbFilterToday, Filter.TODAY);

        mapIntFilter.clear();
        mapIntFilter.put(Filter.NONE, rbFilterNone);
        mapIntFilter.put(Filter.MONTH, rbFilterMonth);
        mapIntFilter.put(Filter.WEEK, rbFilterWeek);
        mapIntFilter.put(Filter.TODAY, rbFilterToday);
    }

    private void initListeners() {
        rbFilterNone.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterMonth.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterWeek.setOnCheckedChangeListener(this::onRbCheck);
        rbFilterToday.setOnCheckedChangeListener(this::onRbCheck);
    }

    private void setRbCheck() {
        Filter filter = iFilter.getFilter();
        RadioButton rb = mapIntFilter.get(filter);
        assert rb != null;
        rb.setChecked(true);
    }

    private void onRbCheck(CompoundButton compoundButton, boolean b) {
        if (!b) {
            return;
        }
        Filter out = mapRbFilter.get(compoundButton);
        iFilter.setFilter(out);
        iChangeFragment.showNotesFragment();
        String message = "Filter " + compoundButton.getText().toString().toLowerCase(Locale.ROOT);
        shortToast(getContext(), message);
    }

}