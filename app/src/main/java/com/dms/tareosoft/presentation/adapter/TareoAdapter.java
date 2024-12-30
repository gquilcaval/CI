package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dms.tareosoft.R;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;
import com.dms.tareosoft.util.Constants;
import com.dms.tareosoft.util.DateUtil;

import java.util.List;

//public class TareoAdapter extends RecyclerView.Adapter<TareoAdapter.TareoViewHolder> implements DeleteCallbackAdapter, PopupMenu.OnMenuItemClickListener {
public class TareoAdapter extends RecyclerView.Adapter<TareoAdapter.TareoViewHolder> implements DeleteCallbackAdapter {
    private AdapterListener.SelectedListener listener;
    private AdapterListener.DeleteListener listenerDelete;
    private List<TareoRow> list;
    private Context context;
    private TareoRow mRecentlyDeletedItem;

    public TareoAdapter(List<TareoRow> listDoc, Context context) {
        this.list = listDoc;
        this.context = context;
    }

    public void setListener(AdapterListener.SelectedListener listener) {
        this.listener = listener;
    }

    public void setListenerDelete(AdapterListener.DeleteListener listener) {
        this.listenerDelete = listener;
    }

    @NonNull
    @Override
    public TareoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tareo, parent, false);
        return new TareoViewHolder(view);
    }

    public void undoDelete(int position) {
        list.add(position, mRecentlyDeletedItem);
        notifyItemInserted(position);
    }

    @Override
    public void delete(int position) {
        if (listener != null) {
            listenerDelete.confirmDelete(list.get(position).getCodigo(), position);
        }
        mRecentlyDeletedItem = list.get(position);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TareoViewHolder holder, int position) {
        TareoRow row = list.get(position);
        String separador = "";
        holder.tvNivel2.setText(row.getConcepto2().toUpperCase());
        if (row.getConcepto5().length() > 1) {
            holder.tvNivel5.setVisibility(View.VISIBLE);
            holder.tvNivel5.setText(row.getConcepto5().toUpperCase());
        } else {
            holder.tvNivel5.setVisibility(View.GONE);
        }

        if (row.getConcepto3().length() > 1) {
            separador = " - ";
        }
        holder.tvNivel4Nivel3.setText(row.getConcepto4().toUpperCase() + separador + row.getConcepto3().toUpperCase());
        holder.tvTrabajadores.setText(row.getCantTrabajadores() + " Trabajadores");

        //Tipo de tareo
        if (row.getTipoTareo() == StatusTareo.TAREO_FINALIZADO) {
            holder.ivIconoTareo.setImageResource(R.drawable.ic_tareo_finalizado);
        }
        //TODO corregir fecha
        holder.tvFechaTareo.setText(DateUtil.changeStringDateTimeFormat(row.getFechaInicio(), Constants.F_DATE_TIME_TAREO, Constants.F_LECTURA));
        holder.itemView.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(context, holder.tvNivel2); // ???

            popup.inflate(R.menu.popup_menu);
            popup.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.elim_menu:
                        delete(position);
                        return true;
                    case R.id.edit_menu:
                        listener.onItemSelected(view, row, position, false);
                        return true;
                }
                return false;
            });
            popup.show();
//            if(listener != null){
//                listener.onItemSelected(row);
//            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public Context getContext() {
        return context;
    }


    public class TareoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivIconoTareo;
        private TextView tvNivel5;
        private TextView tvNivel4Nivel3;
        private TextView tvNivel2;
        private TextView tvTrabajadores;
        private TextView tvFechaTareo;

        public TareoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIconoTareo = itemView.findViewById(R.id.iv_icono_tareo);

            tvNivel5 = itemView.findViewById(R.id.tv_nivel5);
            tvNivel4Nivel3 = itemView.findViewById(R.id.tv_nivel4_nivel3);
            tvNivel2 = itemView.findViewById(R.id.tv_nivel2);
            tvTrabajadores = itemView.findViewById(R.id.tv_trabajadores);
            tvFechaTareo = itemView.findViewById(R.id.tv_total_producido);
        }
    }
}
