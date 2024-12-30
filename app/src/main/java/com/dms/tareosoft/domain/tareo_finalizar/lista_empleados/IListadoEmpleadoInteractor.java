package com.dms.tareosoft.domain.tareo_finalizar.lista_empleados;

import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;

import java.util.List;

import io.reactivex.Flowable;

public interface IListadoEmpleadoInteractor {
    Flowable<List<EstadoEmpleadoRow>> obtenerEmpleados(String codTareo);
}
