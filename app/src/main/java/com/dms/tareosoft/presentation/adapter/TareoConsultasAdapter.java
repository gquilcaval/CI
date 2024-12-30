package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareoConsultasAdapter extends RecyclerView.Adapter<TareoConsultasAdapter.TareoViewHolder> {

    private List<AllTareoRow> mDataList;
    private Context mContext;
    private AdapterListener.selectTareo mListener;

    public TareoConsultasAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public TareoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_view_all_tareos,
                parent, false);
        return new TareoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareoViewHolder holder, int position) {
        AllTareoRow row = mDataList.get(position);
        holder.bind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null
                && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<AllTareoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setListener(AdapterListener.selectTareo listener) {
        mListener = listener;
    }

    public class TareoViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.container)
        View container;
        @BindView(R.id.iv_icono_tareo)
        ImageView ivIconoTareo;
        @BindView(R.id.tv_name_tareo)
        TextView tvNameTareo;
        @BindView(R.id.tv_status_tareo)
        TextView tvStatusTareo;
        @BindView(R.id.tv_status_servidor)
        TextView tvStatusServidor;
        @BindView(R.id.tv_date_time)
        TextView tvDateTime;

        private AllTareoRow allTareoRow;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnClickListener(this);
        }

        public void bind(AllTareoRow item) {
            allTareoRow = item;
            tvNameTareo.setText(allTareoRow.getTareo());
            String status = "";
            String statusServidor = "";
            switch (allTareoRow.getStatus()) {
                case StatusTareo.TAREO_ELIMINADO:
                    status = "Eliminado";
                    break;
                case StatusTareo.TAREO_ACTIVO:
                    status = "Activo";
                    break;
                case StatusTareo.TAREO_FINALIZADO:
                    status = "Finalizado";
                    ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
                    break;
                case StatusTareo.TAREO_LIQUIDADO:
                    status = "Liquidado";
                    break;
            }
            switch (allTareoRow.getStatusServidor()) {
                case StatusDescargaServidor.BACKUP:
                    statusServidor = "Backup";
                    break;
                case StatusDescargaServidor.ENVIADO:
                    statusServidor = "Enviado";
                    break;
                case StatusDescargaServidor.PENDIENTE:
                    statusServidor = "Pendiente";
                    break;
            }
            tvStatusTareo.setText(mContext.getResources().getString(R.string.status_tareo_x, status));
            tvStatusServidor.setText(mContext.getResources().getString(R.string.status_servidor_x, statusServidor));
            tvDateTime.setText(DateUtil.changeStringDateTimeFormat(allTareoRow.getHoraFecha(),
                    Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container:
                    if (mListener != null)
                        mListener.onItemSelectTareo(allTareoRow);
                    break;
            }
        }
    }
}
