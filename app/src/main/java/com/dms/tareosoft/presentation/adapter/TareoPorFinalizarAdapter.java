package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.annotacion.ennum.RecyclerViewType;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareoPorFinalizarAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = TareoPorFinalizarAdapter.class.getSimpleName();

    private Context mContext;
    private AdapterListener.SelectedListener listener;
    private List<GroupTareoRow> mDataList;

    public TareoPorFinalizarAdapter(Context context) {
        this.mContext = context;
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
                    .inflate(R.layout.row_tareo_seleccion, parent, false);
            return new TareoViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        GroupTareoRow item = mDataList.get(position);
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

    public void setData(List<GroupTareoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
    }

    public void updateItem(boolean select, int position) {
        mDataList.get(position).getData().setChecked(select);
        notifyItemChanged(position);
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

    public class TareoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.container_item)
        View containerItem;
        @BindView(R.id.iv_icono_tareo)
        ImageView ivIconoTareo;
        @BindView(R.id.tv_fundo)
        TextView tvFundo;
        @BindView(R.id.tv_actividad)
        TextView tvActividad;
        @BindView(R.id.tv_trabajadores)
        TextView tvTrabajadores;
        @BindView(R.id.tv_total_producido)
        TextView tvFechaTareo;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        private TareoRow row;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            containerItem.setOnClickListener(this);
            containerItem.setOnLongClickListener(this);
        }

        public void bind(GroupTareoRow item) {
            row = item.getData();
            Log.e(TAG, "bind row: " + row);
            String separador = "";
            if (!TextUtils.isEmpty(row.getConcepto2()))
                tvFundo.setText(row.getConcepto2().toUpperCase());

            /*if (!TextUtils.isEmpty(row.getConcepto5())) {
                tvNivel5.setVisibility(View.VISIBLE);
                tvNivel5.setText(row.getConcepto5().toUpperCase());
            } else {
                tvNivel5.setVisibility(View.GONE);
            }*/

            if (!TextUtils.isEmpty(row.getConcepto3())) {
                separador = " - ";
            }

            if (!TextUtils.isEmpty(row.getConcepto4()) &&
                    !TextUtils.isEmpty(row.getConcepto3()))
                tvActividad.setText(row.getConcepto4().toUpperCase() + separador + row.getConcepto3().toUpperCase());
            else
                tvActividad.setVisibility(View.GONE);

            tvTrabajadores.setText(mContext.getResources().getQuantityString(
                    R.plurals.cant_trabajadores,
                    row.getCantTrabajadores(), row.getCantTrabajadores()));
            cbSelect.setChecked(false);
            cbSelect.setVisibility(View.GONE);

            //Tipo de tareo
            if (row.getTipoTareo() == StatusTareo.TAREO_FINALIZADO) {
                ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
            }
            //TODO corregir fecha
            tvFechaTareo.setText(DateUtil.changeStringDateTimeFormat(row.getFechaInicio(),
                    Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));

            if (row.isChecked()) {
                cbSelect.setChecked(true);
                cbSelect.setVisibility(View.VISIBLE);
            } else {
                cbSelect.setChecked(false);
                cbSelect.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (cbSelect.isChecked()) {
                        updateItem(false, getAdapterPosition());
                    }
                    if (listener != null) {
                        listener.onItemSelected(v, row, getAdapterPosition(), false);
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (listener != null) {
                        listener.onItemSelected(v, row, getAdapterPosition(), true);
                    }
                    return true;
            }
            return false;
        }
    }
}
