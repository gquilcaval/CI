package com.dms.tareosoft.domain.tareo_resultado;

import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.ResultadoRow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface IResultadoInteractor {

    Flowable<List<ResultadoRow>> obtenerResultadoEmpleados(String codigoTareo);

    Completable guardarResultadoEmpleado(ResultadoTareo nuevo, String codTareo);

    Completable modificarResultadoEmpleado(ResultadoTareo nuevo, String codTareo);

    Single<EmpleadoResultadoRow> validarEmpleado(String codigoTareo, String codEmpleado);

    Completable liquidarTareo(String codDetalleTareo, String fechaModificacion, int usuario);
}

