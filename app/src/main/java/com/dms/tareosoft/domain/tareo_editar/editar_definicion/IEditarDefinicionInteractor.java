package com.dms.tareosoft.domain.tareo_editar.editar_definicion;

import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.List;

import io.reactivex.Single;

public interface IEditarDefinicionInteractor {

    Single<List<NivelTareo>> obtenerNivelesTareo(int idClaseTareo);

    Single<List<ConceptoTareo>> listarConceptosTareo(int idNivel);

    Single<ClaseTareo> obtenerClaseTareo(int idClaseTareo);

    Single<Tareo> obtenerTareo(String codigoTareo);
}
