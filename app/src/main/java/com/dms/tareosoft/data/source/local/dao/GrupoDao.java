package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.GrupoModel;
import java.util.List;

@Dao
public interface GrupoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAllGrupo(List<GrupoModel> entitie);

    @Query("Delete from GrupoModel")
    void deleteAllGrupo();
}
