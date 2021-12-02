package com.example.model;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences{
    private static final String SHARED_DATA = "shared_data";
    private static MySharedPreferences mySharedPreferences;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_DATA, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static MySharedPreferences getInstance(Context context) {
        if(mySharedPreferences == null) {
            mySharedPreferences = new MySharedPreferences(context);
        }
        return mySharedPreferences;
    }

    public String getString(String key, String defaultResult) {
        return sharedPreferences.getString(key, defaultResult);
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultResult) {
        return sharedPreferences.getInt(key, defaultResult);
    }

    public void putSort(String key, Sort sort) {
        String name = sort.name();
        putString(key, name);
    }

    public Sort getSort(String key, Sort defaultSort) {
        String name = getString(key, defaultSort.name());
        return Sort.valueOf(name);
    }

    public void putFilter(String key, Filter filter) {
        String name = filter.name();
        putString(key, name);
    }

    public Filter getFilter(String key, Filter defaultFilter) {
        String name = getString(key, defaultFilter.name());
        return Filter.valueOf(name);
    }

}
