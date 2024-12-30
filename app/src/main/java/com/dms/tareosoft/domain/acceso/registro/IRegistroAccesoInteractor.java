package com.dms.tareosoft.domain.acceso.registro;

import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.ClaseTareo;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface IRegistroAccesoInteractor {

    Maybe<Long> registrarAccesoLocal(Acceso acceso);


}
