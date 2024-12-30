package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.Empleado;
import com.dms.tareosoft.annotacion.StatusEmpleado;
import com.dms.tareosoft.presentation.models.EmpleadoControlRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface EmpleadosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Empleado> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //@Query("Insert into Empleado(codigoEmpleado) values (:codTrabajador)")
    Maybe<Long> nuevo(Empleado nuevo);

    @Query("Delete from Empleado")
    void deleteAll();

    @Query("Select * from Empleado where (codigoEmpleado = :codTrabajador or numDoc = :codTrabajador ) limit 1 ")
    Maybe<Empleado> buscarporCodigo(String codTrabajador);

    @Query("Select * from Empleado " +
            "where tipoPlanilla= :codPlanilla and ( codigoEmpleado = :codTrabajador or numDoc = :codTrabajador ) limit 1 ")
    Maybe<Empleado> buscarporNomina(String codPlanilla, String codTrabajador);

    @Query("Update Empleado set identificadorTareo = '', estadoEmpleado= :estado  where identificadorTareo= :codTareo")
    void liberarEmpleados(String codTareo, @StatusEmpleado int estado);

    @Query("Update Empleado set identificadorTareo = '', estadoEmpleado= :estado  " +
            "where identificadorTareo= :codTareo and codigoEmpleado = :codTrabajador")
    void liberarEmpleado(String codTareo, String codTrabajador, @StatusEmpleado int estado);

    @Query("Update Empleado set identificadorTareo = '', estadoEmpleado= :estado  " +
            "where codigoEmpleado in (:codTrabajador) ")
    void liberarEmpleados(List<String> codTrabajador, @StatusEmpleado int estado);

    @Query("Update Empleado set identificadorTareo = null, estadoEmpleado= :estado  " +
            "where identificadorTareo= :codTareo")
    void liberarEmpleadoDeleteTareo(String codTareo, @StatusEmpleado int estado);

    @Query("Update Empleado " +
            "set identificadorTareo = null, " +
            "estadoEmpleado= :estado  " +
            "where identificadorTareo in (:listaCodTareos) ")
    void liberarEmpleadosTareos(List<String> listaCodTareos, @StatusEmpleado int estado);

    @Query("UPDATE Empleado SET identificadorTareo = :codTareo, " +
            "estadoEmpleado = :estado " +
            "WHERE id= :idEmpleado")
    void asignarTareo(String codTareo, @StatusEmpleado int estado, int idEmpleado);

    @Query("UPDATE Empleado SET identificadorTareo=:codTareo , estadoEmpleado = :estado WHERE id in(:empleadoAsignado)")
    void asignarTareo(String codTareo, @StatusEmpleado int estado, ArrayList<Integer> empleadoAsignado);

    @Query("Select dt.codigoEmpleado, " +
            "ifnull(dt.fkEmpleado,'') as idEmpleado, " +
            "count(idControl) as controles " +
            "from DetalleTareo dt " +
            "left join DetalleTareoControl dtc on dt.codigoEmpleado = dtc.codEmpleado " +
            "where dt.fkTareo = :codTareo and ( dt.codigoEmpleado = :codTrabajador or dt.fkEmpleado = :codTrabajador ) limit 1 ")
    Maybe<EmpleadoControlRow> buscarporTareo(String codTrabajador, String codTareo);

    @Query("Update Empleado set identificadorTareo = :identificadorTareo, estadoEmpleado= :estadoEmpleado  " +
            "where identificadorTareo in (:id) ")
    void finalizarEmpleadosParaReubicar(String identificadorTareo,
                                        @StatusEmpleado int estadoEmpleado, List<String> id);
}
