package com.dms.tareosoft.domain.tareo_editar_emp;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface ITareoEditarEmpInteractor {

    Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);

    Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado);

    Single<List<DetalleTareo>> obtenerEmpleados(String codigoTareo,
                                                @StatusFinDetalleTareo int statusFinTareo);

    Single<List<EmpleadoRow>> obtenerListaEmpleados(String codigoTareo,
                                                    @StatusFinDetalleTareo int statusFinTareo);

    Single<Tareo> obtenerTareo(String codigoTareo);

    Single<Long> actualizarTareo(Tareo tareo);

    Single<Long[]> registrarEmpleadoxsTareo(List<DetalleTareo> detalleTareos);

    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                        @StatusFinDetalleTareo int statusDetalleTareo);
}
