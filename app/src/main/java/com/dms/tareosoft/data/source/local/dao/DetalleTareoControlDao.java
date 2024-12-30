package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.DetalleTareoControl;
import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.presentation.models.EmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface DetalleTareoControlDao extends BaseDao<DetalleTareoControl> {

    @Query("Select fechaControl || ' ' || horaControl as fechaHora, " +
            "dc.codEmpleado as codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin informacion') as empleado " +
            "from DetalleTareoControl dc " +
            "LEFT JOIN Empleado e on dc.codEmpleado = e.codigoEmpleado " +
            "where dc.fkTareo = :codTareo")
    Flowable<List<EmpleadoRow>> listaEmpleados(String codTareo);

    @Query("UPDATE DetalleTareoControl set flgEnvio= :flag WHERE idControl in (:codigos)")
    Completable actualizarEstado(ArrayList<Integer> codigos, String flag);


    @Query("Delete from DetalleTareoControl WHERE flgEnvio= :flag")
    void deleteEnviados(String flag);

    @Query("Select * from DetalleTareoControl WHERE flgEnvio= :flag")
    Maybe<List<DetalleTareoControl>> pendientes(String flag);

}
