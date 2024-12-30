package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.dms.tareosoft.data.entities.Turno;
import java.util.List;

import io.reactivex.Single;

@Dao
public interface TurnoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<Turno> entitie);

    @Query("Delete from Turno")
    void deleteAll();

    @Query("SELECT * FROM Turno")
    Single<List<Turno>> lista();

    @Query("SELECT * FROM Turno where id= :idTurno")
    Single<Turno> consultar(int idTurno);
}
