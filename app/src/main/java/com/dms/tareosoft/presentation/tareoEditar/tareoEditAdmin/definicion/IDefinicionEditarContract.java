package com.dms.tareosoft.presentation.tareoEditar.tareoEditAdmin.definicion;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.ConceptoTareo;
import com.dms.tareosoft.data.entities.NivelTareo;
import com.dms.tareosoft.data.entities.Tareo;

import java.util.List;

public interface IDefinicionEditarContract {

    interface View extends IBaseContract.View {
        void actualizarSeccionNiveles(List<NivelTareo> listaNivel);

        void listarConceptoTareo1(List<ConceptoTareo> lista, String descripcion);

        void listarConceptoTareo2(List<ConceptoTareo> lista, String descripcion);

        void listarConceptoTareo3(List<ConceptoTareo> lista, String descripcion);

        void listarConceptoTareo4(List<ConceptoTareo> lista, String descripcion);

        void listarConceptoTareo5(List<ConceptoTareo> lista, String descripcion);

        boolean hasTareo();

        void actualizarClaseTareo(String descripcion);
    }

    interface Presenter extends IBaseContract.Presenter<IDefinicionEditarContract.View> {

        void setCodTareo(String codTareo);

        void cargarTareo();

        void mostrarNiveles(int idClaseTareo);

        void obtenerConceptoTareo1(int idNivel);

        void obtenerConceptoTareo2(int idNivel);

        void obtenerConceptoTareo3(int idNivel);

        void obtenerConceptoTareo4(int idNivel);

        void obtenerConceptoTareo5(int idNivel);

        void setNivel1(int idConceptoTareo);

        void setNivel2(int idConceptoTareo);

        void setNivel3(int idConceptoTareo);

        void setNivel4(int idConceptoTareo);

        void setNivel5(int idConceptoTareo);

        String obtenerNomina();

        Tareo obtenerTareo();
    }
}
