package com.dms.tareosoft.data.source.local.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.dms.tareosoft.data.entities.ConceptoTareo;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface ConceptosTareoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertAll(List<ConceptoTareo> entities);

    @Query("Delete from ConceptoTareo")
    void deleteAll();

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel")
    Single<List<ConceptoTareo>> lista(int idNivel);

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel and fkConceptoPadre= :idPadre")
    Single<List<ConceptoTareo>> listaxPadre(int idNivel, int idPadre);

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel and descripcion like :search ")
    Single<List<ConceptoTareo>> listaLike(int idNivel, String search);

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel and fkConceptoPadre= :idPadre " +
            "and descripcion like  :search")
    Single<List<ConceptoTareo>> listaxPadreLike(int idNivel, int idPadre, String search);

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel and codConcepto like :search ")
    Single<List<ConceptoTareo>> listaLikeCod(int idNivel, String search);

    @Query("Select * from ConceptoTareo where fkNivel = :idNivel and fkConceptoPadre= :idPadre " +
            "and codConcepto like  :search")
    Single<List<ConceptoTareo>> listaxPadreLikeCod(int idNivel, int idPadre, String search);
}