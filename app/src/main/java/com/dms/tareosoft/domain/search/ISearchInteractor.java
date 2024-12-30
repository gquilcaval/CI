package com.dms.tareosoft.domain.search;

import com.dms.tareosoft.data.entities.ConceptoTareo;

import java.util.List;

import io.reactivex.Single;

public interface ISearchInteractor {

    Single<List<ConceptoTareo>> listarConceptosTareo(int idNivel, int idPadre);

    Single<List<ConceptoTareo>> listarConceptosTareoLike(int idNivel, int idPadre, String search);

    Single<List<ConceptoTareo>> listarConceptosTareoLikeCod(int idNivel, int idPadre, String search);
}
