package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class EmpleadoAdapter extends RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder> implements DeleteCallbackAdapter {

    private List<EmpleadoRow> list;
    private Context context;
    private View view;
    private EmpleadoRow mRecentlyDeletedItem;
    private int mRecentlyDeletedItemPosition;

    public EmpleadoAdapter(List<EmpleadoRow> listDoc, Context context, View view) {
        this.list = listDoc;
        this.context = context;
        this.view = view;
    }

    @NonNull
    @Override
    public EmpleadoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_empleado, parent, false);
        return new EmpleadoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmpleadoViewHolder holder, int position) {
        EmpleadoRow row = list.get(position);
        holder.tvCodigoEmpTareo.setText(row.getCodigoEmpleado());
        holder.tvNomApeTareo.setText(row.getEmpleado());
        holder.tvFechaEmpTareo.setText(DateUtil.changeStringDateTimeFormat(
                row.getFechaHoraIngreso(),
                Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void delete(int position) {
        mRecentlyDeletedItem = list.get(position);
        mRecentlyDeletedItemPosition = position;
        list.remove(position);
        notifyItemRemoved(position);

        showUndoSnackbar();
    }

    private void showUndoSnackbar() {
        Snackbar snackbar = Snackbar.make(view, "snack_bar_text", Snackbar.LENGTH_LONG);
        snackbar.setAction("R.string.snack_bar_undo", v -> undoDelete());
        snackbar.show();
    }

    private void undoDelete() {
        list.add(mRecentlyDeletedItemPosition, mRecentlyDeletedItem);
        notifyItemInserted(mRecentlyDeletedItemPosition);
    }

    public class EmpleadoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvCodigoEmpTareo;
        private TextView tvNomApeTareo;
        private TextView tvFechaEmpTareo;

        public EmpleadoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCodigoEmpTareo = itemView.findViewById(R.id.tv_codigoEmpTareo);
            tvNomApeTareo = itemView.findViewById(R.id.tv_nomApeTareo);
            tvFechaEmpTareo = itemView.findViewById(R.id.txt_fecha_ini);
        }
    }
}
