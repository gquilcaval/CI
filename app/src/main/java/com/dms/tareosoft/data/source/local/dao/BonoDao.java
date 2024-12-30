package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.BonoModel;

import java.util.List;

@Dao
public interface BonoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllBono(List<BonoModel> entitie);

    @Query("Delete from BonoModel")
    void deleteAllBono();
}
