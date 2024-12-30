package com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo.finalizar_masivo_por_destajo_cantidad;

import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ITareoReubicarMasivoPorDestajoCantidadInteractor {

    Maybe<List<TareoRow>> obtenerTareosIniciados(int usuario);

    Single<Long> finalizarTareosParaReubicar(List<String> listaCodDetalleTareo,
                                             List<String> listaCodTareos, String fechaFinTareo,
                                             String horaFinTareo,
                                             @StatusRefrigerio int statusRefrigerio,
                                             String fechaIniRefrigerio, String fechaFinRefrigerio,
                                             @StatusFinDetalleTareo int statusFinTareo,
                                             @StatusEmpleado int statusEmpleado,
                                             @StatusTareo int statusTareo);

    Single<Long> finalizarEmpleadoParaReubicar(List<String> listaCodDetalleTareo,
                                               List<String> listaCodEmpleado,
                                               @StatusRefrigerio int statusRefrigerio,
                                               String fechaFinTareo, String horaFinTareo,
                                               String horaIniRefrigerio, String horaFinRefrigerio,
                                               @StatusFinDetalleTareo int estadoFinTareo,
                                               @StatusEmpleado int statusEmpleado,
                                               List<CantEmpleados> listCantEmpleados);

    Single<Long> crearResultadoParaReubicarEmpleado(ArrayList<ResultadoTareo> resultadoTareo,
                                                    ArrayList<AllEmpleadoRow> cantProducida);

    Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codTareo, String codEmpleado);
}
