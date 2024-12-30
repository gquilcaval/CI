package com.dms.tareosoft.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.annotacion.ennum.RecyclerViewType;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReubicarPorEmpleadoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllEmpleadoAdapter> mDataList;
    private AdapterListener.selectEmpleadoReubicar listener;

    public ReubicarPorEmpleadoAdapter() {
        this.mDataList = new ArrayList<>();
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
                    .inflate(R.layout.row_body_tareo_reubicar, parent, false);
            return new BodyViewHolder(itemView);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AllEmpleadoAdapter item = mDataList.get(position);
        if (viewHolder instanceof HeaderViewHolder) {
            final HeaderViewHolder holder = ((HeaderViewHolder) viewHolder);
            holder.bind(item);
        } else if (viewHolder instanceof BodyViewHolder) {
            final BodyViewHolder holder = ((BodyViewHolder) viewHolder);
            holder.bind(item);
        }
    }

    @Override
    public int getItemViewType(int position) {
        final AllEmpleadoAdapter item = mDataList.get(position);
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

    public void setData(List<AllEmpleadoAdapter> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setListener(AdapterListener.selectEmpleadoReubicar listener) {
        this.listener = listener;
    }

    public void updateItem(boolean select, int position) {
        mDataList.get(position).getData().setChecked(select);
        notifyItemChanged(position);
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_tareo)
        TextView tvTareo;

        AllEmpleadoAdapter allEmpleadoAdapter;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AllEmpleadoAdapter item) {
            allEmpleadoAdapter = item;
            tvTareo.setText(allEmpleadoAdapter.getConcepto1());
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.container_data)
        View containerData;
        @BindView(R.id.tv_empleado)
        TextView tvEmpleado;
        @BindView(R.id.tv_codigo_empleado)
        TextView tvCodigoEmpleado;
        @BindView(R.id.tv_fecha_hora)
        TextView tvFechaHora;
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        AllEmpleadoAdapter allEmpleadoAdapter;

        public BodyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            containerData.setOnClickListener(this);
            containerData.setOnLongClickListener(this);
        }

        public void bind(AllEmpleadoAdapter item) {
            allEmpleadoAdapter = item;
            tvEmpleado.setText(allEmpleadoAdapter.getData().getEmpleado());
            tvCodigoEmpleado.setText(allEmpleadoAdapter.getData().getCodigoEmpleado());
            tvFechaHora.setText(DateUtil.changeStringDateTimeFormat(
                    allEmpleadoAdapter.getData().getFechaHora(),
                    Constants.F_DATE_TIME_TAREO,
                    Constants.F_LECTURA));
            if (allEmpleadoAdapter.getData().isChecked()) {
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
                case R.id.container_data:
                    if (cbSelect.isChecked()) {
                        updateItem(false, getAdapterPosition());
                    }
                    if (listener != null) {
                        listener.onItemSelected(allEmpleadoAdapter, false, getAdapterPosition());
                    }
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.container_data:
                    if (listener != null) {
                        listener.onItemSelected(allEmpleadoAdapter, true, getAdapterPosition());
                    }
                    return true;
            }
            return false;
        }
    }
}
