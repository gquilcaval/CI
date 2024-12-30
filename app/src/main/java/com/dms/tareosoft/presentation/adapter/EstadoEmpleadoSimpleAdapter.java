package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.List;

public class EstadoEmpleadoSimpleAdapter extends RecyclerView.Adapter<EstadoEmpleadoSimpleAdapter.EmpleadoViewHolder>{

    private List<EstadoEmpleadoRow> list;
    private Context context;

    public EstadoEmpleadoSimpleAdapter(List<EstadoEmpleadoRow> listDoc, Context context) {
        this.list = listDoc;
        this.context = context;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_estadoempleado, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        EstadoEmpleadoRow row = list.get(position);

        holder.tvCodigoEmpTareo.setText(row.getCodigoEmpleado());
        holder.tvNomApeTareo.setText(row.getEmpleado());

        if(row.getEstado() == StatusFinDetalleTareo.TAREO_DETALLE_FINALIZADO){
            ((GradientDrawable) holder.layoutEmpleado.getBackground()).setColor( Color.GRAY );
            holder.tvFechaEmpTareo.setText( DateUtil.changeStringDateTimeFormat(row.getFechaHoraSalida(), Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA) );
        }else{
            ((GradientDrawable) holder.layoutEmpleado.getBackground()).setColor( context.getResources().getColor(R.color.colorPrimary) );
            holder.tvFechaEmpTareo.setText( DateUtil.changeStringDateTimeFormat(row.getFechaHoraIngreso(), Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA) );
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCodigoEmpTareo;
        private TextView tvNomApeTareo;
        private TextView tvFechaEmpTareo;
        private ImageView ivIconoTareo;
        private ConstraintLayout layoutEmpleado;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIconoTareo = itemView.findViewById(R.id.iv_empleado);

            tvCodigoEmpTareo = itemView.findViewById(R.id.tv_codigoEmpTareo);
            tvNomApeTareo = itemView.findViewById(R.id.tv_nomApeTareo);
            tvFechaEmpTareo = itemView.findViewById(R.id.txt_fecha_ini);
            layoutEmpleado = itemView.findViewById(R.id.ly_empleado);
        }
    }
}
