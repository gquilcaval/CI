package com.dms.tareosoft.domain.tareo_finalizar;

import androidx.annotation.NonNull;

import com.dms.tareosoft.annotacion.ModoTrabajo;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.PreferenceManager;
import com.dms.tareosoft.data.repository.DataSourceLocal;
import com.dms.tareosoft.data.repository.DataSourceRemote;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class FinalizadoMasivoInteractorImpl implements IFinalizadoMasivoInteractor {
    private final DataSourceLocal local;
    private final DataSourceRemote remote;
    private final PreferenceManager preferenceManager;

    @Inject
    public FinalizadoMasivoInteractorImpl(@NonNull DataSourceLocal dataSourceLocal,
                                          @NonNull DataSourceRemote dataSourceRemote,
                                          @NonNull PreferenceManager preferenceManager) {
        this.local = dataSourceLocal;
        this.remote = dataSourceRemote;
        this.preferenceManager = preferenceManager;
    }


    @Override
    public Single<Long> finalizarTareos(ArrayList<String> listaCodTareos, String fechaFinTareo,
                                        String horaFinTareo, String fechaIniRefrigerio, String fechaFinRefrigerio) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.finalizarTareos(listaCodTareos, fechaFinTareo, horaFinTareo,
                        fechaIniRefrigerio, fechaFinRefrigerio);
            default:
                return null;
        }
    }

    @Override
    public Single<List<AllEmpleadoRow>> obtenerAllEmpleados(ArrayList<String> listCodTareo,
                                                            @StatusFinDetalleTareo int statusFinTareo) {
        switch (preferenceManager.getModoTrabajo()) {
            case ModoTrabajo.LINEA:
                //todo verificar url
                return null;
            case ModoTrabajo.BATCH:
                return local.obtenerAllEmpleadosWithCodTareo(listCodTareo, statusFinTareo);
            default:
                return null;
        }
    }
}
