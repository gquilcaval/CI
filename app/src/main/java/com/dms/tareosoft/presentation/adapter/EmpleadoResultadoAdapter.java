package com.dms.tareosoft.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.models.ResultadoRow;

import java.util.List;

public class EmpleadoResultadoAdapter extends RecyclerView.Adapter<EmpleadoResultadoAdapter.EmpleadoViewHolder>{

    private List<ResultadoRow> list;

    public EmpleadoResultadoAdapter(List<ResultadoRow> listDoc) {
        this.list = listDoc;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empleado_resultado, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        ResultadoRow row = list.get(position);

        holder.tvCodigoEmpTareo.setText(row.getCodigoEmpleado());
        holder.tvNomApeTareo.setText(row.getEmpleado());
        holder.tvFechaEmpTareo.setText(row.getFechaHora());
        holder.tvCantidad.setText("Producido :"+row.getCantidadProducida());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder{
        private TextView tvCodigoEmpTareo;
        private TextView tvNomApeTareo;
        private TextView tvFechaEmpTareo;
        private TextView tvCantidad;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoEmpTareo = itemView.findViewById(R.id.tv_codigoEmpTareo);
            tvNomApeTareo = itemView.findViewById(R.id.tv_nomApeTareo);
            tvFechaEmpTareo = itemView.findViewById(R.id.txt_fecha_ini);
            tvCantidad = itemView.findViewById(R.id.tv_cantidad);
        }
    }
}
