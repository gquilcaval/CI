package com.dms.tareosoft.presentation.tareoNew.tareo_nuevo.empleado;

import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.CustomDialog;

import java.util.Date;
import java.util.List;

public interface IEmpleadoContract {

    interface View extends IBaseContract.View {
        void mostrarEmpleados(List<EmpleadoRow> lista);

        void agregarEmpleado(EmpleadoRow row);

        void quitarEmpleado(int position);

        List<DetalleTareo> getEmpleados();

        void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton);

        boolean hasTareo();

        void playSound();

        void reiniciarFechaInicio();

        void reiniciarHoraInicio();

        void mostrarTodo();

        void ocultarTodo();

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        void mostrarMensajeRegistroConfirmacion(Empleado empleado, String fecha, String hora);
    }

    interface Presenter extends IBaseContract.Presenter<IEmpleadoContract.View> {

        void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime);

        void showTimePickerDialog(Fragment fragment, final EditText editText);

        void evaluarEmpleado(String codEmpleado, String nomina, String fecha, String hora);

        void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora);

        List<DetalleTareo> obtenerEmpleados();

        boolean validarFecha(Date date, String hora);

        boolean validarHora(String fecha, Date hora);

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        void validarVista();

        boolean isRegisterNotEmployer();
    }
}
