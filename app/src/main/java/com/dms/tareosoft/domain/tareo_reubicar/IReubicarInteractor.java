package com.dms.tareosoft.domain.tareo_reubicar;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.List;

import io.reactivex.Maybe;

public interface IReubicarInteractor {

    Maybe<List<AllEmpleadoRow>> obtenerAllEmpleadosWithTareo(@StatusTareo int statusTareo,
                                                             @StatusFinDetalleTareo int statusFinDetalleTareo,
                                                             int idUsuario);

    Maybe<List<TareoRow>> obtenerTareosIniciados(@StatusTareo int statusTareo, int usuario,
                                                 @StatusDescargaServidor String statusServidor);
}
