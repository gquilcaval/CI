package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.ParametrosModel;
import java.util.List;

@Dao
public interface ParametrosDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllParametros(List<ParametrosModel> entitie);

    @Query("Delete from ParametrosModel")
    void deleteAllParametros();
}
