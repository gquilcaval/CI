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
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareoReubicarListAdapter extends
        RecyclerView.Adapter<TareoReubicarListAdapter.TareoViewHolder> {

    private AdapterListener.SelectedListener listener;
    private List<TareoRow> mDataList;
    private Context mContext;

    public TareoReubicarListAdapter(Context context) {
        mContext = context;
    }

    public void setListener(AdapterListener.SelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TareoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tareo, parent, false);
        return new TareoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareoViewHolder holder, int position) {
        TareoRow row = mDataList.get(position);
        holder.bind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<TareoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
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

        TareoRow tareoRow;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnClickListener(this);
        }

        public void bind(TareoRow item) {
            tareoRow = item;
            if (tareoRow != null) {
                String separador = "";
                if (!TextUtils.isEmpty(tareoRow.getConcepto2()))
                    tvNivel2.setText(tareoRow.getConcepto2().toUpperCase());

                if (!TextUtils.isEmpty(tareoRow.getConcepto5())) {
                    tvNivel5.setVisibility(View.VISIBLE);
                    tvNivel5.setText(tareoRow.getConcepto5().toUpperCase());
                } else {
                    tvNivel5.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(tareoRow.getConcepto3())) {
                    separador = " - ";
                }

                if (!TextUtils.isEmpty(tareoRow.getConcepto4()) &&
                        !TextUtils.isEmpty(tareoRow.getConcepto3()))
                    tvNivel4Nivel3.setText(tareoRow.getConcepto4().toUpperCase() + separador + tareoRow.getConcepto3().toUpperCase());
                else
                    tvNivel4Nivel3.setVisibility(View.GONE);

                tvTrabajadores.setText(mContext.getResources().getQuantityString(
                        R.plurals.cant_trabajadores, tareoRow.getCantTrabajadores(), tareoRow.getCantTrabajadores()));//row.getCantidad() + " Trabajadores");

                if (tareoRow.getTipoTareo() == StatusTareo.TAREO_FINALIZADO) {
                    ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
                }

                //TODO corregir fecha
                tvFechaTareo.setText(DateUtil.changeStringDateTimeFormat(tareoRow.getFechaInicio(),
                        Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (listener != null) {
                        listener.onItemSelected(v, tareoRow, getAdapterPosition(), false);
                    }
            }
        }
    }
}
