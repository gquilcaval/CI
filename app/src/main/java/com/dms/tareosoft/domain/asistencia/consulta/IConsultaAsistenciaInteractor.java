package com.dms.tareosoft.domain.asistencia.consulta;

import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.data.entities.Marcacion;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface IConsultaAsistenciaInteractor {

    Observable<List<Marcacion>> searchAsistenciasEmpleado(String query);
}
