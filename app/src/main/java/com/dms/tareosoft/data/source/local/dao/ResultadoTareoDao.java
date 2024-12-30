package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.ResultadoTareo;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.presentation.models.ResultadoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

@Dao
public interface ResultadoTareoDao extends BaseDao<ResultadoTareo> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAllResultadoTareo(List<ResultadoTareo> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllResultadoTareoVoid(List<ResultadoTareo> entities);

    @Query("select  d.codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno, 'Sin informacion') as empleado, " +
            " d.vch_FechaSalida || ' ' || d.vch_HoraSalida as fechaHora, " +
            "cantidad as cantidadProducida " +
            "from ResultadoTareo r " +
            "INNER JOIN  DetalleTareo d  on r.fkDetalleTareo = d.codDetalleTareo " +
            "LEFT JOIN Empleado e on d.fkEmpleado = e.id " +
            "where d.fkTareo = :codigoTareo")
    Flowable<List<ResultadoRow>> listaEmpleados(String codigoTareo);

    @Query("UPDATE ResultadoTareo set flgEnvio=:flag WHERE codResultado in (:codigos)")
    Completable actualizarEstado(ArrayList<String> codigos, String flag);

    @Query("UPDATE ResultadoTareo " +
            "set fechaModificacion=:fechaMod, " +
            "cantidad = :cantProducida, " +
            "fkUsuarioUpdate = :userUpdate " +
            "WHERE fkDetalleTareo = :codDetalleTareo")
    Completable actualizarCantidad(String fechaMod, double cantProducida,
                                   int userUpdate, String codDetalleTareo);

    @Query("Delete from ResultadoTareo WHERE flgEnvio= :flag")
    void deleteEnviados(String flag);

    @Query("Delete from ResultadoTareo")
    void deleteAllResultadoTareo();

    @Query("Select * from ResultadoTareo WHERE flgEnvio= :flagResultado and fkDetalleTareo in ( Select codDetalleTareo from DetalleTareo where fkTareo in (:codigos) )")
    Maybe<List<ResultadoTareo>> getAllByEstadoxTareo(ArrayList<String> codigos, String flagResultado);
}
