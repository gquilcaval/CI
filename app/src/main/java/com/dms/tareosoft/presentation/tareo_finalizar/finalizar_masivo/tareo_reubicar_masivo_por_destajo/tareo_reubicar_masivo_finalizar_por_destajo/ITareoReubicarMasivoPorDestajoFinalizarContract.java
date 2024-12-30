package com.dms.tareosoft.presentation.tareo_finalizar.finalizar_masivo.tareo_reubicar_masivo_por_destajo.tareo_reubicar_masivo_finalizar_por_destajo;

import com.dms.tareosoft.base.IBaseContract;

import java.util.Date;

public interface ITareoReubicarMasivoPorDestajoFinalizarContract {

    interface View extends IBaseContract.View {

        void ocultarTodo();

        void mostrarTodo();

        boolean hasFinalizar();

        boolean hasRefrigerio();

        String getFechaFinTareo();

        String getHoraFinTareo();

        String getFechaIniRefrigerio();

        String getFechaFinRefrigerio();
    }

    interface Presenter extends IBaseContract.Presenter<ITareoReubicarMasivoPorDestajoFinalizarContract.View> {
        void validarCampos();

        boolean validarFechaFinTareo(Date date, String time);

        boolean validarFechaFinTareo(String date, Date time);

        boolean validarFechaInicioRefrigerio(Date fecha, String hora);

        boolean validarFechaInicioRefrigerio(String fecha, Date hora);

        boolean validarFechaFinRefrigerio(Date fecha, String hora);

        boolean validarFechaFinRefrigerio(String fecha, Date hora);
    }
}
