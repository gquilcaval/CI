package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin;

import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.OpcionesTareo;

import java.util.ArrayList;
import java.util.List;

public interface IEditarTareoContract {

    interface View extends IBaseContract.View {
        void salir();

        void changePage(int page);

        void showDetailedErrorDialog(ArrayList<String> listaErrores);

        String getFechaInicio();

        String getHoraInicio();
    }

    interface Presenter extends IBaseContract.Presenter<IEditarTareoContract.View> {

        void actualizarTareo(Tareo nuevoTareo, int niveles, List<DetalleTareo> empleados,
                             OpcionesTareo obtenerCampos);

        void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime);

       void showTimePickerDialog(Fragment fragment, final EditText editText);
    }
}
