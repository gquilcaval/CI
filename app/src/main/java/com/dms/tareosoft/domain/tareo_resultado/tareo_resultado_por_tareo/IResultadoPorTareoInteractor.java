package com.dms.tareosoft.domain.tareo_resultado.tareo_resultado_por_tareo;

import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IResultadoPorTareoInteractor {

    Maybe<List<AllResultadoPorTareoRow>> obtenerListResultforTareo(String codigoTareo);

    Single<Long> guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo);

    Completable liquidarTareo(String codDetalleTareo, String fechaModificacion, int usuario);
}

