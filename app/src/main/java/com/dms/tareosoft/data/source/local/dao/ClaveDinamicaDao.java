package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.ClaveDinamicaModel;

@Dao
public interface ClaveDinamicaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAllClaveDinamico(ClaveDinamicaModel entitie);

    @Query("Delete from ClaveDinamicaModel")
    void deleteAllClaveDinamico();
}
