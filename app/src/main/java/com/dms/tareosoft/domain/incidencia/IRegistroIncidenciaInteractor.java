package com.dms.tareosoft.domain.incidencia;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IRegistroIncidenciaInteractor {
    Observable<List<ClaseTareo>> listarClaseTareo();

    Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo);

    Maybe<Long> registrarIncidenciaLocal(Incidencia incidencia);


}
