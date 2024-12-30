package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.ParcelaModel;
import java.util.List;

@Dao
public interface ParcelaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllParcela(List<ParcelaModel> entitie);

    @Query("Delete from ParcelaModel")
    void deleteAllParcela();
}
