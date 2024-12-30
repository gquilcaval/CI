package com.dms.tareosoft.domain.tareo_nuevo.nuevo_definicion;

import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface IDefinicionInteractor {
    Observable<List<ClaseTareo>> listarClaseTareo();

    Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo);
}
