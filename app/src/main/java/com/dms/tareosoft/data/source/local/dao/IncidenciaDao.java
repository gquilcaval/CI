package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.source.local.BaseDao;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface IncidenciaDao extends BaseDao<Incidencia> {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Tareo> entities);*/

    @Query("Delete from Incidencia")
    void deleteAll();

    //Tipo de tareo : 0= Jornal,1=Destajo,2=Tarea
   /* @Query("Insert into Tareo(codTareo,fkConcepto1,fkConcepto2,fkConcepto3,fkConcepto4,fkConcepto5,fkUnidadMedida,fechaInicio,horaInicio,cantTrabajadores,tipoTareo, estado, fkTurno," +
           "fechaRegistroInicio, horaRegistroInicio,fechaRegistroFin,horaRegistroFin,usuarioInsert ) values (:nuevo)")
   */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> nuevo(Incidencia nuevo);

    @Query("UPDATE Incidencia SET flgEnvio = 'E' WHERE int_MarcaInsidencia = :idIncidencia")
    Maybe<Integer> updateEnvioIncidencia(int idIncidencia);

    @Query("SELECT * FROM Incidencia")
    Single<List<Incidencia>> listaIncidencia();

    @Query("SELECT * FROM Incidencia WHERE flgEnvio == 'P' ")
    Maybe<List<Incidencia>> listaIncidenciaPendientes();

    @Query("SELECT * FROM Incidencia WHERE DTM_FECMARCA BETWEEN :fchIni and :fchFin")
    Single<List<Incidencia>> getIncidenciasByDate(String fchIni, String fchFin);

   /* @Query("SELECT * FROM Incidencia WHERE codigoEmpleado = :codEmpleado order by idMarcacion desc limit 1")
    Single<List<Marcacion>> lastMarcacionByEmpleado(String codEmpleado);*/

    /*@Query("SELECT * FROM marcacion where codigoEmpleado like :codEmpleado || '%' or nombreEmpleado like '%' || :nombre || '%' order by idMarcacion desc")
    Single<List<Marcacion>> searchMarcacion(String codEmpleado, String nombre);*/
}