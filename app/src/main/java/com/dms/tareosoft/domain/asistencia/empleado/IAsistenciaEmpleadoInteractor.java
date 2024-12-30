package com.dms.tareosoft.domain.asistencia.empleado;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IAsistenciaEmpleadoInteractor {
    Observable<List<ClaseTareo>> listarClaseTareo();

    Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo);

    Observable<List<Marcacion>> listarAsistencias();

    Observable<List<Marcacion>> listarAsistenciasHoy();

    Observable<List<Marcacion>> listarUltimaAsistenciaEmpleado(String codEmpleado);

    Maybe<Empleado> validarExistenciaEmpleado(String codEmpleado);

    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado,
                                                        @StatusFinDetalleTareo int statusDetalleTareo);

    Maybe<Long> registrarAsistencia(Marcacion nuevo);

    Maybe<Empleado> validarEmpleadoPlanilla(String codEmpleado, String codPlanilla);
}
