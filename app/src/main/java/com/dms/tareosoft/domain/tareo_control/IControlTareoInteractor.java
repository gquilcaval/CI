package com.dms.tareosoft.domain.tareo_control;

import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface IControlTareoInteractor {
    Completable guardarControl(DetalleTareoControl nuevo);

    Flowable<List<EmpleadoRow>> obtenerControlEmpleados(String codTareo);

    Maybe<EmpleadoControlRow> validarEmpleadoTareo(String codEmpleado, String codTareo);
}
