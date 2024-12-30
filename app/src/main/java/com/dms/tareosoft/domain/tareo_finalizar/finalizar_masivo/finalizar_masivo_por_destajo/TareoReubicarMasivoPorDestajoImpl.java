package com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo.finalizar_masivo_por_destajo;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.domain.models.CantEmpleados;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;
import io.reactivex.Single;

public class TareoReubicarMasivoPorDestajoImpl implements ITareoReubicarMasivoPorDestajoInteractor {

    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public TareoReubicarMasivoPorDestajoImpl(@NonNull DataSourceLocal dataSourceLocal,
                                             @NonNull DataSourceRemote dataSourceRemote,
                                             @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }

    @Override
    public Maybe<List<TareoRow>> obtenerTareosIniciados(int usuario) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                return remote.obtenerListaTareo(StatusTareo.TAREO_ACTIVO, usuario,
                        StatusDescargaServidor.PENDIENTE);
            case ModoTrabajo.BATCH:
                return local.obtenerListaTareo(StatusTareo.TAREO_ACTIVO, usuario,
                        StatusDescargaServidor.PENDIENTE);
            default:
                return null;
        }
    }

    @Override
    public Single<Long> finalizarTareosParaReubicar(List<String> listaCodDetalleTareo,
                                                    List<String> listaCodTareos, String fechaFinTareo,
                                                    String horaFinTareo,
                                                    @StatusRefrigerio int statusRefrigerio,
                                                    String fechaIniRefrigerio, String fechaFinRefrigerio,
                                                    @StatusFinDetalleTareo int statusFinTareo,
                                                    @StatusEmpleado int statusEmpleado,
                                                    @StatusTareo int statusTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.finalizarTareoParaReubicar(listaCodDetalleTareo, listaCodTareos,
                        fechaFinTareo, horaFinTareo, statusRefrigerio, fechaIniRefrigerio,
                        fechaFinRefrigerio, statusFinTareo, statusEmpleado, statusTareo);
            default:
                return null;
        }
    }

    @Override
    public Single<Long> finalizarEmpleadoParaReubicar(List<String> listaCodDetalleTareo,
                                                      List<String> listaCodEmpleado,
                                                      @StatusRefrigerio int statusRefrigerio,
                                                      String fechaFinTareo, String horaFinTareo,
                                                      String horaIniRefrigerio, String horaFinRefrigerio,
                                                      @StatusFinDetalleTareo int estadoFinTareo,
                                                      @StatusEmpleado int statusEmpleado,
                                                      List<CantEmpleados> listCantEmpleados) {

        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.finalizarEmpleadoParaReubicar(listaCodDetalleTareo,
                        listaCodEmpleado, statusRefrigerio, fechaFinTareo, horaFinTareo,
                        horaIniRefrigerio, horaFinRefrigerio, estadoFinTareo, statusEmpleado,
                        listCantEmpleados);
            default:
                return null;
        }
    }

    @Override
    public Single<Long> crearResultadoParaReubicarEmpleado(List<ResultadoTareo> resultadoTareo,
                                                           List<AllEmpleadoRow> cantProducida) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo revisar url
                return null;
            case ModoTrabajo.BATCH:
                return local.crearResultadoParaReubicarEmpleado(resultadoTareo, cantProducida);
            default:
                return null;
        }
    }
}
