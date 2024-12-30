package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface NivelesTareoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<NivelTareo> entitie);

    @Query("Delete from NivelTareo")
    void deleteAll();

    @Query("Select * from NivelTareo where fkClase = :idClase order by orden")
    Single<List<NivelTareo>> lista(int idClase);
}
