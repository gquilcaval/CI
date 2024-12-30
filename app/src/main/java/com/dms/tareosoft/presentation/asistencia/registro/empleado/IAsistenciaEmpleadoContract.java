package com.dms.tareosoft.presentation.asistencia.registro.empleado;

import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.CustomDialog;

import java.util.Date;
import java.util.List;

public interface IAsistenciaEmpleadoContract {

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

        void mostrarAsistencias(List<Marcacion> lista);

        void mostrarMensajeRegistroConfirmacion(Empleado empleado, String fecha, String hora, Marcacion marcacion, String tipoEntrada);

        void setTotal(int size);
    }

    interface Presenter extends IBaseContract.Presenter<View> {

        void showDatePickerDialog(FragmentManager fragment, final EditText editText, Long dateTime);

        void showTimePickerDialog(Fragment fragment, final EditText editText);

        void validarUltimaMarcacion(String codEmpleado, String fecha, String hora, Marcacion marcacion, String tipoEntrada);

        void evaluarEmpleado(String codEmpleado, String fecha, String hora, Marcacion marcacion, String tipoEntrada);

        void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora, Marcacion marcacion, String tipoEntrada);

        List<DetalleTareo> obtenerEmpleados();

        boolean validarFecha(Date date, String hora);

        boolean validarHora(String fecha, Date hora);

        void setFechaInicio(String fechaInicio);

        void setHoraInicio(String horaInicio);

        void validarVista();

        boolean isRegisterNotEmployer();

        void registrarAsistencia(Marcacion marcacion);

        void obtenerAsistencias();
    }
}
