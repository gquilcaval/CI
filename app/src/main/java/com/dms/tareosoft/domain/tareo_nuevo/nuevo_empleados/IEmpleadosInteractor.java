package com.dms.tareosoft.domain.tareo_nuevo.nuevo_empleados;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IEmpleadosInteractor {

    Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);

    Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado);

    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                        @StatusFinDetalleTareo int statusDetalleTareo);

    Single<Long> finalizarTareoEmpleado(String codigoTareo, String codigoEmpleado,
                                        String fechaFinTareo, String horaFinTareo,
                                        Boolean refrigerio, String fechaIniRefrigerio,
                                        String fechaFinRefrigerio, Boolean flgUltimo,
                                        String horaRegistroSalida);

    Maybe<List<CodigoEmpleadoRow>> obtenerCodigoEmpleados(String codTareo,
                                                          @StatusFinDetalleTareo int status);

    Flowable<List<EmpleadoRow>> obtenerEmpleadosSinControl(String codTareo,
                                                           @StatusIniDetalleTareo int estadoIniciado);
}
