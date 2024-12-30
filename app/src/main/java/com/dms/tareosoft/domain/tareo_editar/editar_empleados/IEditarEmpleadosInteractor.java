package com.dms.tareosoft.domain.tareo_editar.editar_empleados;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IEditarEmpleadosInteractor {
    Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);

    Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado);

    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                        @StatusFinDetalleTareo int statusDetalleTareo);

    Single<List<DetalleTareo>> obtenerListaDetalleTareo(String codigoTareo,
                                                        @StatusFinDetalleTareo int status);

    Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo,
                                                    @StatusFinDetalleTareo int status);
}
