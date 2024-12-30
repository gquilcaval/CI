package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.ClaseTareo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ClaseTareoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ClaseTareo> entitie);

    @Query("Delete from ClaseTareo")
    void deleteAll();

    @Query("SELECT * FROM ClaseTareo")
    Single<List<ClaseTareo>> lista();

    @Query("SELECT * FROM ClaseTareo where id = :idClaseTareo")
    Single<ClaseTareo> consultar(int idClaseTareo);
}
