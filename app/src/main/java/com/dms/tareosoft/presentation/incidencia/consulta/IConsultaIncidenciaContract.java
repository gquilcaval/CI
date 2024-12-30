package com.dms.tareosoft.presentation.incidencia.consulta;

import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;

import java.util.List;

public interface IConsultaIncidenciaContract {

    interface View extends IBaseContract.View {
        void displayIncidencias(List<Incidencia> lista);

        void limpiarRecyclerview();
    }

    interface Presenter extends IBaseContract.Presenter<View>{
        void searchIncidenciasByDate(String fch_ini, String fch_fin);

        void  showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime, boolean search);

    }
}
