package com.dms.tareosoft.domain.incidencia;

import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IConsultaIncidenciaInteractor {
    Single<List<Incidencia>> findIncidenciaByDate(String fch_ini, String fch_fin);


}
