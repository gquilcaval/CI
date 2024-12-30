package com.dms.tareosoft.domain.tareo_finalizar.finalizar_masivo;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ITareoReubicarMasivoListInteractor {
    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int statusTareo,
                                                 int usuario,
                                                 @StatusDescargaServidor String statusServer);

    Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos);
}
