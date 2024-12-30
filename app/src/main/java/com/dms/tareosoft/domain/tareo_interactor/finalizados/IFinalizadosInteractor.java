package com.dms.tareosoft.domain.tareo_interactor.finalizados;

import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface IFinalizadosInteractor {

    Maybe<List<TareoRow>> obtenerTareosFinalizados(int usuario);

    Completable deleteTareo(String codigoTareo);
}
