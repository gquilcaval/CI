package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.models.AllEmpleadoConsultaRow;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareoConsultaFinallizarAdapter
        extends RecyclerView.Adapter<TareoConsultaFinallizarAdapter.TareoViewHolder> {

    private List<AllEmpleadoConsultaRow> mDataList;
    private Context mContext;

    public TareoConsultaFinallizarAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public TareoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tareo_consulta_finalizado,
                parent, false);
        return new TareoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareoViewHolder holder, int position) {
        AllEmpleadoConsultaRow row = mDataList.get(position);
        holder.bind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<AllEmpleadoConsultaRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public class TareoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.container)
        ViewGroup container;
        @BindView(R.id.tv_empleado)
        TextView tvEmpleado;
        @BindView(R.id.tv_cant_producida)
        TextView tvCantProducida;
        private AllEmpleadoConsultaRow allEmpleadoRow;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AllEmpleadoConsultaRow item) {
            allEmpleadoRow = item;
            tvEmpleado.setText(allEmpleadoRow.getEmpleado());
            tvCantProducida.setText(mContext.getResources().getString(R.string.cantidad_producida_x,
                    String.valueOf(allEmpleadoRow.getCantProducida())));
        }
    }
}
