package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.Tareo;
import com.dms.tareosoft.data.pojos.AllTareosWithResult;
import com.dms.tareosoft.data.pojos.TareoPendiente;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.presentation.models.AllTareoRow;
import com.dms.tareosoft.presentation.models.TareoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface TareoDao extends BaseDao<Tareo> {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Tareo> entities);*/

    @Query("Delete from Tareo")
    void deleteAll();

    //Tipo de tareo : 0= Jornal,1=Destajo,2=Tarea
   /* @Query("Insert into Tareo(codTareo,fkConcepto1,fkConcepto2,fkConcepto3,fkConcepto4,fkConcepto5,fkUnidadMedida,fechaInicio,horaInicio,cantTrabajadores,tipoTareo, estado, fkTurno," +
           "fechaRegistroInicio, horaRegistroInicio,fechaRegistroFin,horaRegistroFin,usuarioInsert ) values (:nuevo)")
   */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Maybe<Long> nuevo(Tareo nuevo);

    @Query("Select  t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ifnull(ct2.descripcion,'') as concepto2, " +
            "ifnull(ct3.descripcion,'') as concepto3, " +
            "ifnull(ct4.descripcion,'') as concepto4,  " +
            "ifnull(ct5.descripcion,'') as concepto5, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.tipoTareo as tipoTareo, " +
            "t.fechaInicio || ' ' || t.horaInicio as fechaInicio , " +
            "t.fechaFin||' '||t.horaFin as fechaFin, " +
            "null as isChecked, " +
            "t.cantProducida as cantProducida , " +
            "t.tipoResultado as tipoResultado, " +
            "t.estado as statusTareo " +
            "from Tareo t inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "where estado = :estado " +
            "and (:usuario = 0 OR usuarioInsert = :usuario) " +
            "and flgEnvio = :estadoEnvio " +
            "order by t.codigoClase")
    Maybe<List<TareoRow>> listaTareos(@StatusTareo int estado,
                                      int usuario, String estadoEnvio);

    @Query("Select  t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ifnull(ct2.descripcion,'') as concepto2, " +
            "ifnull(ct3.descripcion,'') as concepto3, " +
            "ifnull(ct4.descripcion,'') as concepto4,  " +
            "ifnull(ct5.descripcion,'') as concepto5, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.tipoTareo as tipoTareo, " +
            "t.fechaInicio || ' ' || t.horaInicio as fechaInicio , " +
            "ifnull(t.fechaFin||' '||t.horaFin,'') as fechaFin, " +
            "null as isChecked, " +
            "t.cantProducida as cantProducida , " +
            "t.tipoResultado as tipoResultado, " +
            "t.estado as statusTareo " +
            "from Tareo t inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "where (estado = :estado or estado =:estado1) " +
            "and (:usuario = 0 OR usuarioInsert = :usuario) " +
            "and flgEnvio = :estadoEnvio " +
            "order by t.codigoClase")
    Maybe<List<TareoRow>> listaTareos(@StatusTareo int estado, @StatusTareo int estado1,
                                      int usuario, String estadoEnvio);

    @Query("Select  t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ct2.descripcion as concepto2, " +
            "ifnull(ct3.descripcion,'') as concepto3, " +
            "ifnull(ct4.descripcion,'') as concepto4, " +
            "ifnull(ct5.descripcion,'') as concepto5, " +
            "count(r.codResultado) as empRegistrados, " +
            "t.tipoResultado as tipoResultado, " +
            "t.estado as statusTareo, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.estado as tipoTareo, " +
            "t.fechaInicio || ' ' || t.horaInicio as fechaInicio , " +
            "ifnull(t.fechaFin||' '||t.horaFin,'') as fechaFin, " +
            "t.cantProducida as cantProducida , " +
            "t.tipoResultado as tipoResultado , " +
            "null as isChecked " +
            "from Tareo t " +
            "inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "inner join DetalleTareo dt on dt.fkTareo = t.codTareo " +
            "left join ResultadoTareo r on r.fkDetalleTareo = dt.codDetalleTareo and r.cantidad > 0 " +
            "where estado != :estado and t.flgEnvio = :estadoEnvio and (:usuario = 0 OR usuarioInsert = :usuario)" +
            "group by t.codTareo,ct1.descripcion, ct2.descripcion,t.cantTrabajadores, t.cantProducida order by t.cantProducida")
    Maybe<List<TareoRow>> listaTareosDetalleFin(@StatusTareo int estado, int usuario,
                                                @StatusDescargaServidor String estadoEnvio);

    @Query("Select * from Tareo where codTareo = :codTareo")
    Single<Tareo> consultarTareo(String codTareo);

    @Query("Update Tareo set cantTrabajadores = :cant, fechaInicio = :fechaInicio, " +
            "horaInicio = :horaInicio, " +
            "usuarioUpdate = :usuarioUpdate  where codTareo = :codTareo")
    void actualizar(String codTareo, int cant, String fechaInicio, String horaInicio,
                    int usuarioUpdate);

    @Query("Delete from Tareo where codTareo = :codigoTareo")
    Completable delete(String codigoTareo);

    @Query("Update Tareo set fechaFin = :fechaFinTareo , horaFin = :horaFinTareo, estado = :estadoTareo where codTareo = :codigoTareo ")
        //flgFechaFin   flgHoraFin  cantProducida    fechaRegistroFin horaRegistroFin  usuarioUpdate
    void finalizarTareo(String codigoTareo, String fechaFinTareo, String horaFinTareo, int estadoTareo);

    @Query("Update Tareo set fechaFin = :fechaFinTareo , " +
            "horaFin = :horaFinTareo, " +
            "fechaRegistroFin = :fechaFinTareo, " +
            "horaRegistroFin = :horaFinTareo, " +
            "estado = :estadoTareo " +
            "where codTareo in (:listaCodTareos )")
    void finalizarTareos(List<String> listaCodTareos, String fechaFinTareo, String horaFinTareo, int estadoTareo);

    @Query("UPDATE Tareo set cantProducida = ( Select sum(cantidad) from Tareo t inner join  DetalleTareo dt on dt.fkTareo = t.codTareo " +
            "inner join ResultadoTareo r on r.fkDetalleTareo = dt.codDetalleTareo and r.cantidad > 0 where dt.fkTareo = :codTareo )" +
            "where codTareo = :codTareo")
    void actualizarTotalProducido(String codTareo);

    @Query("UPDATE Tareo set estado= :estado , fechaModificacion = :fechaModificacion, usuarioUpdate = :usuario WHERE codTareo = :codigoTareo")
    Completable liquidar(String codigoTareo, int estado, String fechaModificacion, int usuario);

    @Transaction
    @Query("Select * from Tareo WHERE estado in ( :condicion1, :condicion2) and flgEnvio = :estadoEnvio")
    Maybe<List<TareoPendiente>> porEstados(@StatusTareo int condicion1, @StatusTareo int condicion2,
                                           @StatusDescargaServidor String estadoEnvio);

    @Query("Select * from Tareo WHERE estado = :status and flgEnvio = :estadoEnvio")
    Maybe<List<TareoPendiente>> porEstados(@StatusTareo int status,
                                           @StatusDescargaServidor String estadoEnvio);

    @Query("Select * from Tareo WHERE estado in ( :condicion1, :condicion2) and flgEnvio = :estadoEnvio")
    Maybe<List<TareoPendiente>> pendientesTipoDestajo(int condicion1, int condicion2, String estadoEnvio); //TODO ver cuantos estan listos para enviar pero pendientes de terminar mostrar mensaje de si quiere proseguir con el envio

    @Query("UPDATE Tareo set flgEnvio= :flag WHERE codTareo in (:codigos)")
    Completable actualizarEstado(ArrayList<String> codigos, String flag);

    @Query("Delete from Tareo WHERE flgEnvio= 'E'")
    void deleteTareosEnviados();

    @Query("UPDATE Tareo set flgEnvio=:flagFinal WHERE flgEnvio = :flagInicial")
    Completable cambiarEstado(String flagInicial, String flagFinal);

    @Query("Update Tareo set cantTrabajadores = (cantTrabajadores - :cantidadEmpleados) " +
            "where codTareo = :codTareos")
    void disminuirEmpleado(String codTareos, int cantidadEmpleados);

    @Query("Update Tareo set cantTrabajadores = (cantTrabajadores + :cantidadEmpleados) " +
            "where codTareo = :codTareos")
    void aumentarEmpleados(String codTareos, int cantidadEmpleados);

    @Query("UPDATE Tareo SET cantProducida = (cantProducida + :cantProducida) " +
            "WHERE codTareo = :codTareos")
    void aumentarCantProducida(double cantProducida, String codTareos);

    @Query("SELECT t.codTareo as codigoTareo, " +
            "ct1.descripcion || ' '|| ifnull(ct2.descripcion,'') || ' '|| ifnull(ct3.descripcion,'')||' '||" +
            "ifnull(ct4.descripcion,'') || ' '|| ifnull(ct5.descripcion,'') as tareo, " +
            "t.estado as status, " +
            "t.flgEnvio as statusServidor, " +
            "t.fechaInicio ||' '||t.horaInicio as horaFecha " +
            "FROM Tareo t " +
            "INNER JOIN ConceptoTareo ct1 on t.fkConcepto1 = ct1.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct2 on t.fkConcepto2 = ct2.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct3 on t.fkConcepto3 = ct3.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct4 on t.fkConcepto4 = ct4.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct5 on t.fkConcepto5 = ct5.idConceptoTareo " +
            "WHERE t.usuarioInsert = :codigoUsuario")
    Single<List<AllTareoRow>> obtenerAllTareos(int codigoUsuario);

    @Query("SELECT t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ct2.descripcion as concepto2, " +
            "ifnull(ct3.descripcion,'')  as concepto3, " +
            "ifnull(ct4.descripcion,'')  as concepto4, " +
            "ifnull(ct5.descripcion,'')  as concepto5, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.estado as tipoTareo, " +
            "t.fechaInicio||' '||t.horaInicio as fechaInicio, " +
            "ifnull(t.fechaFin||' '||t.horaFin,'') as fechaFin, " +
            "null as isChecked, " +
            "t.cantProducida as cantProducida, " +
            "t.tipoResultado as tipoResultado , " +
            "t.estado as statusTareo " +
            "FROM Tareo t " +
            "inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "WHERE t.codTareo = :codTareo")
    Single<TareoRow> obtenerTareoDetail(String codTareo);

    @Query("SELECT t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ct2.descripcion as concepto2, " +
            "ifnull(ct3.descripcion,'')  as concepto3, " +
            "ifnull(ct4.descripcion,'')  as concepto4, " +
            "ifnull(ct5.descripcion,'')  as concepto5, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.estado as tipoTareo, " +
            "t.fechaInicio||' '||t.horaInicio as fechaInicio, " +
            "null as isChecked, " +
            "t.cantProducida as cantProducida, " +
            "t.tipoResultado as tipoResultado , " +
            "t.estado as statusTareo " +
            "FROM Tareo t " +
            "inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "WHERE t.codTareo in (:codTareo)")
    Single<List<AllTareosWithResult>> obtenerTareoWithResult(List<String> codTareo);

    @Query("Select  t.codTareo as codigo, " +
            "t.codigoClase as codigoClase, " +
            "ct1.descripcion as concepto1, " +
            "ct2.descripcion as concepto2, " +
            "ifnull(ct3.descripcion,'') as concepto3, " +
            "ifnull(ct4.descripcion,'') as concepto4, " +
            "ifnull(ct5.descripcion,'') as concepto5, " +
            "t.cantTrabajadores as cantTrabajadores, " +
            "t.estado as tipoTareo, " +
            "t.fechaInicio || ' ' || t.horaInicio as fechaInicio , " +
            "ifnull(t.fechaFin||' '||t.horaFin,'') as fechaFin, " +
            "t.cantProducida as cantProducida , " +
            "t.tipoResultado as tipoResultado , " +
            "t.estado as statusTareo , " +
            "null as isChecked " +
            "from Tareo t " +
            "inner join ConceptoTareo ct1 on t.fkConcepto1= ct1.idConceptoTareo " +
            "left join ConceptoTareo ct2 on t.fkConcepto2= ct2.idConceptoTareo " +
            "left join ConceptoTareo ct3 on t.fkConcepto3= ct3.idConceptoTareo " +
            "left join ConceptoTareo ct4 on t.fkConcepto4= ct4.idConceptoTareo " +
            "left join ConceptoTareo ct5 on t.fkConcepto5= ct5.idConceptoTareo " +
            "where estado = :estado " +
            "and (:usuario = 0 OR usuarioInsert = :usuario) " +
            "and flgEnvio = :estadoEnvio " +
            "and t.codTareo not in (:listCodTareos) " +
            "and t.codigoClase in (:listCodClase)")
    Maybe<List<TareoRow>> obtenerTareosIniciadosOnly(@StatusTareo int estado, int usuario,
                                                     String estadoEnvio, List<String> listCodTareos,
                                                     List<Integer> listCodClase);
}