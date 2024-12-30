package com.dms.tareosoft.presentation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.dms.tareosoft.R;
import com.dms.tareosoft.data.entities.ConceptoTareo;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteConceptoTareoAdapter extends ArrayAdapter<ConceptoTareo> implements Filterable {
    private List<ConceptoTareo> list, originalList, filteredList;
    private CustomFilter customFilter;

    public AutoCompleteConceptoTareoAdapter(@NonNull Context context, @NonNull int resource,
                                            @NonNull List<ConceptoTareo> clientes) {
        super(context, resource, clientes);
        this.list = clientes;
        this.originalList = new ArrayList<>(clientes);
        this.filteredList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CustomFilter getFilter() {
        if (customFilter == null) {
            customFilter = new CustomFilter();
        }
        return customFilter;
    }


    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null) {
                String busqueda = constraint.toString().toLowerCase();
                filteredList.clear();
                for (ConceptoTareo clienteModel : originalList) { //Por cada cliente de la lista original
                    if ((clienteModel.getCodConcepto() + clienteModel.getDescripcion().toLowerCase()).contains(busqueda)) {
                        filteredList.add(clienteModel);
                    }
                }
                results.values = filteredList;
                results.count = filteredList.size();
            } else {
                results.values = originalList;
                results.count = originalList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                clear();
                addAll((List) results.values);
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

    @Nullable
    @Override
    public ConceptoTareo getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        try {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.row_auto_conceptotareo, parent, false);
                holder = new ViewHolder();

                holder.tv_cliente = convertView.findViewById(R.id.tv_descripcion_conceptotareo);
                holder.tv_codigo = convertView.findViewById(R.id.tv_codigo_conceptotareo);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            //Obtener la data del item de la lista filtrada
            ConceptoTareo cliente = getItem(position);
            //Seteamos los valores a las vistas
            holder.tv_cliente.setText(cliente.getDescripcion());
            holder.tv_codigo.setText(cliente.getCodConcepto());
        } catch (Exception e) {
            ConceptoTareo cliente = getItem(position);
            if (cliente != null) {
                Log.e("Autocomplete", "ERROR POR DIRECCION: " + getItem(position).getIdConceptoTareo());
            } else {
                Log.e("Autocomplete", "ERROR POR NULL: " + position);
            }

            e.printStackTrace();
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv_cliente;
        TextView tv_codigo;
    }

    public void updateOriginalList() {
        if (list != null) {
            this.originalList.clear();
            this.originalList.addAll(list);
        } else {
            Log.e("Autocomplete", "Es null");
        }
    }
}
