package com.dms.tareosoft.presentation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;
import com.dms.tareosoft.util.Constants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ResultadoPorTareoAdapter extends
        RecyclerView.Adapter<ResultadoPorTareoAdapter.TareoViewHolder> {

    private List<AllResultadoPorTareoRow> mDataList;

    public ResultadoPorTareoAdapter() {
    }

    @NonNull
    @Override
    public TareoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_reubicar_tipo_tareo,
                parent, false);
        return new TareoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareoViewHolder holder, int position) {
        AllResultadoPorTareoRow row = mDataList.get(position);
        holder.bind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<AllResultadoPorTareoRow> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void clear() {
        mDataList.clear();
    }

    public int countTotal() {
        int total = 0;
        for (AllResultadoPorTareoRow data : mDataList) {
            total = (int) (total + data.getCantidad());
        }
        return total;
    }

    public class TareoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_date)
        TextView txtDate;
        @BindView(R.id.txt_time)
        TextView txtTime;
        @BindView(R.id.txt_cantidad)
        TextView tvCantidad;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AllResultadoPorTareoRow data) {
            Date newDate = new Date(data.getFecha());
            DateFormat sdfDate = new SimpleDateFormat(Constants.F_DATE_LECTURA, Locale.getDefault());
            DateFormat sdfTime = new SimpleDateFormat(Constants.F_TIME_LECTURA, Locale.getDefault());
            txtDate.setText(sdfDate.format(newDate));
            txtTime.setText(sdfTime.format(newDate));
            tvCantidad.setText(String.valueOf(data.getCantidad()));
        }
    }
}
