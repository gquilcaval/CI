package com.dms.tareosoft.domain.tareo_interactor.iniciados;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface IIniciadosInteractor {

    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int estado, int usuario,
                                                 @StatusDescargaServidor String status);

    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int estado, @StatusTareo int estado1,
                                                 int usuario, @StatusDescargaServidor String status);

    Completable deleteTareo(String codigoTareo, HashMap<String, Integer> body);
}
