package com.dms.tareosoft.domain.tareo_consultar;

import com.dms.tareosoft.presentation.models.AllTareoRow;

import java.util.List;

import io.reactivex.Single;

public interface ITareoConsultarInteractor {
    Single<List<AllTareoRow>> obtenerAllTareos(int usuario);
}
