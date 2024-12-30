package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.Marcacion;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface MarcacionDao extends BaseDao<Marcacion> {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Tareo> entities);*/

    @Query("Delete from Marcacion")
    void deleteAll();

    //Tipo de tareo : 0= Jornal,1=Destajo,2=Tarea
   /* @Query("Insert into Tareo(codTareo,fkConcepto1,fkConcepto2,fkConcepto3,fkConcepto4,fkConcepto5,fkUnidadMedida,fechaInicio,horaInicio,cantTrabajadores,tipoTareo, estado, fkTurno," +
           "fechaRegistroInicio, horaRegistroInicio,fechaRegistroFin,horaRegistroFin,usuarioInsert ) values (:nuevo)")
   */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> nuevo(Marcacion nuevo);

    @Query("UPDATE Marcacion SET flgEnvio = 'E' WHERE idMarcacion = :idMarcacion")
    Maybe<Integer> updateEnvioMarcacion(int idMarcacion);

    @Query("SELECT * FROM Marcacion")
    Single<List<Marcacion>> listaAsistencias();

    @Query("SELECT * FROM Marcacion where fechaMarca = date('now',  'localtime') order by idMarcacion desc")
    Single<List<Marcacion>> listaAsistenciasHoy();

    @Query("SELECT * FROM Marcacion WHERE flgEnvio == 'P' ")
    Maybe<List<Marcacion>> listaAsistenciaPendientes();

    @Query("SELECT * FROM Marcacion WHERE codigoEmpleado = :codEmpleado order by idMarcacion desc limit 1")
    Single<List<Marcacion>> lastMarcacionByEmpleado(String codEmpleado);

    @Query("SELECT * FROM marcacion where codigoEmpleado like :codEmpleado || '%' or nombreEmpleado like '%' || :nombre || '%' order by idMarcacion desc")
    Single<List<Marcacion>> searchMarcacion(String codEmpleado, String nombre);
}