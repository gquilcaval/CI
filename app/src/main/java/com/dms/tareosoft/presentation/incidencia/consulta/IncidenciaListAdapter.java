package com.dms.tareosoft.presentation.incidencia.consulta;


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
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IncidenciaListAdapter extends RecyclerView.Adapter<IncidenciaListAdapter.ViewHolder> {

    protected List<Incidencia> objList;

    public IncidenciaListAdapter(List<Incidencia> objList) {
        this.objList = objList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_incidencia, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Incidencia obj = objList.get(position);

        holder.tvMotivoIncidencia.setText(obj.getVch_Tarea());
        holder.tvMensajeIncidencia.setText(obj.getVCH_MENSAJE());
        holder.tvFechaIncidencia.setText(DateUtil.changeStringDateTimeFormat(obj.getDTM_FECMARCA() + " " + obj.getVCH_HORAMARCA(),
                Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));

    }

    @Override
    public int getItemCount() {
        return objList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_motivo_incidencia)
        TextView tvMotivoIncidencia;
        @BindView(R.id.tv_mensaje_incidencia)
        TextView tvMensajeIncidencia;
        @BindView(R.id.txt_fecha_ini)
        TextView tvFechaIncidencia;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public void filterList(ArrayList<Incidencia> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        objList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}


