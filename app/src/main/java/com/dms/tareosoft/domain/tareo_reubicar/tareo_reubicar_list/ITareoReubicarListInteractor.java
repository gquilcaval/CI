package com.dms.tareosoft.domain.tareo_reubicar.tareo_reubicar_list;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ITareoReubicarListInteractor {

    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int estado, int usuario,
                                                 @StatusDescargaServidor String estadoEnvio,
                                                 List<String> listCodTareos,
                                                 List<Integer> listCodClase);

    Single<Long> createNewDetalleTareo(List<DetalleTareo> detalleTareos);
}
