package com.dms.tareosoft.presentation.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EmpleadoSimpleAdapter extends RecyclerView.Adapter<EmpleadoSimpleAdapter.EmpleadoViewHolder> {

    private List<EmpleadoRow> mDataList;
    private int new_icon = 0;

    public EmpleadoSimpleAdapter(List<EmpleadoRow> listDoc) {
        this.mDataList = listDoc;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empleado_new_tareo,
                parent, false);
        return new EmpleadoViewHolder(view);
    }

    public void update(List<EmpleadoRow> e) {
        mDataList = e;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        EmpleadoRow row = mDataList.get(position);
        holder.bind(row);

    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setNew_icon(int new_icon) {
        this.new_icon = new_icon;
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_codigoEmpTareo)
        TextView tvCodigoEmpTareo;
        @BindView(R.id.tv_nomApeTareo)
        TextView tvNomApeTareo;
        @BindView(R.id.txt_fecha_ini)
        TextView tvFechaEmpTareo;
        @BindView(R.id.iv_empleado)
        ImageView ivIconoTareo;
        @BindView(R.id.imvTipoIngreso)
        ImageView imvTipoIngreso;
        @BindView(R.id.ly_empleado)
        ConstraintLayout lyEmpleado;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(EmpleadoRow row) {
            if (row != null) {
                Log.d("AQUI", "adapter EMPLEADO ROW " + row);
                if (!TextUtils.isEmpty(row.getCodigoEmpleado()))
                    tvCodigoEmpTareo.setText(row.getCodigoEmpleado());
                if (!TextUtils.isEmpty(row.getEmpleado()))
                    tvNomApeTareo.setText(row.getEmpleado());
                if (!TextUtils.isEmpty(row.getTipoIngreso())){
                    if (row.getTipoIngreso().equals("E")) {
                        imvTipoIngreso.setImageResource(R.drawable.ic_circle_up_);
                    } else {
                        imvTipoIngreso.setImageResource(R.drawable.ic_circle_down);
                    }
                }
                if (!TextUtils.isEmpty(row.getFechaHoraIngreso())) tvFechaEmpTareo.setText(row.getFechaHoraIngreso());

                if (new_icon != 0) { //srcCompat
                    ivIconoTareo.setImageResource(R.drawable.ic_visibility);
                }
                try{
                    if (row.getSupervisor().toLowerCase(Locale.ROOT).equals("si")) {

                        lyEmpleado.setBackgroundResource(R.drawable.round_tareo_superv);
                    }
                } catch (Exception e) {
                    Log.d("EmpleadoSimpleAdapter", e.toString());
                }

            }
        }
    }
}
