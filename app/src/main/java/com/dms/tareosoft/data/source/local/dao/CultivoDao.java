package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.CultivoModel;
import java.util.List;

@Dao
public interface CultivoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllCultivo(List<CultivoModel> entitie);

    @Query("Delete from CultivoModel")
    void deleteAllCultivo();
}
