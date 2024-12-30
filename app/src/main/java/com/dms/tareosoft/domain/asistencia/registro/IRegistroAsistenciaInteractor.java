package com.dms.tareosoft.domain.asistencia.registro;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IRegistroAsistenciaInteractor {

    Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);

    Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado);

    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                        @StatusFinDetalleTareo int statusDetalleTareo);

    Maybe<Long> registrarAsistencia(Marcacion nuevo);
}
