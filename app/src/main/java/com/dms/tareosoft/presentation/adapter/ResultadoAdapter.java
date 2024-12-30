package com.dms.tareosoft.presentation.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.annotacion.TypeResultado;
import com.dms.tareosoft.annotacion.TypeTareo;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.annotacion.ennum.RecyclerViewType;
import com.dms.tareosoft.presentation.models.GroupTareoDetailRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String TAG = ResultadoAdapter.class.getSimpleName();

    private List<GroupTareoDetailRow> mDataList;
    private AdapterListener.SelectedDetailListener listener;

    public ResultadoAdapter() {
    }

    public void setListener(AdapterListener.SelectedDetailListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;
        if (viewType == RecyclerViewType.HEADER.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_header_tareo_reubicar, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == RecyclerViewType.BODY.ordinal()) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_tareo_resultado, parent, false);
            return new TareoViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        GroupTareoDetailRow item = mDataList.get(position);
        if (viewHolder instanceof HeaderViewHolder) {
            final HeaderViewHolder holder = ((HeaderViewHolder) viewHolder);
            holder.bind(item);
        } else if (viewHolder instanceof TareoViewHolder) {
            final TareoViewHolder holder = ((TareoViewHolder) viewHolder);
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

    @Override
    public int getItemViewType(int position) {
        final GroupTareoDetailRow item = mDataList.get(position);
        switch (item.getTypeView()) {
            case TypeView.HEADER:
                return RecyclerViewType.HEADER.ordinal();
            case TypeView.BODY:
                return RecyclerViewType.BODY.ordinal();
            default:
                return -1;
        }

    }

    public void setData(List<GroupTareoDetailRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tareo)
        TextView tvTareo;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GroupTareoDetailRow item) {
            tvTareo.setText(item.getNivel1());
        }
    }

    public class TareoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.container_item)
        View container;
        @BindView(R.id.iv_icono_tareo)
        ImageView ivIconoTareo;
        @BindView(R.id.tv_nivel5)
        TextView tvNivel5;
        @BindView(R.id.tv_nivel4_nivel3)
        TextView tvNivel4Nivel3;
        @BindView(R.id.tv_nivel2)
        TextView tvNivel2;
        @BindView(R.id.tv_trabajadores)
        TextView tvTrabajadores;
        @BindView(R.id.tv_total_producido)
        TextView tvFechaTareo;
        @BindView(R.id.txt_tareo_and_result)
        TextView tvTareoAndResult;

        private TareoRow data;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnClickListener(this);
        }

        public void bind(GroupTareoDetailRow item) {
            Log.e(TAG, "item bind: " + item);
            data = item.getData();
            String separador = "";
            Log.e(TAG, "item data: " + data);
            String mensaje = "";

            if (data != null) {
                if (!TextUtils.isEmpty(data.getConcepto2()))
                    tvNivel2.setText(data.getConcepto2().toUpperCase());

                if (!TextUtils.isEmpty(data.getConcepto5())) {
                    tvNivel5.setVisibility(View.VISIBLE);
                    tvNivel5.setText(data.getConcepto5().toUpperCase());
                } else {
                    tvNivel5.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(data.getConcepto3())) {
                    separador = " - ";
                }

                if (!TextUtils.isEmpty(data.getConcepto4()) &&
                        !TextUtils.isEmpty(data.getConcepto3()))
                    tvNivel4Nivel3.setText(data.getConcepto4().toUpperCase() + separador + data.getConcepto3().toUpperCase());
                else
                    tvNivel4Nivel3.setVisibility(View.GONE);

                tvFechaTareo.setText("Producido: " + data.getCantProducida());

                mensaje = "Empleados: " + data.getCantTrabajadores();

                String tipoAndResult = "";

                switch (data.getTipoResultado()) {
                    case TypeResultado.TIPO_RESULTADO_POR_EMPLEADO:
                        mensaje = mensaje + " " + "Faltantes:" + (data.getCantTrabajadores() - data.getCantTrabajadores());
                        tipoAndResult = "Resultado por empleado";
                        break;
                    case TypeResultado.TIPO_RESULTADO_POR_TAREO:
                        mensaje = mensaje + " ";
                        tipoAndResult = "Resultado por tareo";
                        break;
                }

                switch (data.getTipoTareo()) {
                    case TypeTareo.TIPO_TAREO_JORNAL:
                        tipoAndResult = tipoAndResult + " - Tipo jornal";
                        break;
                    case TypeTareo.TIPO_TAREO_DESTAJO:
                        tipoAndResult = tipoAndResult + " - Tipo destajo";
                        break;
                    case TypeTareo.TIPO_TAREO_TAREA:
                        tipoAndResult = tipoAndResult + " - Tipo tarea";
                        break;
                }

                switch (data.getStatusTareo()) {
                    case StatusTareo.TAREO_ELIMINADO:
                        break;
                    case StatusTareo.TAREO_ACTIVO:
                        ivIconoTareo.setImageResource(R.drawable.ic_tareo);
                        break;
                    case StatusTareo.TAREO_FINALIZADO:
                        ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
                        break;
                    case StatusTareo.TAREO_LIQUIDADO:
                        break;
                }

                tvTrabajadores.setText(mensaje);

                tvTareoAndResult.setText(tipoAndResult);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (listener != null) {
                        listener.onItemSelected(data);
                    }
                    break;
            }
        }
    }
}
