package com.dms.tareosoft.presentation.tareoNew.tareoNewNotEmployer.definicion;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.List;

public interface IDefNewTareoNotEmployerContract {

    interface View extends IBaseContract.View {
        void listarClaseTareos(List<String> lista, int posicion);

        void actualizarVistaNiveles(List<NivelTareo> listaNivel);

        boolean hasTareo();
    }

    interface Presenter extends IBaseContract.Presenter<IDefNewTareoNotEmployerContract.View> {

        void obtenerClasesTareo(int clase);

        void setClaseTareo(int pos);

        void setNivel1(int idConceptoTareo);

        void setNivel2(int idConceptoTareo);

        void setNivel3(int idConceptoTareo);

        void setNivel4(int idConceptoTareo);

        void setNivel5(int idConceptoTareo);

        String obtenerNomina();

        Tareo obtenerTareo();
    }
}
