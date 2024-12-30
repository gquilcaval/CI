package com.dms.tareosoft.domain.tareo_nuevo.nuevo_opciones;

import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;

import java.util.List;

import io.reactivex.Single;

public interface IOpcionesInteractor {

    Single<List<UnidadMedida>> listarUnidadMedidas();

    Single<List<Turno>> listarTurnos();
}
