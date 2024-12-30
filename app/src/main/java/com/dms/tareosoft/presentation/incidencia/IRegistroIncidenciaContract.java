package com.dms.tareosoft.presentation.incidencia;

import com.dms.tareosoft.base.IBaseContract;
import com.dms.tareosoft.data.entities.Acceso;
import com.dms.tareosoft.data.entities.Incidencia;
import com.dms.tareosoft.data.entities.NivelTareo;

import java.util.List;

public interface IRegistroIncidenciaContract {

    interface View extends IBaseContract.View {
        void listarClaseTareos(List<String> lista, int posicion);

        void actualizarVistaNiveles(List<NivelTareo> listaNivel);

        boolean hasTareo();

        void limpiarCampos();
    }

    interface Presenter extends IBaseContract.Presenter<View> {
        void registroIncidencia(Incidencia incidencia);

        void obtenerClasesTareo();

        void setClaseTareo(int pos);

        void setNivel1(String idConceptoTareo);

        void setNivel2(String actividad);

        void setNivel3(String msg);

        Incidencia obtenerIncidencia();

        String getPerfilUser();
    }
}
