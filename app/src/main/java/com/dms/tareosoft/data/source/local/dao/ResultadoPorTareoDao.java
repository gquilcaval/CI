package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.annotacion.StatusDescargaServidor;
import com.dms.tareosoft.data.entities.ResultadoPorTareo;
import com.dms.tareosoft.data.source.local.BaseDao;
import com.dms.tareosoft.presentation.models.AllResultadoPorTareoRow;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

@Dao
public interface ResultadoPorTareoDao extends BaseDao<ResultadoPorTareo> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long[] insertAllResultadoPorTareo(List<ResultadoPorTareo> entities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllResultadoPorTareoVoid(List<ResultadoPorTareo> entities);

    @Query("SELECT rpt.horaRegistro || ' ' || rpt.fechaRegistro as fecha, " +
            "rpt.cantidad as cantidad " +
            "FROM ResultadoPorTareo rpt " +
            "WHERE rpt.fkTareo = :codTareo")
    Maybe<List<AllResultadoPorTareoRow>> obtenerListResultforTareo(String codTareo);

    @Query("SELECT * FROM ResultadoPorTareo rpt " +
            "WHERE rpt.fkTareo in (:codTareo) and rpt.flgEnvio = :statusServer")
    Maybe<List<ResultadoPorTareo>> obtenerResultadoPorTareo(List<String> codTareo,
                                                            @StatusDescargaServidor String statusServer);

    @Query("update ResultadoPorTareo set flgEnvio = :statusServer WHERE fkTareo in (:codigosTareo)")
    Completable actualizarResultadoPorTareo(List<String> codigosTareo,
                                            @StatusDescargaServidor String statusServer);
}
