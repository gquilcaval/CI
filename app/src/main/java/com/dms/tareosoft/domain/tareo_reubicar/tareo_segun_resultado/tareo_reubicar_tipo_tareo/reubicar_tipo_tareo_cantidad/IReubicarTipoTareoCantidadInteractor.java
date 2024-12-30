package com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_tareo.reubicar_tipo_tareo_cantidad;

import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;

import io.reactivex.Single;

public interface IReubicarTipoTareoCantidadInteractor {

    Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codTareo, String codEmpleado);
}
