package com.dms.tareosoft.presentation.tareoReubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_finalizar;

import com.dms.tareosoft.base.IBaseContract;

import java.util.Date;

public interface IReubicarTipoEmpleadoFinalizarContract {

    interface View extends IBaseContract.View {

        void mostrarTodo();

        void ocultarTodo();

        boolean hasFinalizar();

        boolean hasRefrigerio();

        String getFechaFinTareo();

        String getHoraFinTareo();

        String getFechaIniRefrigerio();

        String getFechaFinRefrigerio();
    }

    interface Presenter extends IBaseContract.Presenter<IReubicarTipoEmpleadoFinalizarContract.View> {

        void validarCampos();

        boolean validarFechaFinTareo(Date date, String time);

        boolean validarFechaFinTareo(String date, Date time);

        boolean validarFechaInicioRefrigerio(Date fecha, String hora);

        boolean validarFechaInicioRefrigerio(String fecha, Date hora);

        boolean validarFechaFinRefrigerio(Date fecha, String hora);

        boolean validarFechaFinRefrigerio(String fecha, Date hora);
    }
}