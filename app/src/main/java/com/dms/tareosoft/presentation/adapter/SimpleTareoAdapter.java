package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.annotacion.ennum.RecyclerViewType;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleTareoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private AdapterListener.SelectedListener listener;
    private List<GroupTareoRow> mDataList;
    private boolean finalizado = false;

    public SimpleTareoAdapter(Context context, List<GroupTareoRow> listDoc) {
        this.mContext = context;
        this.mDataList = listDoc;
    }

    public SimpleTareoAdapter(Context context, List<GroupTareoRow> listDoc, Boolean finalizado) {
        this.mContext = context;
        this.mDataList = listDoc;
        this.finalizado = finalizado;
    }

    public void setListener(AdapterListener.SelectedListener listener) {
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
                    .inflate(R.layout.row_tareo, parent, false);
            return new TareoViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        final GroupTareoRow item = mDataList.get(position);
        if (viewHolder instanceof HeaderViewHolder) {
            final HeaderViewHolder holder = ((HeaderViewHolder) viewHolder);
            holder.bind(item);
        } else if (viewHolder instanceof TareoViewHolder) {
            final TareoViewHolder holder = ((TareoViewHolder) viewHolder);
            holder.bind(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        final GroupTareoRow item = mDataList.get(position);
        switch (item.getTypeView()) {
            case TypeView.HEADER:
                return RecyclerViewType.HEADER.ordinal();
            case TypeView.BODY:
                return RecyclerViewType.BODY.ordinal();
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }


    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_tareo)
        TextView tvTareo;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GroupTareoRow item) {
            tvTareo.setText(item.getNivel1());
        }
    }

    public class TareoViewHolder extends RecyclerView.ViewHolder {

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

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(GroupTareoRow row) {
            TareoRow data = row.getData();
            String separador = "";

            if (!TextUtils.isEmpty(data.getConcepto2()))
                tvNivel2.setText(data.getConcepto2().toUpperCase());

            if (!TextUtils.isEmpty(data.getConcepto5())) {
                tvNivel5.setVisibility(View.VISIBLE);
                tvNivel5.setText(data.getConcepto5().toUpperCase());
            } else {
                tvNivel5.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(data.getConcepto3()))
                separador = " - ";


            if (!TextUtils.isEmpty(data.getConcepto3()) && !TextUtils.isEmpty(data.getConcepto4()))
                tvNivel4Nivel3.setText(data.getConcepto4().toUpperCase() + separador + data.getConcepto3().toUpperCase());
            else
                tvNivel4Nivel3.setVisibility(View.GONE);

            tvTrabajadores.setText(mContext.getResources().getQuantityString(
                    R.plurals.cant_trabajadores, data.getCantTrabajadores(), data.getCantTrabajadores()));

            //TODO corregir fecha
            if (!TextUtils.isEmpty(data.getFechaInicio()))
                tvFechaTareo.setText(DateUtil.changeStringDateTimeFormat(data.getFechaInicio(),
                        Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
            if (finalizado) {
                ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemSelected(view, data, getAdapterPosition(), false);
                    }
                }
            });
        }
    }
}
