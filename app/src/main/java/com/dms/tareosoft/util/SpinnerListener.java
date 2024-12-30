package com.dms.tareosoft.util;

import android.view.View;
import android.widget.AdapterView;

public interface SpinnerListener extends AdapterView.OnItemSelectedListener {
    @Override
    default void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        onSelected(position);
    }

    @Override
    default void onNothingSelected(AdapterView<?> parent) {

    }

    void onSelected(int pos);
}