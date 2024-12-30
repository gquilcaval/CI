package com.dms.tareosoft.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReubicarTipoEmpleadoCantidadAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<AllEmpleadoRow> mDataList;
    private AdapterListener.selectPorEmpleado listener;

    public ReubicarTipoEmpleadoCantidadAdapter() {
        mDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_reubicar_tipo_empleado, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AllEmpleadoRow item = mDataList.get(position);
        if (viewHolder instanceof ViewHolder) {
            final ViewHolder holder = ((ViewHolder) viewHolder);
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setListener(AdapterListener.selectPorEmpleado listener) {
        this.listener = listener;
    }

    public void setItem(AllEmpleadoRow tipoEmpleado, int position) {
        mDataList.get(position).setCantProducida(tipoEmpleado.getCantProducida());
        notifyItemChanged(position);
    }

    public void setData(ArrayList<AllEmpleadoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public boolean containZero() {
        for (AllEmpleadoRow item : mDataList) {
            if (item.getTipoTareo() == TypeTareo.TIPO_TAREO_DESTAJO
                    && item.getCantProducida() <= 0) {
                return false;
            }
        }
        return true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.container_data)
        View containerData;
        @BindView(R.id.tv_empleado)
        TextView tvEmpleado;
        @BindView(R.id.tv_cod_empleado)
        TextView tvCodEmpleado;
        @BindView(R.id.tv_cant_producida)
        TextView tvCantidadProducida;
        @BindView(R.id.tv_cantidad)
        TextView tvCantidad;

        AllEmpleadoRow allEmpleadoRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            containerData.setOnClickListener(this);
        }

        public void bind(AllEmpleadoRow item) {
            allEmpleadoRow = item;
            tvEmpleado.setText(allEmpleadoRow.getEmpleado());
            tvCodEmpleado.setText(allEmpleadoRow.getCodigoEmpleado());
            tvCantidad.setText(String.valueOf(allEmpleadoRow.getCantProducida()));
            switch (item.getTipoTareo()) {
                case TypeTareo.TIPO_TAREO_DESTAJO:
                    tvCantidadProducida.setText("Cantidad Producida: (Obligatoria)");
                    break;
                default:
                    tvCantidadProducida.setText("Cantidad Producida: (Opcional)");
                    break;
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_data:
                    if (listener != null) {
                        listener.onItemSelected(allEmpleadoRow, getAdapterPosition());
                    }
                    break;
            }
        }
    }
}
