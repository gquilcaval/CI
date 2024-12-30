package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReubicarTipoTareoCantidadAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AllTareosWithResult> mDataList;
    private AdapterListener.selectPorTareo listener;
    private Context mContext;

    public ReubicarTipoTareoCantidadAdapter(Context context) {
        mDataList = new ArrayList<>();
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_tipo_tareo_reubicar, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AllTareosWithResult item = mDataList.get(position);
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

    public void setListener(AdapterListener.selectPorTareo listener) {
        this.listener = listener;
    }

    public void setItem(TareoRow tipoEmpleado, int position) {
        mDataList.get(position).getTareo().setCantProducida(tipoEmpleado.getCantProducida());
        notifyItemChanged(position);
    }

    public void setData(List<AllTareosWithResult> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.container)
        View container;
        @BindView(R.id.txt_tareo)
        TextView txtTareo;
        @BindView(R.id.txt_cant_producida)
        TextView txtCantProducida;
        @BindView(R.id.recycler_tipo_tareo)
        RecyclerView rvResultadosTareo;

        TareoRow tareo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            container.setOnClickListener(this);
        }

        public void bind(AllTareosWithResult item) {
            if (item.getTareo() != null) {
                tareo = item.getTareo();
                txtTareo.setText(tareo.getConcepto1() + " " + tareo.getConcepto2() + " "
                        + tareo.getConcepto3() + " " + tareo.getConcepto4() + " " + tareo.getConcepto5());
                txtCantProducida.setText(mContext.getResources().getString(R.string.cantidad_producida_x,
                        String.valueOf(tareo.getCantProducida())));
            }
            if (item.getResultado() != null
                    && item.getResultado().size() > 0) {
                rvResultadosTareo.setVisibility(View.VISIBLE);

                ChildReubicarTipoTareoCantidaAdapter adapter = new ChildReubicarTipoTareoCantidaAdapter();
                rvResultadosTareo.setLayoutManager(new LinearLayoutManager(mContext));
                rvResultadosTareo.addItemDecoration(
                        new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                                mContext,
                                R.drawable.line_divider));
                rvResultadosTareo.setAdapter(adapter);

                adapter.setData(item.getResultado());

            } else {
                rvResultadosTareo.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container:
                    if (listener != null) {
                        listener.onItemSelected(tareo, getAdapterPosition());
                    }
                    break;
            }
        }
    }
}
