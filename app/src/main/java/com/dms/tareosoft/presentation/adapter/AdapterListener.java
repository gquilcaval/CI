package com.dms.tareosoft.presentation.adapter;

import android.view.View;

import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoAdapter;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

public interface AdapterListener {

    interface SelectedListener {
        void onItemSelected(View view, TareoRow item, int position, boolean longPress);
    }

    interface SelectedDetailListener {
        void onItemSelected(TareoRow item);
    }

    interface DeleteListener {
        void confirmDelete(String codigoTareo, int position);
    }

    interface selectEmpleadoReubicar {
        void onItemSelected(AllEmpleadoAdapter allEmpleadoRow, boolean longPress, int position);
    }

    interface selectPorEmpleado {
        void onItemSelected(AllEmpleadoRow AllEmpleadoRow, int position);
    }

    interface selectPorTareo{
        void onItemSelected(TareoRow tareoRow, int position);
    }

    interface selectConcepto {
        void onItemSelected(ConceptoTareo conceptoTareo);
    }

    interface selectTareo {
        void onItemSelectTareo(AllTareoRow allTareoRow);
    }
}
