package com.dms.tareosoft.domain.acceso.consulta;

import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;

import java.util.List;

import io.reactivex.Single;

public interface IConsultaAccesoInteractor {
    Single<List<Acceso>> findAccesoByDate(String fch_ini, String fch_fin);


}
