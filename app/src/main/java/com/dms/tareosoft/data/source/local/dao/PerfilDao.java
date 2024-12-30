package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.Perfil;

import java.util.List;

@Dao
public interface PerfilDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Perfil> entities);

    @Query("Delete from Perfil")
    void deleteAll();
}
