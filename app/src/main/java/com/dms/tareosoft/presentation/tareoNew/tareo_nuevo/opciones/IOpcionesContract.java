package com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.opciones;

import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;
import com.dms.tareosoft.presentation.models.OpcionesTareo;

import java.util.Date;
import java.util.List;

public interface IOpcionesContract {

    interface View extends IBaseContract.View {
        void listarUnidadMedida(List<UnidadMedida> resultado, int seleccion);

        void llenarSpinnerTurno(List<Turno> resultado, int seleccion);

        OpcionesTareo obtenerCampos();
    }

    interface Presenter extends IBaseContract.Presenter<IOpcionesContract.View> {

        void obtenerTurnos();

        void obtenerUnidadesMedidas();

        String getFechaInicio();

        String getHoraInicio();

        boolean validarFechaInicio(Date dateTime);

        void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime);

        void showTimePickerDialog(FragmentManager fragment, final EditText editText);

        void selectTurnoSegunHora(Spinner spinner);

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        void setCodigoTurno(int codigoTurno);

        void setTipoTareo(int tipoTareo);

        void setTipoResultado(int tipoResultado);

        void setCodigoUnidadMedida(int codigoUnidadMedida);

        OpcionesTareo getOpcionesTareo();
    }
}
