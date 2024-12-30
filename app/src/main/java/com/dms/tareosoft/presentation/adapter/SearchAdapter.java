package com.dms.tareosoft.presentation.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.TypeView;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = SearchAdapter.class.getSimpleName();

    private List<ConceptoTareo> mDataList;
    private AdapterListener.selectConcepto listener;

    public SearchAdapter() {
        this.mDataList = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_auto_conceptotareo, parent, false);
        return new BodyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ConceptoTareo item = mDataList.get(position);
        if (viewHolder instanceof BodyViewHolder) {
            final BodyViewHolder holder = ((BodyViewHolder) viewHolder);
            holder.bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void setListener(AdapterListener.selectConcepto listener) {
        this.listener = listener;
    }

    public void setData(List<ConceptoTareo> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.container_data)
        View containerData;
        @BindView(R.id.tv_descripcion_conceptotareo)
        TextView tvDescripcionConceptoTareo;
        @BindView(R.id.tv_codigo_conceptotareo)
        TextView tvCodigoTareo;

        ConceptoTareo conceptoTareo;

        public BodyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            containerData.setOnClickListener(this);
        }

        public void bind(ConceptoTareo item) {
            conceptoTareo = item;
            Log.e(TAG, "conceptoTareo: " + conceptoTareo);
            tvDescripcionConceptoTareo.setText(conceptoTareo.getDescripcion());
            tvCodigoTareo.setText(String.format("%d", conceptoTareo.getIdConceptoTareo()));
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.container_data:
                    if (listener != null) {
                        listener.onItemSelected(conceptoTareo);
                    }
                    break;
            }
        }
    }
}
