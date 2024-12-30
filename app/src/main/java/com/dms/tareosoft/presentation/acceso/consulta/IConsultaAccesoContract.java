package com.dms.tareosoft.presentation.acceso.consulta;

import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;

import java.util.List;

public interface IConsultaAccesoContract {

    interface View extends IBaseContract.View {
        void displayAccesos(List<Acceso> lista);

        void limpiarRecyclerview();
    }

    interface Presenter extends IBaseContract.Presenter<View>{
        void searchAccesosByDate(String fch_ini, String fch_fin);

        void  showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime, boolean search);

    }
}
