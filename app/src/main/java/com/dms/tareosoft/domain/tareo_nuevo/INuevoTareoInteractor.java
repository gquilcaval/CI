package com.dms.tareosoft.domain.tareo_nuevo;

import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface INuevoTareoInteractor {

    Maybe<Long> registrarTareo(Tareo nuevo);

    Single<Long[]> registrarEmpleadosTareo(List<DetalleTareo> empleados);
}
