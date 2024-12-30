package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
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
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.presentation.models.GroupTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReubicarPorTareoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private OnLongClickListener listener;
    private List<GroupTareoRow> mDataList;
    private Context mContext;

    public ReubicarPorTareoAdapter(Context context) {
        this.mDataList = new ArrayList<>();
        mContext = context;
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
                    .inflate(R.layout.row_reubicar_por_tareo, parent, false);
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

    public void setData(List<GroupTareoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void setListener(OnLongClickListener listener) {
        this.listener = listener;
    }

    public void updateItem(int position, boolean isSelect) {
        mDataList.get(position).getData().setChecked(isSelect);
        //notifyItemChanged(position);
        notifyDataSetChanged();
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

    public class TareoViewHolder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, View.OnClickListener {

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
        @BindView(R.id.cb_select)
        CheckBox cbSelect;

        private TareoRow row;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnLongClickListener(this);
            container.setOnClickListener(this);
        }

        public void bind(GroupTareoRow item) {
            row = item.getData();
            if (row != null) {
                String separador = "";
                if (!TextUtils.isEmpty(row.getConcepto2()))
                    tvNivel2.setText(row.getConcepto2().toUpperCase());

                if (!TextUtils.isEmpty(row.getConcepto5())) {
                    tvNivel5.setVisibility(View.VISIBLE);
                    tvNivel5.setText(row.getConcepto5().toUpperCase());
                } else {
                    tvNivel5.setVisibility(View.GONE);
                }

                if (!TextUtils.isEmpty(row.getConcepto3())) {
                    separador = " - ";
                }

                if (!TextUtils.isEmpty(row.getConcepto4()) &&
                        !TextUtils.isEmpty(row.getConcepto3()))
                    tvNivel4Nivel3.setText(row.getConcepto4().toUpperCase() + separador + row.getConcepto3().toUpperCase());
                else
                    tvNivel4Nivel3.setVisibility(View.GONE);

                tvTrabajadores.setText(mContext.getResources().getQuantityString(
                        R.plurals.cant_trabajadores, row.getCantTrabajadores(), row.getCantTrabajadores()));

                //Tipo de tareo
                if (row.getTipoTareo() == StatusTareo.TAREO_FINALIZADO) {
                    ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
                }
                //TODO solo esta devolviendo la fecho y no la hora lo cual produce un error al convertir
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
        }

        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (listener != null)
                        listener.itemLongClick(row, getAdapterPosition(), true);
                    return true;
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_item:
                    if (listener != null)
                        listener.itemLongClick(row, getAdapterPosition(), false);
                    break;
            }
        }
    }

    public interface OnLongClickListener {
        boolean itemLongClick(TareoRow item, int position, boolean longPress);
    }
}
