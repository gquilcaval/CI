package com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IReubicarTipoEmpleadoInteractor {

    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int statusTareo, int usuario,
                                                 @StatusDescargaServidor String statusServidor);

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

    Single<Long> crearResultadoParaReubicarEmpleado(List<ResultadoTareo> resultadoTareo,
                                                    List<AllEmpleadoRow> cantProducida);
}
