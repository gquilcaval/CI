package com.dms.tareosoft.domain.tareo_finalizar;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

public interface IFinalizadoMasivoInteractor {

    Single<Long> finalizarTareos(ArrayList<String> listaCodTareos, String fechaFinTareo,
                                 String horaFinTareo, String fechaIniRefrigerio,
                                 String fechaFinRefrigerio);

    Single<List<AllEmpleadoRow>> obtenerAllEmpleados(ArrayList<String> listCodTareo,
                                                     @StatusFinDetalleTareo int statusFinTareo);

}
