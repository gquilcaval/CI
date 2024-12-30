package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.UnidadMedida;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface UnidadMedidaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<UnidadMedida> entitie);

    @Query("Delete from UnidadMedida")
    void deleteAll();

    @Query("SELECT * FROM UnidadMedida")
    Single<List<UnidadMedida>> lista();

    @Query("SELECT * FROM UnidadMedida where id= :idUnidad")
    Single<UnidadMedida> consultar(int idUnidad);
}
