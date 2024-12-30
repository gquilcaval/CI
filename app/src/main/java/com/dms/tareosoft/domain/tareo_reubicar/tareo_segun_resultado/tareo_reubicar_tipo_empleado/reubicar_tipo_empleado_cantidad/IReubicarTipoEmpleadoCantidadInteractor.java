package com.dms.tareosoft.domain.tareo_reubicar.tareo_segun_resultado.tareo_reubicar_tipo_empleado.reubicar_tipo_empleado_cantidad;

import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;

import java.util.List;

import io.reactivex.Single;

public interface IReubicarTipoEmpleadoCantidadInteractor {

    Single<EmpleadoResultadoRow> validarResultadoEmpleadoParaReubicar(String codTareo, String codEmpleado);

    Single<List<AllTareosWithResult>> obtenerTareoWithResult(List<String> listTareoEnd);

    Single<Long> guardarResultadoPorTareo(ResultadoPorTareo resultadoPorTareo);
}
