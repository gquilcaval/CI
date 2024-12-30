package com.dms.tareosoft.domain.tareo_editar;

import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IEditarTareoInteractor {

    Single<Tareo> consultarTareo(String codigoTareo);

    Single<Long> actualizarTareo(Tareo tareo);

    Single<Long[]> registrarEmpleadoEnTareo(List<DetalleTareo> detalleTareos);
}
