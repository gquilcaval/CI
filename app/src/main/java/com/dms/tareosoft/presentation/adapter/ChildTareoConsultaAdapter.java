package com.dms.tareosoft.presentation.adapter;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.presentation.models.EmpleadoConsultaRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChildTareoConsultaAdapter extends
        RecyclerView.Adapter<ChildTareoConsultaAdapter.EmpleadoViewHolder> {

    String TAG = ChildTareoConsultaAdapter.class.getSimpleName();

    private List<DetalleTareoControl> mDataList;
    private int new_icon = 0;

    public ChildTareoConsultaAdapter() {
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_simple,
                parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        DetalleTareoControl row = mDataList.get(position);
        holder.onBind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<DetalleTareoControl> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSimpleText)
        TextView tvSimpleText;


        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(DetalleTareoControl data) {
            Log.e(TAG, "data: " + data);
            tvSimpleText.setText(DateUtil.changeStringDateTimeFormat(
                    data.getFechaControl() + " " + data.getHoraControl(),
                    Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
        }
    }
}
