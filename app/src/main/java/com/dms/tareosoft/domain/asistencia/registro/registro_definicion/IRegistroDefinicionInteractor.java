package com.dms.tareosoft.domain.asistencia.registro.registro_definicion;

import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IRegistroDefinicionInteractor {
    Observable<List<ClaseTareo>> listarClaseTareo();

    Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo);

    Observable<List<Marcacion>> listarAsistencias();
}
