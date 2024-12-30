package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.presentation.models.EmpleadoConsultaRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TareoConsultaDetailsAdapter extends
        RecyclerView.Adapter<TareoConsultaDetailsAdapter.EmpleadoViewHolder> {

    String TAG = TareoConsultaDetailsAdapter.class.getSimpleName();

    private List<AllEmpleadosConsulta> mDataList;
    private int new_icon = 0;
    private Context mContext;

    public TareoConsultaDetailsAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empleado,
                parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        AllEmpleadosConsulta row = mDataList.get(position);
        holder.onBind(row);
    }

    @Override
    public int getItemCount() {
        if (mDataList != null && mDataList.size() > 0)
            return mDataList.size();
        else
            return 0;
    }

    public void setData(List<AllEmpleadosConsulta> dataList) {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        mDataList.clear();
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setNew_icon(int new_icon) {
        this.new_icon = new_icon;
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_empleado)
        ImageView ivIconoTareo;
        @BindView(R.id.tv_codigoEmpTareo)
        TextView tvCodigoEmpTareo;
        @BindView(R.id.txt_fecha_ini)
        TextView txtfechaIni;
        @BindView(R.id.txt_fecha_Fin)
        TextView txtFechaFin;
        @BindView(R.id.tv_nomApeTareo)
        TextView tvNomApeTareo;
        @BindView(R.id.tv_cant_producida)
        TextView tvCantProducida;
        @BindView(R.id.recycler_control)
        RecyclerView recyclerControl;
        @BindView(R.id.container_controls)
        View containerControls;


        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void onBind(AllEmpleadosConsulta data) {
            Log.e(TAG, "data: " + data);
            if (data.getEmpleado() != null) {
                EmpleadoConsultaRow item = data.getEmpleado();
                if (!TextUtils.isEmpty(item.getCodigoEmpleado()))
                    tvCodigoEmpTareo.setText(item.getCodigoEmpleado());

                if (!TextUtils.isEmpty(item.getEmpleado()))
                    tvNomApeTareo.setText(item.getEmpleado());

                tvCantProducida.setVisibility(View.VISIBLE);
                tvCantProducida.setText(mContext.getResources().getString(R.string.cantidad_producida_x,
                        String.valueOf(item.getCantProducida())));

                if (!TextUtils.isEmpty(item.getFechaHoraIngreso()))
                    txtfechaIni.setText(DateUtil.changeStringDateTimeFormat(item.getFechaHoraIngreso(),
                            Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
                if (!TextUtils.isEmpty(item.getFechaHoraSalida()))
                    txtFechaFin.setText(DateUtil.changeStringDateTimeFormat(item.getFechaHoraSalida(),
                            Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
                if (new_icon != 0)
                    ivIconoTareo.setImageResource(R.drawable.ic_person);
            }

            if (data.getControles() != null
                    && data.getControles().size() > 0) {
                containerControls.setVisibility(View.VISIBLE);
                ChildTareoConsultaAdapter child = new ChildTareoConsultaAdapter();
                recyclerControl.setLayoutManager(new LinearLayoutManager(mContext));
                recyclerControl.addItemDecoration(
                        new com.dms.consultaprecios.utils.SimpleDividerItemDecoration(
                                mContext,
                                R.drawable.line_divider_gray));
                recyclerControl.setAdapter(child);
                child.setData(data.getControles());
            } else {
                containerControls.setVisibility(View.GONE);
            }
        }
    }
}
