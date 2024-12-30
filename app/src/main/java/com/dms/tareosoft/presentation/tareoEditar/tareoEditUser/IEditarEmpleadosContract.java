package com.dms.tareosoft.presentation.tareoEditar.tareoEditUser;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.util.CustomDialog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface IEditarEmpleadosContract {

    interface View extends IBaseContract.View {

        void mostrarEmpleados(List<EmpleadoRow> lista);

        void agregarEmpleado(EmpleadoRow row);

        void quitarEmpleado(int position);

        void reiniciarHoraInicio();

        void reiniciarFechaInicio();

        List<DetalleTareo> getEmpleados();

        void confirmDialog(String titulo, String mensaje, CustomDialog.IButton iPositiveButton);

        boolean hasTareo();

        void playSound();

        void salir();

        void showDetailedErrorDialog(ArrayList<String> listaErrores);
    }

    interface Presenter extends IBaseContract.Presenter<IEditarEmpleadosContract.View> {

        void cargarTareo(String codTareo);

        void cargarEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

        void evaluarEmpleado(String codEmpleado, String fecha, String hora);

        void registrarEmpleadoTareo(Empleado empleado, String fecha, String hora);

        List<DetalleTareo> obtenerEmpleados();

        boolean validarFecha(Date fecha, String hora);

        boolean validarHora(String fecha, Date time);

        void cargarListaEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);


        String fechaInicio();

        String horaInicio();

        void guardarTareo();

        void guardarEmpleadosTareo(List<DetalleTareo> detalleTareo);
    }

}
