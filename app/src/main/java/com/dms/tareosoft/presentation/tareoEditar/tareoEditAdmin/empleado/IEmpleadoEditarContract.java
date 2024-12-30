package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.empleado;

import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.CustomDialog;

import java.util.Date;
import java.util.List;

public interface IEmpleadoEditarContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<EmpleadoRow> lista);

        void agregarEmpleado(EmpleadoRow row);

        void quitarEmpleado(int position);

        void reiniciarHoraInicio();

        void reiniciarFechaInicio();

        List<DetalleTareo> getEmpleados();

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton);

        boolean hasTareo();

        void playSound();
    }

    interface Presenter extends IBaseContract.Presenter<IEmpleadoEditarContract.View> {

        void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime);

        void showTimePickerDialog(Fragment fragment, final EditText editText);

        void cargarDetalleTareo(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

        void evaluarEmpleado(String codEmpleado, String nomina, String fecha, String hora);

        void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora);

        List<DetalleTareo> getListDetalleTareo();

        boolean validarFecha(Date fecha, String hora);

        boolean validarHora(String fecha, Date time);

        void cargarListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        String getCurrentDate();

        String getCurrentTime();
    }
}
