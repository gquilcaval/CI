package com.dms.tareosoft.data.source.local.dao;

import androidx.annotation.StringDef;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.annotacion.StatusIniDetalleTareo;
import com.dms.tareosoft.annotacion.StatusRefrigerio;
import com.dms.tareosoft.annotacion.StatusTareo;
import com.dms.tareosoft.data.entities.DetalleTareo;
import com.dms.tareosoft.data.pojos.AllEmpleadosConsulta;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.annotacion.StatusFinDetalleTareo;
import com.dms.tareosoft.presentation.models.AllEmpleadoConsultaRow;
import com.dms.tareosoft.presentation.models.AllEmpleadoRow;
import com.dms.tareosoft.presentation.models.CodigoEmpleadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoResultadoRow;
import com.dms.tareosoft.presentation.models.EmpleadoRow;
import com.dms.tareosoft.presentation.models.EstadoEmpleadoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface DetalleTareoDao extends BaseDao<DetalleTareo> {

    /*@Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<DetalleTareo> entities);*/

    @Query("Delete from DetalleTareo")
    void deleteAll();

    // fechaRegistro FECHA DEL TAREO inicio
    //pHoraRegistroIngreso  horaRegistroInicio es la hora actual

    /*
      //  pFechaIngreso  es la dtpInicioManualFecha o la fecha inicio del tareo por defecto
    //pHoraIngreso igual que la fecha de arriba
    * */
    //ESTADOINICIOTAREO", DbType.Int32).Value = 1

    @Insert(onConflict = OnConflictStrategy.REPLACE)
        //@Query("Insert into DetalleTareo(codDetalleTareo,fkTareo,fkEmpleado,codigoEmpleado, fechaRegistro,horaRegistroInicio,fechaIngreso,horaIngreso, flgEstadoIniTareo ) values (:nuevo)")
    Maybe<Long> registrarTrabajadorTareo(DetalleTareo nuevo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long[]> registrarTrabajadoresTareo(List<DetalleTareo> entities);
    /*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] actualizarTrabajadoresTareo(List<DetalleTareo> entities);*/

    @Query("Select d.codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin informacion') as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHoraIngreso " +
            "from DetalleTareo d " +
            "left join Empleado e on d.fkEmpleado = e.id " +
            "where fkTareo= :codigoTareo and flgEstadoFinTareo = :statusFinTareo")
    Single<List<EmpleadoRow>> consultarEmpleados(String codigoTareo, @StatusFinDetalleTareo int statusFinTareo);

    @Query("Select * from DetalleTareo where fkTareo= :codTareo and flgEstadoFinTareo = :statusFinTareo")
    Single<List<DetalleTareo>> consultar(String codTareo, @StatusFinDetalleTareo int statusFinTareo);

    @Query("Update DetalleTareo set vch_FechaSalida = :fechaFinTareo , " +
            "vch_HoraSalida = :horaFinTareo, horaIniRefrigerio = :fechaIniRefrigerio, " +
            "vch_HoraRegistroSalida = :fechaFinTareo|' '|:horaFinTareo, " +
            "horaFinRefrigerio = :fechaFinRefrigerio, flgEstadoFinTareo = :estadoFinTareo " +
            "where fkTareo in (:listaCodTareos) and flgEstadoFinTareo == 0")
    void finalizarDetalleTareos(ArrayList<String> listaCodTareos, String fechaFinTareo,
                                String horaFinTareo, String fechaIniRefrigerio,
                                String fechaFinRefrigerio, int estadoFinTareo);

    @Query("Delete from DetalleTareo where codDetalleTareo in (:listaCodTareos )")
    void deleteDetalleTareoById(List<String> listaCodTareos);

    @Query("Update DetalleTareo set vch_FechaSalida = :fechaFinTareo, " +
            "vch_HoraSalida = :horaFinTareo, " +
            "horaIniRefrigerio = :fechaIniRefrigerio, " +
            "vch_HoraRegistroSalida = :fechaHoraSalida, " +
            "int_FlgHoraRefrigerio = :statusRefrigerio, " +
            "horaFinRefrigerio = :fechaFinRefrigerio, " +
            "flgEstadoFinTareo = :estadoFinTareo " +
            "where codDetalleTareo in (:listCodDetalleTareo) ")
    void finalizarDetalleTareosParaReubicar(List<String> listCodDetalleTareo, @StatusRefrigerio int statusRefrigerio,
                                            String fechaHoraSalida, String fechaFinTareo, String horaFinTareo, String fechaIniRefrigerio,
                                            String fechaFinRefrigerio, @StatusFinDetalleTareo int estadoFinTareo);

    @Query("Update DetalleTareo set vch_FechaSalida = :fechaFinTareo, " +
            "vch_HoraSalida = :horaFinTareo, " +
            "horaIniRefrigerio = :fechaIniRefrigerio, " +
            "horaFinRefrigerio = :fechaFinRefrigerio, " +
            "flgEstadoFinTareo = :estadoFinTareo," +
            "int_FlgHoraRefrigerio = :refrigerio, " +
            "vch_HoraRegistroSalida = :horaRegistroSalida " +
            "where fkTareo =:codTareo and codigoEmpleado = :codTrabajador")
    void finalizarTareoEmpleado(String codTareo, String fechaFinTareo, String horaFinTareo,
                                Boolean refrigerio, String fechaIniRefrigerio, String fechaFinRefrigerio,
                                int estadoFinTareo, String codTrabajador, String horaRegistroSalida);

    @Query("Select d.codigoEmpleado, e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno as empleado," +
            " d.fechaIngreso || ' ' || d.horaIngreso as fechaHoraIngreso " +
            "from DetalleTareo d " +
            "inner join Empleado e on d.fkEmpleado = e.id " +
            "where fkTareo= :codigoTareo and flgEstadoFinTareo= :estado")
    Flowable<List<EmpleadoRow>> listaEmpleadosFinalizados(String codigoTareo, int estado);

    @Query("Select d.codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin informacion') as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHoraIngreso, " +
            "d.vch_FechaSalida || ' ' || d.vch_HoraSalida as fechaHoraSalida , " +
            "flgEstadoFinTareo as estado " +
            "from DetalleTareo d " +
            "left join Empleado e on d.fkEmpleado = e.id " +
            "where fkTareo= :codigoTareo order by flgEstadoFinTareo")
    Flowable<List<EstadoEmpleadoRow>> listaEstadoEmpleados(String codigoTareo);

    @Query("Select d.fkTareo as codTareo, " +
            " d.codigoEmpleado as codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin informacion') as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHoraIngreso, " +
            "ifnull(d.vch_FechaSalida || ' ' || d.vch_HoraSalida, '' ) as fechaHoraSalida , " +
            "ifnull(rt.cantidad, 0)as cantProducida, " +
            "flgEstadoFinTareo as estado " +
            "from DetalleTareo d " +
            "left join Empleado e on d.fkEmpleado = e.id " +
            "left join ResultadoTareo rt on  rt.fkDetalleTareo = d.codDetalleTareo " +
            "where fkTareo= :codigoTareo")
    Flowable<List<AllEmpleadosConsulta>> obtenerEmpleadosConsulta(String codigoTareo);

    @Query("SELECT dt.codigoEmpleado ,dt.fkEmpleado as numeroDocumento " +
            "FROM DetalleTareo dt " +
            "WHERE dt.fkTareo = :codigoTareo AND dt.flgEstadoFinTareo = :estado")
    Maybe<List<CodigoEmpleadoRow>> codigosEmpleadosxEstadoFinTareo(String codigoTareo,
                                                                   @StatusFinDetalleTareo int estado);

    @Query("Select dt.codDetalleTareo as detalleTareo, " +
            "ifnull(rt.cantidad,0) as cantidadProducida " +
            "from DetalleTareo dt " +
            "left join ResultadoTareo rt on  rt.fkDetalleTareo = dt.codDetalleTareo " +
            "where fkTareo = :codigoTareo and codigoEmpleado = :codEmpleado")
    Single<EmpleadoResultadoRow> validarResultadoEmpleado(String codigoTareo, String codEmpleado);

    @Query("Select d.codigoEmpleado as codigoEmpleado, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin informacion') as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHoraIngreso " +
            "from DetalleTareo d " +
            "left join Empleado e on d.fkEmpleado = e.id " +
            "where d.codigoEmpleado not in (select codEmpleado from DetalleTareoControl where fkTareo = :codTareo ) " +
            "and fkTareo = :codTareo and d.flgEstadoIniTareo = :estadoIniciado")
    Flowable<List<EmpleadoRow>> listaEmpleadosSinControl(String codTareo,
                                                         @StatusIniDetalleTareo int estadoIniciado);

    @Query("Delete from DetalleTareo where fkTareo= :codTareo")
    void deleteAllTareo(String codTareo);

    @Query("SELECT d.codDetalleTareo as codigoDetalleTareo, " +
            "t.codTareo as codigoTareo, " +
            "t.codigoClase as codigoClase, " +
            "ifnull(e.codigoEmpleado, d.codigoEmpleado) as codigoEmpleado, " +
            "ct1.descripcion ||' ' || ifnull(ct2.descripcion,'') || ' ' || ifnull(ct3.descripcion,'') || ' ' || " +
            "ifnull(ct4.descripcion,'') || ' ' ||ifnull(ct5.descripcion,'') as tareo, " +
            "ifnull(e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno,'Sin información') as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHora, " +
            "t.tipoTareo as tipoTareo, " +
            "t.tipoResultado as tipoResultado, " +
            "d.fkEmpleado as idEmpleado, " +
            "ifnull(rt.cantidad , 0)as cantProducida, " +
            "0 as cantTrabajadores, " +
            "null as checked " +
            "FROM DetalleTareo d " +
            "LEFT JOIN ResultadoTareo rt on  rt.fkDetalleTareo = d.codDetalleTareo " +
            "INNER JOIN Tareo t on d.fkTareo = t.codTareo " +
            "INNER JOIN ConceptoTareo ct1 on t.fkConcepto1 = ct1.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct2 on t.fkConcepto2 = ct2.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct3 on t.fkConcepto3 = ct3.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct4 on t.fkConcepto4 = ct4.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct5 on t.fkConcepto5 = ct5.idConceptoTareo " +
            "LEFT JOIN Empleado e on d.fkEmpleado = e.id " +
            "WHERE t.estado = :estado and d.flgEstadoFinTareo = :estadoFinTareo AND t.usuarioInsert = :idUsuario ")
    Maybe<List<AllEmpleadoRow>> allEmpleadosWithTareos(@StatusTareo int estado,
                                                       @StatusFinDetalleTareo int estadoFinTareo,
                                                       int idUsuario);

    @Query("SELECT d.codDetalleTareo as codigoDetalleTareo, " +
            "t.codTareo as codigoTareo, " +
            "t.codigoClase as codigoClase, " +
            "e.codigoEmpleado as codigoEmpleado, " +
            "d.fkEmpleado as idEmpleado, " +
            "ct1.descripcion ||' ' || ifnull(ct2.descripcion,'') || ' ' || ifnull(ct3.descripcion,'') || ' ' || " +
            "ifnull(ct4.descripcion,'') || ' ' ||ifnull(ct5.descripcion,'') as tareo, " +
            "e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHora, " +
            "t.tipoTareo as tipoTareo, " +
            "t.tipoResultado as tipoResultado, " +
            "ifnull(rt.cantidad , 0)as cantProducida, " +
            "0 as cantTrabajadores, " +
            "null as checked " +
            "FROM DetalleTareo d " +
            "LEFT JOIN ResultadoTareo rt on  rt.fkDetalleTareo = d.codDetalleTareo " +
            "INNER JOIN Tareo t on d.fkTareo = t.codTareo " +
            "INNER JOIN ConceptoTareo ct1 on t.fkConcepto1 = ct1.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct2 on t.fkConcepto2 = ct2.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct3 on t.fkConcepto3 = ct3.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct4 on t.fkConcepto4 = ct4.idConceptoTareo " +
            "LEFT JOIN ConceptoTareo ct5 on t.fkConcepto5 = ct5.idConceptoTareo " +
            "INNER JOIN Empleado e on d.fkEmpleado = e.id " +
            "WHERE d.fkTareo in (:listCodTareo) and d.flgEstadoFinTareo = :estadoFinTareo ")
    Single<List<AllEmpleadoRow>> obtenerAllEmpleadosWithCodTareo(ArrayList<String> listCodTareo,
                                                                 @StatusFinDetalleTareo int estadoFinTareo);

    @Query("Update DetalleTareo set vch_HoraRegistroSalida = :fechaSalida||' '||:horaSalida, vch_FechaSalida = :fechaSalida , " +
            "vch_HoraSalida = :horaSalida, flgEstadoFinTareo = :estadoFinTareo, int_FlgHoraRefrigerio = :flgEstadoRefirgerio, " +
            "horaIniRefrigerio = :horaIniRefrigerio, horaFinRefrigerio = :horaFinRefrigerio " +
            "where codDetalleTareo = :CodDetalleTareos")
    void finalizarEmpleadoParaReubicar(String fechaSalida, String horaSalida,
                                       @StatusFinDetalleTareo int estadoFinTareo,
                                       @StatusRefrigerio int flgEstadoRefirgerio, String horaIniRefrigerio,
                                       String horaFinRefrigerio, String CodDetalleTareos);

    @Query("SELECT d.codDetalleTareo as codigoDetalleTareo, " +
            "t.codTareo as codigoTareo, " +
            "e.codigoEmpleado as codigoEmpleado, " +
            "ct1.descripcion || ' '|| ct2.descripcion || ' '|| ct3.descripcion as tareo, " +
            "e.nombres || ' ' || e.apellidoPaterno || ' ' || e.apellidoMaterno as empleado, " +
            "d.fechaIngreso || ' ' || d.horaIngreso as fechaHora, " +
            "t.tipoTareo as tipoTareo, " +
            "d.fkEmpleado as idEmpleado, " +
            "ifnull(rt.cantidad,0)as cantProducida " +
            "FROM DetalleTareo d " +
            "LEFT JOIN ResultadoTareo rt on  rt.fkDetalleTareo = d.codDetalleTareo " +
            "INNER JOIN Tareo t on d.fkTareo = t.codTareo " +
            "INNER JOIN ConceptoTareo ct1 on t.fkConcepto1 = ct1.idConceptoTareo " +
            "INNER JOIN ConceptoTareo ct2 on t.fkConcepto2 = ct2.idConceptoTareo " +
            "INNER JOIN ConceptoTareo ct3 on t.fkConcepto3 = ct3.idConceptoTareo " +
            "INNER JOIN Empleado e on d.fkEmpleado = e.id " +
            "WHERE d.fkTareo = :codTareo")
    Single<List<AllEmpleadoConsultaRow>> obtenerAllEmpleados(String codTareo);

    @Query("SELECT * " +
            "FROM DetalleTareo " +
            "WHERE codigoEmpleado = :codEmpleado and flgEstadoFinTareo = :statusDetalleTareo")
    Maybe<DetalleTareo> validarExistenciaEmpleadoporDni(String codEmpleado, @StatusFinDetalleTareo int statusDetalleTareo);
}
