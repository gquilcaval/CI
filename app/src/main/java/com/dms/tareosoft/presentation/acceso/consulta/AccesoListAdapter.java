package com.dms.tareosoft.presentation.acceso.consulta;


import android.app.AlertDialog;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccesoListAdapter extends RecyclerView.Adapter<AccesoListAdapter.ViewHolder> {

    protected List<Acceso> objList;

    public AccesoListAdapter(List<Acceso> objList) {
        this.objList = objList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_acceso, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Acceso obj = objList.get(position);

        holder.tvCodSolicitud.setText(obj.getCod_solicitud());
        holder.tvAgencia.setText(obj.getAgencia_oficina());

        holder.itemView.setOnClickListener(view -> {
            showCustomDialog(obj, holder.itemView.getContext());
        });

    }

    @Override
    public int getItemCount() {
        return objList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_codigo_solicitud)
        TextView tvCodSolicitud;
        @BindView(R.id.tv_agencia)
        TextView tvAgencia;

        ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


    private void showCustomDialog(Acceso obj, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_data_acceso, null);
        builder.setView(dialogView);

        TextView tv_codigo_solicitud = dialogView.findViewById(R.id.tv_codigo_solicitud);
        TextView tv_agencia = dialogView.findViewById(R.id.tv_agencia);
        TextView tv_fecha_ini = dialogView.findViewById(R.id.tv_fecha_ini);
        TextView tv_hora_ini = dialogView.findViewById(R.id.tv_hora_ini);
        TextView tv_fecha_fin = dialogView.findViewById(R.id.tv_fecha_fin);
        TextView tv_hora_fin = dialogView.findViewById(R.id.tv_hora_fin);
        TextView tv_detalle_motivo = dialogView.findViewById(R.id.tv_detalle_motivo);
        TextView tv_zonas = dialogView.findViewById(R.id.tv_zonas);
        TextView tv_area_solicitante = dialogView.findViewById(R.id.tv_area_solicitante);
        TextView tv_motivo = dialogView.findViewById(R.id.tv_motivo);
        TextView tv_empresa = dialogView.findViewById(R.id.tv_empresa);
        TextView tv_visitante = dialogView.findViewById(R.id.tv_visitante);
        TextView tv_equipo_portatil = dialogView.findViewById(R.id.tv_equipo_portatil);
        TextView tv_serie_marca = dialogView.findViewById(R.id.tv_serie_marca);
        ImageView imv_cerrar_dialog = dialogView.findViewById(R.id.imv_cerrar);

        tv_codigo_solicitud.setText(obj.getCod_solicitud());
        tv_agencia.setText(obj.getAgencia_oficina());
        tv_fecha_ini.setText(obj.getFech_inicial());
        tv_hora_ini.setText(obj.getHora_inicio());
        tv_fecha_fin.setText(obj.getFecha_final());
        tv_hora_fin.setText(obj.getHora_final());
        tv_detalle_motivo.setText(obj.getDetalle_motivo());
        tv_zonas.setText(obj.getZonas());
        tv_area_solicitante.setText(obj.getArea_solicitante());
        tv_motivo.setText(obj.getMotivo());
        tv_empresa.setText(obj.getEmpresa());
        tv_visitante.setText(obj.getVisitante_personal());
        tv_equipo_portatil.setText(obj.getEquipo_portatil());
        tv_serie_marca.setText(obj.getSerie_marca());


        AlertDialog dialog = builder.create();

        imv_cerrar_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void filterList(ArrayList<Acceso> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        objList = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }
}


