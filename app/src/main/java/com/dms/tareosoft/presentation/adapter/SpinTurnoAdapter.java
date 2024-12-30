package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dms.tareosoft.data.entities.Turno;

import java.util.List;

public class SpinTurnoAdapter extends ArrayAdapter<Turno> {

    private Context context;
    private List<Turno> list;

    public SpinTurnoAdapter(@NonNull Context context, int resource, @NonNull List<Turno> list) {
        super(context, resource, list);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Turno getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.GRAY);
        label.setText(list.get(position).getDescripcion());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.GRAY);
        label.setText(list.get(position).getDescripcion());

        return label;
    }
}
