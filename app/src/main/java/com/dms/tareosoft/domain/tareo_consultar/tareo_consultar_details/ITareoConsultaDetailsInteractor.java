package com.dms.tareosoft.domain.tareo_consultar.tareo_consultar_details;

import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ITareoConsultaDetailsInteractor {

    Single<TareoRow> obtenerTareoDetail(String codTareo);

    Flowable<List<AllEmpleadosConsulta>> obtenerEmpleadosConsulta(String codTareo);

    Completable deleteTareo(String codigoTareo, HashMap<String, Integer> body);
}