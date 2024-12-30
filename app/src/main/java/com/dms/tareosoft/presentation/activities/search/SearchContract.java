package com.dms.tareosoft.presentation.activities.search;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ConceptoTareo;

import java.util.List;

public interface SearchContract {

    interface View extends IBaseContract.View {
        void mostrarConceptoTareo(List<ConceptoTareo> listConceptoTareo);
    }

    interface Presenter extends IBaseContract.Presenter<SearchContract.View> {
        //void obtenerConceptoTareo();

        void obtenerConceptoTareoAndPadre();

        //void obtenerConceptoTareo(String search);

        void obtenerConceptoTareoAndPadre(String search);

        //void obtenerConceptoTareoCod(String search);

        void obtenerConceptoTareoAndPadreCod(String search);

        String getUserName();

        String getPerfilUser();
    }

}
