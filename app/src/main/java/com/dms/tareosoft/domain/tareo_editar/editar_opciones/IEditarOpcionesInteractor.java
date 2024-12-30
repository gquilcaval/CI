package com.dms.tareosoft.domain.tareo_editar.editar_opciones;

import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.entities.Turno;
import com.dms.tareosoft.data.entities.UnidadMedida;

import io.reactivex.Single;

public interface IEditarOpcionesInteractor {

    Single<Tareo> obtenerTareo(String codigoTareo);

    Single<UnidadMedida> obtenerUnidadMedida(int idUnidad);

    Single<Turno> obtenerTurno(int idTurno);
}
