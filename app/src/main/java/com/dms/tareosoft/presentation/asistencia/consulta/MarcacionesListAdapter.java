package com.dms.tareosoft.presentation.asistencia.consulta;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.dms.tareosoft.R;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarcacionesListAdapter extends RecyclerView.Adapter<MarcacionesListAdapter.ViewHolder> {

    protected List<Marcacion> objList;

    public MarcacionesListAdapter(List<Marcacion> objList) {
        this.objList = objList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_empleado_new_tareo, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Marcacion obj = objList.get(position);

        holder.tvCodigoEmpTareo.setText(obj.getCodigoEmpleado());
        holder.tvNomApeTareo.setText(obj.getNombreEmpleado());
        holder.tvFechaEmpTareo.setText(obj.getFechaMarca() + " " + obj.getHoraMarca());
        if (!TextUtils.isEmpty(obj.getTipo())) {
            if (obj.getTipo().equals("E")) {
                holder.imvTipoIngreso.setImageResource(R.drawable.ic_circle_up_);
            } else {
                holder.imvTipoIngreso.setImageResource(R.drawable.ic_circle_down);
            }
        }

        try{
            if (obj.getSupervisor().toLowerCase(Locale.ROOT).equals("si")) {

                holder.lyEmpleado.setBackgroundResource(R.drawable.round_tareo_superv);
            }
        } catch (Exception e) {
            Log.d("EmpleadoSimpleAdapter", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return objList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_codigoEmpTareo)
        TextView tvCodigoEmpTareo;
        @BindView(R.id.tv_nomApeTareo)
        TextView tvNomApeTareo;
        @BindView(R.id.txt_fecha_ini)
        TextView tvFechaEmpTareo;
        @BindView(R.id.ly_empleado)
        ConstraintLayout lyEmpleado;
        @BindView(R.id.imvTipoIngreso)
        ImageView imvTipoIngreso;
        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public void filterList(ArrayList<Marcacion> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        objList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}


